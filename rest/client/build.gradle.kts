plugins {
    maven
    `maven-publish`

    id("io.spring.dependency-management")
}

tasks.getByName<Upload>("uploadArchives") {
    repositories {
        withConvention(MavenRepositoryHandlerConvention::class) {
            mavenDeployer {
                withGroovyBuilder {
                    "repository"("url" to uri("https://nexus.ci.funtrips.io/repository/maven-releases/")) {
                        "authentication"("userName" to System.getenv("SONATYPE_USERNAME"), "password" to System.getenv("SONATYPE_PASSWORD"))
                    }
                    "snapshotRepository"("url" to uri("https://nexus.ci.funtrips.io/repository/maven-snapshots/")) {
                        "authentication"("userName" to System.getenv("SONATYPE_USERNAME"), "password" to System.getenv("SONATYPE_PASSWORD"))
                    }
                }
            }
        }
    }
}

dependencies {
    compile(project(":rest:common")) {
        exclude(module = "core")
        exclude("org.springframework.boot", "spring-boot-starter-web")
    }

    compile(kotlin("stdlib", version = "1.3.21"))
    compile(kotlin("reflect", version = "1.3.21"))

    compile("org.springframework.boot:spring-boot-starter-actuator")

    compile("org.glassfish.jersey.core:jersey-client")
    compile("org.glassfish.jersey.inject:jersey-hk2")
    compile("org.glassfish.jersey.media:jersey-media-json-jackson")
    compile("org.glassfish.jersey.media:jersey-media-multipart")

    compile("com.fasterxml.jackson.core:jackson-databind")
    compile("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:2.1.2.RELEASE")
    }
}