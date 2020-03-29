repositories {
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.3.70"
    kotlin("plugin.serialization") version "1.3.70"
    `maven-publish`
    signing
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")

    implementation("com.charleskorn.kaml:kaml:0.16.1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
    testImplementation("org.assertj:assertj-core:3.15.0")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    test {
        useJUnitPlatform()
    }
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                name.set("GitlabCi Kotlin DSL")
                description.set("Library providing Kotlin DSL to configure GitlabCI file")
                url.set("https://github.com/pcimcioch/gitlab-ci-kotlin-dsl")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/pcimcioch/gitlab-ci-kotlin-dsl.git")
                    developerConnection.set("scm:git:git@github.com:pcimcioch/gitlab-ci-kotlin-dsl.git")
                    url.set("https://github.com/pcimcioch/gitlab-ci-kotlin-dsl")
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}