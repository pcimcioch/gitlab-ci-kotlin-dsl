repositories {
    mavenCentral()
    maven(url = "https://kotlin.bintray.com/kotlinx/") // remove when kotlinx-datetime moved to jcenter
}

plugins {
    kotlin("jvm") version "1.3.72"
    application
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.github.pcimcioch:gitlab-ci-kotlin-dsl:1.2.0")
}

application {
    mainClassName = "com.example.MainKt"
}