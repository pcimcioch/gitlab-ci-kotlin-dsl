import pcimcioch.gitlabci.dsl.Duration
import pcimcioch.gitlabci.dsl.gitlabCi
import pcimcioch.gitlabci.dsl.job.WhenUploadType

buildscript {
    repositories {
        mavenCentral()
        maven(url = "https://kotlin.bintray.com/kotlinx/") // remove when kotlinx-datetime moved to jcenter
    }

    dependencies {
        classpath("com.github.pcimcioch:gitlab-ci-kotlin-dsl:1.2.0")
    }
}

tasks.register("generateGitlabCi") {
    doLast {
        gitlabCi {
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
                    whenUpload = WhenUploadType.ALWAYS
                    paths("build/test-results", "build/reports")
                    expireIn = Duration(days = 7)
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
                    whenUpload = WhenUploadType.ON_SUCCESS
                    paths("build/libs")
                    expireIn = Duration(days = 7)
                }
            }
        }
    }
}