import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar
import se.transmode.gradle.plugins.docker.DockerTask

group = "com.sfl.tms"
version = "0.0.1-SNAPSHOT"

//region Plugins

plugins {
    val kotlinVersion = "1.3.11"
    val springBootVersion = "2.1.1.RELEASE"

    kotlin("jvm") version kotlinVersion apply true
    kotlin("plugin.jpa") version kotlinVersion apply true
    kotlin("plugin.allopen") version kotlinVersion apply true
    kotlin("plugin.spring") version kotlinVersion apply true

    id("org.springframework.boot") version springBootVersion apply true

    id("org.sonarqube") version "2.6.2" apply true

    jacoco apply true
}

apply {
    plugin("docker")
    plugin("io.spring.dependency-management")
}

//endregion

//region Build script

buildscript {
    repositories {
        maven("https://plugins.gradle.org/m2/")
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        mavenLocal()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.1.1.RELEASE")
        classpath("se.transmode.gradle:gradle-docker:1.2")
    }
}

//endregion

//region Dependencies

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.10")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("org.hibernate:hibernate-java8:")
    implementation("org.postgresql:postgresql:42.2.5")

    implementation("org.apache.commons:commons-lang3:")
    implementation("com.google.code.gson:gson:2.8.5")

    implementation("io.springfox:springfox-swagger2:2.9.2")
    implementation("io.springfox:springfox-swagger-ui:2.9.2")

    implementation("net.logstash.logback:logstash-logback-encoder:5.2")
}

//endregion

//region SpringBoot

springBoot {
    mainClassName = "com.sfl.tms.TmsApplication"

    buildInfo {
        properties {
            name = project.name
            group = project.group.toString()
            version = project.version.toString()
            artifact = "translation-ms"
        }
    }
}

//endregion

//region Tasks

configure<JavaPluginConvention> {
    setSourceCompatibility(1.8)
    setTargetCompatibility(1.8)
}

tasks.withType<JavaCompile> {
    options.isFork = true
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

tasks.getByName<BootJar>("bootJar") {
    mainClassName = "com.sfl.tms.TmsApplication"
    launchScript()
}

tasks.register<DockerTask>("buildDockerWithLatestTag") {
    val registryUrl = if (project.hasProperty("dockerRegistryUrl")) project.properties["dockerRegistryUrl"] else ""
    val projectEnvironment = if (project.hasProperty("environment")) project.properties["environment"] else ""

    tagVersion = "latest"
    push = true
    applicationName = "translation-ms-$projectEnvironment"
    registry = registryUrl as String?

    runCommand("wget https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip -P /tmp")
    runCommand("unzip /tmp/newrelic-java.zip -d /opt/newrelic")

    addFile("${System.getProperty("user.dir")}/config/newrelic/newrelic.yml", "/opt/newrelic/newrelic.yml")
    addFile("${System.getProperty("user.dir")}/build/libs/translation-ms-$version.jar", "/opt/jar/translation-ms.jar")

    runCommand("touch /opt/jar/translation-ms.jar")

    exposePort(8080)

    entryPoint(arrayOf("java", " -javaagent:/opt/newrelic/newrelic.jar -Dnewrelic.environment=$projectEnvironment \$JAVA_OPTS -jar /opt/jar/translation-ms.jar").toMutableList())

    dockerfile = file("Dockerfile")
}

//endregion

//region Sonar

jacoco {
    toolVersion = "0.8.0"
    reportsDir = file("$project.rootDir/build/reports")
}

sonarqube {
    properties {
        property("sonar.projectName", "Translations Microservice")
        property("sonar.projectKey", "com.sfl.tms")
        property("sonar.exclusions", "**/exception/**")
        property("sonar.exclusions", "**/dto/**")
        property("sonar.exclusions", "**/configuration/**")
        property("sonar.exclusions", "**/boot/**")
        property("sonar.exclusions", "**/aspect/**")
        property("sonar.jacoco.reportPaths", "$buildDir/jacoco/test.exec")
    }
}

//endregion

//region Repositories

repositories {
    maven("https://plugins.gradle.org/m2/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    mavenLocal()
    mavenCentral()
    jcenter()
}

//endregion