import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

ext {
    this["platformGroup"] = "com.sfl.tms"
    this["platformVersion"] = "0.0.1-SNAPSHOT"
}

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
        classpath("org.ajoberstar:grgit:2.2.1")
    }
}

//endregion

//region Plugins

plugins {
    val kotlinVersion = "1.3.20"
    val springBootVersion = "2.1.2.RELEASE"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("plugin.allopen") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion

    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version "1.0.6.RELEASE"

    id("org.sonarqube") version "2.6.2"

    jacoco apply true
}

//endregion

//region Projects

allprojects {
    group = "com.sfl.tms"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }

    ext {
        this["platformGroup"] = "com.sfl.tms"
        this["platformVersion"] = "0.0.1-SNAPSHOT"
    }
}

subprojects {
    group = "com.sfl.tms"
    version = "0.0.1-SNAPSHOT"

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

/*
    configure<MavenPublishAction> {
        repositories {
            maven {
                name = "releases"
                url = uri("https://nexus.ci.funtrips.io/repository/maven-releases/")
                credentials {
                    username = System.getenv("SONATYPE_USERNAME")
                    password = System.getenv("SONATYPE_PASSWORD")
                }
            }

            maven {
                name = "snapshots"
                url = uri("https://nexus.ci.funtrips.io/repository/maven-snapshots/")
                credentials {
                    username = System.getenv("SONATYPE_USERNAME")
                    password = System.getenv("SONATYPE_PASSWORD")
                }
            }
        }
    }
*/

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }
}

//endregion

//region Tasks

tasks.getByName<BootJar>("bootJar") {
    enabled = false
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