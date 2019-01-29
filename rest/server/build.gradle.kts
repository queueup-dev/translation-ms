import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.tasks.bundling.BootJar

apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")

dependencies {
    implementation(project(":core"))
    implementation(project(":rest:common"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("io.springfox:springfox-swagger-ui:2.9.2")

    implementation("com.google.code.gson:gson:2.8.5")
}

the<DependencyManagementExtension>().apply {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

tasks.getByName<BootJar>("bootJar") {
    mainClassName = "com.sfl.tms.ServerApplication"
    manifest {
        attributes("Start-Class" to "com.sfl.tms.ServerApplication")
    }
    launchScript()
}