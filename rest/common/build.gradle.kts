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

    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")

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
            url = uri("https://queueup-java-085415868203.d.codeartifact.eu-west-1.amazonaws.com/maven/qup-core/")
            credentials {
                username = "aws"
                password = System.getenv("CODEARTIFACT_AUTH_TOKEN")
            }
        }
    }
}