plugins {
    // Kotlin
    kotlin("jvm")            version "1.9.20"
    kotlin("plugin.jpa")     version "1.9.20"
    kotlin("plugin.allopen") version "1.9.20"
    kotlin("plugin.noarg")   version "1.9.20"

    // Spring’s BOM support
    id("io.spring.dependency-management") version "1.1.2"

    // Publishing
    `maven-publish`
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(project(":rest:common")) {
        exclude(module = "core")
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-web")
    }

    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.glassfish.jersey.core:jersey-client:3.1.2")
    implementation("org.glassfish.jersey.inject:jersey-hk2:3.1.2")
    implementation("org.glassfish.jersey.media:jersey-media-multipart:3.1.2")
    implementation("org.glassfish.jersey.media:jersey-media-json-jackson:3.1.2")

    implementation("jakarta.ws.rs:jakarta.ws.rs-api:3.1.0")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}

dependencyManagement {
    imports {
        // align with Spring Boot plugin version
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.6")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "snapshots"
            url = uri(
                "https://queueup-java-085415868203.d.codeartifact.eu-west-1.amazonaws.com/maven/qup-core/"
            )
            credentials {
                username = "aws"
                password = System.getenv("CODEARTIFACT_AUTH_TOKEN")
            }
        }
    }
}