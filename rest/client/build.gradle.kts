import org.ajoberstar.grgit.Grgit

group = "${ext["platformGroup"]!!}.rest.client"
version = ext["platformVersion"]!!

val currentGroup = group as String

apply(plugin = "io.spring.dependency-management")

plugins {
    maven
}

dependencies {
    implementation(project(":rest:common")) {
        exclude(module = "core")
        exclude("org.springframework.boot", "spring-boot-starter-web")
    }

    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.glassfish.jersey.core:jersey-client")
    implementation("org.glassfish.jersey.inject:jersey-hk2")
    implementation("org.glassfish.jersey.media:jersey-media-json-jackson")
    implementation("org.glassfish.jersey.media:jersey-media-multipart")

    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:Greenwich.RELEASE")
    }
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

                pom {
                    groupId = currentGroup
                    artifactId = "translation-ms-client"
                    version = environmentPlatformVersion()
                }
            }
        }
    }
}

fun environmentPlatformVersion(): String = when (Grgit.open(mapOf("dir" to file("../../"))).branch.current().name) {
    "development" -> "$version-SNAPSHOT"
    "acceptance" -> "$version-acceptance-SNAPSHOT"
    "master" -> "$version"
    else -> "$version"
}