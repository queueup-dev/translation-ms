import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.DockerRemoveImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.ajoberstar.grgit.Grgit
import org.springframework.boot.gradle.tasks.bundling.BootJar

group = "${ext["platformGroup"]!!}.api.rest.server"
version = ext["platformVersion"]!!

val dockerRegistry = "${project.findProperty("dockerRegistry") ?: ""}"

apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")

plugins {
    id("com.bmuschko.docker-remote-api") version "4.4.1"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":rest:common"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("io.springfox:springfox-swagger-ui:2.9.2")

    implementation("com.google.code.gson:gson:2.8.5")
}

the<DependencyManagementExtension>().apply {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

tasks.getByName<BootJar>("bootJar") {
    mainClassName = "com.sfl.tms.ServerApplication"
    manifest {
        attributes("Start-Class" to "com.sfl.tms.ServerApplication")
    }
    launchScript()
}

docker {
    registryCredentials {
        url.set("https://$dockerRegistry")
        username.set(System.getenv("DOCKER_REGISTRY_USERNAME"))
        password.set(System.getenv("DOCKER_REGISTRY_PASSWORD"))
    }
}

//region Create and push tags to docker registry

tasks.register<Dockerfile>("createDockerfile") {
    from("openjdk:8-jre-alpine")

    addFile("${System.getProperty("user.dir")}/config/newrelic/newrelic.yml", "/opt/newrelic/newrelic.yml")

    runCommand("apk add --update unzip wget && rm -rf /var/cache/apk/*")
    runCommand("wget https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip -P /tmp")
    runCommand("unzip /tmp/newrelic-java.zip -d /opt/newrelic")

    addFile("${System.getProperty("user.dir")}/rest/server/build/libs/server.jar", "/opt/jar/translation-ms.jar")

    runCommand("touch /opt/jar/translation-ms.jar")

    exposePort(8080)

    entryPoint("java", " -javaagent:/opt/newrelic/newrelic.jar -Dnewrelic.environment=${if (project.hasProperty("environment")) project.properties["environment"] else ""} \$JAVA_OPTS -jar /opt/jar/translation-ms.jar")
}

tasks.register<DockerBuildImage>("buildDockerImage") {
    dependsOn("createDockerfile", "build")

    val git = Grgit.open(mapOf("dir" to file("../../")))
    val branchName = git.branch.current().name.replace("/", "-")
    val hash = git.head().getAbbreviatedId(10)

    tags.add("${dockerRegistry}translation-ms-server-${project.version}")
    tags.add("${dockerRegistry}translation-ms-server-$branchName")
    tags.add("${dockerRegistry}translation-ms-server-$hash")
}

getDockerBuildImageTask().tags.get().forEach {
    tasks.create("pushDockerTag_${it.replace("[/\\\\:<>\"\\?\\*\\|]", "-")}", DockerPushImage::class) {
        dependsOn(tasks.withType(DockerBuildImage::class))
        imageName.set(it)
    }
}

tasks.create("pushDockerTags") {
    dependsOn(tasks.withType(DockerPushImage::class))
}

//endregion

//region remove docker tags

tasks.register<DockerRemoveImage>("removeDockerImage") {
    force.set(true)
    targetImageId(getDockerBuildImageTask().imageId)
}

//endregion

if (project.hasProperty("removeImage")) {
    tasks.getByName("pushDockerTags").finalizedBy(tasks.getByName("removeDockerImage", DockerRemoveImage::class))
}

fun getDockerBuildImageTask() = tasks.getByName("buildDockerImage", DockerBuildImage::class)