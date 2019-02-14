import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.tasks.bundling.BootJar

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

val dockerRegistryUrl = "https://${project.findProperty("dockerRegistry") ?: ""}"

docker {
    registryCredentials {
        url.set(dockerRegistryUrl)
        username.set(System.getenv("DOCKER_REGISTRY_USERNAME"))
        password.set(System.getenv("DOCKER_REGISTRY_PASSWORD"))
    }
}

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

    tags.add("$dockerRegistryUrl/translation-ms-server:$project.version")
    tags.add("$dockerRegistryUrl/translation-ms-server:$project.version")
    tags.add("$dockerRegistryUrl/translation-ms-server:$project.version")
}