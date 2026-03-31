import org.jreleaser.model.Active.ALWAYS

repositories {
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.serialization") version "1.9.20"
    `maven-publish`
    id("org.jreleaser") version "1.23.0"
    id("org.jetbrains.dokka") version "1.9.20"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.charleskorn.kaml:kaml:0.55.0")

    api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testImplementation("org.assertj:assertj-core:3.27.7")
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
    jreleaserDeploy {
        dependsOn(publish)
    }
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc)
}

java {
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(dokkaJar)

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
                developers {
                    developer {
                        id.set("pcimcioch")
                        name.set("Przemysław Cimcioch")
                        email.set("cimcioch.przemyslaw@gmail.com")
                    }
                }
            }
        }
    }

    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("staging-deploy"))
        }
    }
}

jreleaser {
    signing {
        pgp {
            active = ALWAYS
            armored = true
        }
        deploy {
            maven {
                mavenCentral {
                    create("sonatype") {
                        active = ALWAYS
                        url = "https://central.sonatype.com/api/v1/publisher"
                        stagingRepository("build/staging-deploy")
                    }
                }
            }
        }
    }
}