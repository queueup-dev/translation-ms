import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.sfl.qup.tms"
version = "0.0.1-SNAPSHOT"

plugins {
    val kotlinVersion = "1.3.10"
    val springBootVersion = "2.1.1.RELEASE"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("plugin.allopen") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion

    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
}

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
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.10")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("org.hibernate:hibernate-java8:")
    implementation("org.postgresql:postgresql:42.2.5")

    implementation("org.apache.commons:commons-lang3:")

    implementation("io.springfox:springfox-swagger2:2.9.2")
    implementation("io.springfox:springfox-swagger-ui:2.9.2")
}

configure<JavaPluginConvention> {
    setSourceCompatibility(1.8)
    setTargetCompatibility(1.8)
}

springBoot {
    mainClassName = "com.sfl.qup.tms.tms.TmsApplication"
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

repositories {
    maven("https://plugins.gradle.org/m2/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    mavenLocal()
    mavenCentral()
    jcenter()
}