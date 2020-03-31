repositories {
    // TODO after release remove local repo
    mavenLocal()
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.3.70"
    application
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    // TODO after release use proper version
    implementation("com.github.pcimcioch:gitlab-ci-kotlin-dsl:1-SNAPSHOT")
}

application {
    mainClassName = "com.example.MainKt"
}