repositories {
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
    `maven-publish`
    signing
    id("org.jetbrains.dokka") version "1.5.31"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.charleskorn.kaml:kaml:0.37.0")

    api("org.jetbrains.kotlinx:kotlinx-datetime:0.3.1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testImplementation("org.assertj:assertj-core:3.24.2")
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
        val url = if (project.version.toString().contains("SNAPSHOT")) "https://oss.sonatype.org/content/repositories/snapshots" else "https://oss.sonatype.org/service/local/staging/deploy/maven2"
        maven(url) {
            credentials {
                username = project.findProperty("ossrh.username")?.toString() ?: ""
                password = project.findProperty("ossrh.password")?.toString() ?: ""
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(project.findProperty("signingKey").toString(), project.findProperty("signingPassword").toString())
    sign(publishing.publications["maven"])
}