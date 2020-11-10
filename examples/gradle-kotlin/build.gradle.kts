repositories {
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.3.70"
    application
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.github.pcimcioch:gitlab-ci-kotlin-dsl:1.1.0")
}

application {
    mainClassName = "com.example.MainKt"
}