//region Build script

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.1.1.RELEASE")
        classpath("org.ajoberstar:grgit:2.2.1")
        classpath(kotlin("gradle-plugin", version = "1.3.21"))
        classpath(kotlin("allopen", version = "1.3.21"))
        classpath(kotlin("noarg", version = "1.3.21"))
    }
}

//endregion

allprojects {
    group = "com.sfl.tms"
    version = "0.0.6-SNAPSHOT"
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")

    apply(plugin = "io.spring.dependency-management")

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }
}
