import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar
import se.transmode.gradle.plugins.docker.DockerTask

group = "com.sfl.tms"
version = "0.0.1-SNAPSHOT"

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

//region Plugins

plugins {
    val kotlinVersion = "1.3.11"
    val springBootVersion = "2.1.2.RELEASE"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("plugin.allopen") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion

    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version "1.0.6.RELEASE"

    id("org.sonarqube") version "2.6.2" apply true

    jacoco apply true
}

apply {
    plugin("docker")
}

//endregion

//region Projects

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")

    apply(plugin = "io.spring.dependency-management")

    dependencies {
        compile(kotlin("stdlib-jdk8"))
        compile(kotlin("reflect"))
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:2.1.2.RELEASE")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:Greenwich.RELEASE")
        }
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    tasks.create<Delete>("delete") {
        delete {
            files("out")
        }
    }

    repositories {
        mavenCentral()
    }
}

//endregion

//region Tasks

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.register<DockerTask>("buildDockerWithLatestTag") {
    val registryUrl = if (project.hasProperty("dockerRegistryUrl")) project.properties["dockerRegistryUrl"] else ""
    val projectEnvironment = if (project.hasProperty("environment")) project.properties["environment"] else ""

    tagVersion = "latest"
    push = true
    applicationName = "translation-ms-$projectEnvironment"
    registry = registryUrl as String?

    addFile("${System.getProperty("user.dir")}/config/newrelic/newrelic.yml", "/opt/newrelic/newrelic.yml")

    runCommand("wget https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip -P /tmp")
    runCommand("unzip /tmp/newrelic-java.zip -d /opt/newrelic")

    addFile("${System.getProperty("user.dir")}/rest/server/build/libs/server.jar", "/opt/jar/translation-ms.jar")

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