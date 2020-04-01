import pcimcioch.gitlabci.dsl.gitlabCi

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("com.github.pcimcioch:gitlab-ci-kotlin-dsl:1.0.0")
    }
}

tasks.register("generateGitlabCi") {
    doLast {
        java.io.FileWriter(".gitlab-ci-generated.yml").use {
            gitlabCi(writer = it) {
                default {
                    image("openjdk:8u162")

                    cache("./gradle/wrapper", "./gradle/caches")

                    beforeScript("export GRADLE_USER_HOME=\$(pwd)/.gradle")
                }

                stages {
                    +"test"
                    +"release"
                }

                job("build") {
                    stage = "test"
                    script("./gradlew clean build")
                    artifacts {
                        whenUpload = pcimcioch.gitlabci.dsl.job.WhenUploadType.ALWAYS
                        paths("build/test-results", "build/reports")
                        expireIn = pcimcioch.gitlabci.dsl.Duration(days = 7)
                        reports {
                            junit("build/test-results/test/TEST-*.xml")
                        }
                    }
                }

                job("release") {
                    stage = "release"
                    script("./gradlew publishToMavenLocal")
                    only {
                        master()
                    }
                    artifacts {
                        whenUpload = pcimcioch.gitlabci.dsl.job.WhenUploadType.ON_SUCCESS
                        paths("build/libs")
                        expireIn = pcimcioch.gitlabci.dsl.Duration(days = 7)
                    }
                }
            }
        }
    }
}