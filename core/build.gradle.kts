dependencies {
    compile("org.springframework.boot:spring-boot-starter-data-jpa")
    compile("org.springframework.boot:spring-boot-configuration-processor")

    compile("org.hibernate:hibernate-java8:")
    compile("org.postgresql:postgresql:42.2.5")

    compile("org.apache.commons:commons-lang3:")

    testCompile("org.springframework.boot:spring-boot-starter-test")
    implementation("com.nhaarman:mockito-kotlin:1.6.0")

    compile("net.logstash.logback:logstash-logback-encoder:5.2")
}