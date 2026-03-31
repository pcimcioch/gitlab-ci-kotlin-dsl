repositories {
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.9.20"
    application
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.github.pcimcioch:gitlab-ci-kotlin-dsl:1.7.0")
}

application {
    mainClass = "com.example.MainKt"
}