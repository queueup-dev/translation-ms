plugins {
    // Kotlin
    kotlin("jvm")            version "1.9.20"
    kotlin("plugin.jpa")     version "1.9.20"
    kotlin("plugin.allopen") version "1.9.20"
    kotlin("plugin.noarg")   version "1.9.20"

    // Spring Dependency Management
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
    implementation(project(":core"))

    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-configuration-processor")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("io.springfox:springfox-swagger2:2.9.2")

    implementation("org.apache.commons:commons-lang3:3.12.0")
}

dependencyManagement {
    imports {
        // Align with Spring Boot 3.4.6
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

