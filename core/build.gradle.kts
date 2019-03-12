plugins {
    maven
    `maven-publish`

    id("io.spring.dependency-management")
}

dependencies {
    compile(kotlin("stdlib", version = "1.3.21"))
    compile(kotlin("reflect", version = "1.3.21"))

    compile("org.springframework.boot:spring-boot-starter-data-jpa")
    compile("org.springframework.boot:spring-boot-configuration-processor")

    compile("org.hibernate:hibernate-java8:")
    compile("org.postgresql:postgresql:42.2.5")

    compile("org.apache.commons:commons-lang3:")

    testCompile("org.springframework.boot:spring-boot-starter-test")
    implementation("com.nhaarman:mockito-kotlin:1.6.0")

    compile("net.logstash.logback:logstash-logback-encoder:5.2")
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
/*
        maven {
            name = "releases"
            url = uri("https://nexus.ci.funtrips.io/repository/maven-releases/")
            credentials {
                username = System.getenv("SONATYPE_USERNAME")
                password = System.getenv("SONATYPE_PASSWORD")
            }
        }
*/

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