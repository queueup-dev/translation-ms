plugins {
    maven
    `maven-publish`

    id("io.spring.dependency-management")
}

dependencies {
    compile(project(":core"))

    compile(kotlin("stdlib", version = "1.3.21"))
    compile(kotlin("reflect", version = "1.3.21"))

    compile("org.springframework.boot:spring-boot-starter-web")
    compile("org.springframework.boot:spring-boot-starter-aop")
    compile("org.springframework.boot:spring-boot-configuration-processor")

    compile("com.fasterxml.jackson.module:jackson-module-kotlin")

    compile("io.springfox:springfox-swagger2:2.9.2")

    compile("org.apache.commons:commons-lang3:")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:2.1.2.RELEASE")
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
            url = if (project.version.toString().endsWith("-SNAPSHOT")) uri("https://nexus.ci.funtrips.io/repository/maven-snapshots/") else uri("https://nexus.ci.funtrips.io/repository/maven-releases/")
            credentials {
                username = System.getenv("SONATYPE_USERNAME")
                password = System.getenv("SONATYPE_PASSWORD")
            }
        }
    }
}