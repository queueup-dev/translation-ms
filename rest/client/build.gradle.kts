apply(plugin = "io.spring.dependency-management")

dependencies {
    implementation(project(":rest:common")) {
        exclude(module = "core")
        exclude("org.springframework.boot", "spring-boot-starter-web")
    }

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.cloud:spring-cloud-starter-feign:1.4.6.RELEASE")

    implementation("io.github.openfeign:feign-okhttp:10.1.0")
    implementation("io.github.openfeign:feign-jackson:10.1.0")
    implementation("io.github.openfeign:feign-gson:10.1.0")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:Greenwich.RELEASE")
    }
}