package pcimcioch.gitlabci.dsl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import pcimcioch.gitlabci.dsl.job.createJob
import java.io.StringWriter

internal class GitlabCiDslTest : DslTestBase() {

    private val writer = StringWriter()

    @Test
    fun `should validate`() {
        // when
        val thrown = assertThrows<IllegalArgumentException> {
            gitlabCi(writer = writer) {
                stages("build", "test", "release")

                default {
                    image("")
                }

                job("image") {
                    script("test")
                }
                job("cache") {
                    script("cache")
                }
            }
        }

        // then
        assertThat(thrown).hasMessage("""
            Configuration validation failed
            [default][image] name '' is incorrect
            [job name='image'] name 'image' is incorrect
            [job name='cache'] name 'cache' is incorrect
            Validation can be disabled by calling 'gitlabCi(validate = false) {}'""".trimIndent())
        assertThat(writer.toString()).isEmpty()
    }

    @Test
    fun `should disable validation`() {
        // when
        gitlabCi(validate = false, writer = writer) {
            stages("build", "test", "release")

            default {
                image("")
            }

            job("image") {
                script("test")
            }
            job("cache") {
                script("cache")
            }
        }

        // then
        assertThat(writer.toString()).isEqualTo("""
            "stages":
            - "build"
            - "test"
            - "release"
            "default":
              image:
                name: ""
            "image":
              script:
              - "test"
            "cache":
              script:
              - "cache"
        """.trimIndent())
    }

    @Test
    fun `should create stages from vararg`() {
        // when
        gitlabCi(writer = writer) {
            stages("build", "test", "release")
        }

        // then
        assertThat(writer.toString()).isEqualTo("""
            "stages":
            - "build"
            - "test"
            - "release"
        """.trimIndent())
    }

    @Test
    fun `should create stages from list`() {
        // when
        gitlabCi(writer = writer) {
            stages(listOf("build", "test", "release"))
        }

        // then
        assertThat(writer.toString()).isEqualTo("""
            "stages":
            - "build"
            - "test"
            - "release"
        """.trimIndent())
    }

    @Test
    fun `should create stages from block`() {
        // when
        gitlabCi(writer = writer) {
            stages {
                stage("build")
                stage("test")
                stage("release")
            }
        }

        // then
        assertThat(writer.toString()).isEqualTo("""
            "stages":
            - "build"
            - "test"
            - "release"
        """.trimIndent())
    }

    @Test
    fun `should create job from name`() {
        // when
        gitlabCi(validate = false, writer = writer) {
            job("test") {}
        }

        // then
        assertThat(writer.toString()).isEqualTo("""
            "test": {}
        """.trimIndent())
    }

    @Test
    fun `should create job from name and block`() {
        // when
        gitlabCi(validate = false, writer = writer) {
            job("test") {
                script("command")
            }
        }

        // then
        assertThat(writer.toString()).isEqualTo("""
            "test":
              script:
              - "command"
        """.trimIndent())
    }

    @Test
    fun `should add job with plus`() {
        // when
        val job = createJob("test") {
            script("command")
        }
        gitlabCi(validate = false, writer = writer) {
            +job
        }

        // then
        assertThat(writer.toString()).isEqualTo("""
            "test":
              script:
              - "command"
        """.trimIndent())
    }

    @Test
    fun `should generate empty`() {
        // when
        gitlabCi(validate = false, writer = writer) {}

        // then
        assertThat(writer.toString()).isEqualTo("""
            {}
        """.trimIndent())
    }

    @Test
    fun `should generate full`() {
        // when
        gitlabCi(validate = false, writer = writer) {
            stages("build", "test", "release")

            default {
                beforeScript("before command")
                afterScript("after command")
            }

            job("build app") {
                script("build command")
                stage = "build"
            }

            job("test app") {
                script("test command")
                stage = "test"
            }

            val release1 = job("release app 1") {
                script("release command 1")
                stage = "release"
            }

            job("release app 2") {
                script("release command 2")
                stage = "release"
                dependencies(release1)
            }
        }

        // then
        assertThat(writer.toString()).isEqualTo("""
            "stages":
            - "build"
            - "test"
            - "release"
            "default":
              before_script:
              - "before command"
              after_script:
              - "after command"
            "build app":
              stage: "build"
              script:
              - "build command"
            "test app":
              stage: "test"
              script:
              - "test command"
            "release app 1":
              stage: "release"
              script:
              - "release command 1"
            "release app 2":
              stage: "release"
              dependencies:
              - "release app 1"
              script:
              - "release command 2"
        """.trimIndent())
    }

    @Test
    fun `should create pages job`() {
        // when
        gitlabCi(validate = false, writer = writer) {
            pages {
                script("command")
            }
        }

        // then
        assertThat(writer.toString()).isEqualTo("""
            "pages":
              artifacts:
                paths:
                - "public"
              only:
                refs:
                - "master"
              script:
              - "command"
        """.trimIndent())
    }
}