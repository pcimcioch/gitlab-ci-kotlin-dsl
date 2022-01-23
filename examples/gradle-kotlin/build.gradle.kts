repositories {
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.5.31"
    application
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.github.pcimcioch:gitlab-ci-kotlin-dsl:1.3.0")
}

application {
    mainClassName = "com.example.MainKt"
}