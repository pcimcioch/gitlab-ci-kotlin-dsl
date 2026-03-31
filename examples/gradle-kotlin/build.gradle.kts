import org.gradle.kotlin.dsl.application
import org.gradle.kotlin.dsl.kotlin

repositories {
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.9.20"
    application
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.github.pcimcioch:gitlab-ci-kotlin-dsl:1.8.0")
}

application {
    mainClass = "com.example.MainKt"
}