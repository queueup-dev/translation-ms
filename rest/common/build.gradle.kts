dependencies {
    compile("org.springframework.boot:spring-boot-starter-web")
    compile("org.springframework.boot:spring-boot-starter-aop")
    compile("org.springframework.boot:spring-boot-configuration-processor")

    compile("com.fasterxml.jackson.module:jackson-module-kotlin")

    compile("io.springfox:springfox-swagger2:2.9.2")

    implementation("org.apache.commons:commons-lang3:")
    implementation(project(":core"))
}