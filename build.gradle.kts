//region Build script
plugins {
    // Kotlin
    kotlin("jvm")             version "1.9.20"
    kotlin("plugin.jpa")      version "1.9.20"
    kotlin("plugin.allopen")  version "1.9.20"
    kotlin("plugin.noarg")    version "1.9.20"

    // Spring Boot + dependency-management
    id("org.springframework.boot") version "3.4.6"
    id("io.spring.dependency-management") version "1.1.2"

    // GrGit plugin
    id("org.ajoberstar.grgit")  version "5.3.0"
}

//endregion

allprojects {
    group = "com.sfl.tms"
    version = "0.1.7-SNAPSHOT"

    repositories {
        mavenCentral()
        mavenLocal()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
    apply(plugin = "org.jetbrains.kotlin.plugin.noarg")
    apply(plugin = "io.spring.dependency-management")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}