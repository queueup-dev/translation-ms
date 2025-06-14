plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.jpa") version "1.9.20"
    kotlin("plugin.noarg") version "1.9.20"
    kotlin("plugin.allopen") version "1.9.20"

    id("io.spring.dependency-management") version "1.1.2"
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
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    implementation("org.hibernate:hibernate-core:6.2.7.Final")
    implementation("org.postgresql:postgresql:42.2.5")

    implementation("org.apache.commons:commons-lang3:3.12.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.nhaarman:mockito-kotlin:1.6.0")

    implementation("net.logstash.logback:logstash-logback-encoder:5.2")
}

dependencyManagement {
    imports {
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
            url = uri("https://queueup-java-085415868203.d.codeartifact.eu-west-1.amazonaws.com/maven/qup-core/")
            credentials {
                username = "aws"
                password = System.getenv("CODEARTIFACT_AUTH_TOKEN")
            }
        }
    }
}
tasks.named<Jar>("jar") {
    enabled = true
}
tasks.withType<Jar> {
    exclude("META-INF/persistence.xml")
}
