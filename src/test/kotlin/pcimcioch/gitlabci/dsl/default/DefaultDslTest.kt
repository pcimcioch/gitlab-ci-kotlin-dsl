package pcimcioch.gitlabci.dsl.default

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import pcimcioch.gitlabci.dsl.DslTestBase
import pcimcioch.gitlabci.dsl.gitlabCi
import pcimcioch.gitlabci.dsl.job.createAfterScript
import pcimcioch.gitlabci.dsl.job.createBeforeScript
import pcimcioch.gitlabci.dsl.job.createCache
import pcimcioch.gitlabci.dsl.job.createImage
import pcimcioch.gitlabci.dsl.job.createServices
import java.io.StringWriter

internal class DefaultDslTest : DslTestBase() {

    private val writer = StringWriter()

    @Test
    fun `should validate nested objects`() {
        // when
        val thrown = assertThrows<IllegalArgumentException> {
            gitlabCi(writer = writer) {
                default {
                    beforeScript("before 1", "before 2")
                    afterScript("after 1", "after 2")

                    image("")
                    services("service 1", "")
                    cache {
                        paths("path")
                        key {
                            prefix = "pre/fix"
                            files("file")
                        }
                    }
                }
            }
        }

        // then
        assertThat(thrown).hasMessage("""
            Configuration validation failed
            [default][image] name '' is incorrect
            [default][service name=''] name '' is incorrect
            [default][cache][key] prefix value 'pre/fix' can't contain '/' nor '%2F'
            Validation can be disabled by calling 'gitlabCi(validate = false) {}'""".trimIndent())
        assertThat(writer.toString()).isEmpty()
    }

    @Test
    fun `should create empty default`() {
        // given
        gitlabCi(writer = writer) {
            default {}
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "default": {}
                """.trimIndent()
        )
    }

    @Test
    fun `should create full default`() {
        // given
        gitlabCi(writer = writer) {
            default {
                image("testImage")
                services("testService")
                cache("testCache")
                beforeScript("before")
                afterScript("after")
            }
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "default":
                      image:
                        name: "testImage"
                      services:
                      - name: "testService"
                      cache:
                        paths:
                        - "testCache"
                      before_script:
                      - "before"
                      after_script:
                      - "after"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow different beforeScript options`() {
        // given
        gitlabCi(writer = writer) {
            default {
                beforeScript {
                    +"command 1"
                }
                beforeScript("command 2")
                beforeScript(listOf("command 3"))
            }
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "default":
                      before_script:
                      - "command 1"
                      - "command 2"
                      - "command 3"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow different afterScript options`() {
        // given
        gitlabCi(writer = writer) {
            default {
                afterScript {
                    +"command 1"
                }
                afterScript("command 2")
                afterScript(listOf("command 3"))
            }
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "default":
                      after_script:
                      - "command 1"
                      - "command 2"
                      - "command 3"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow image by name configuration `() {
        // given
        gitlabCi(writer = writer) {
            default {
                image("image:1")
            }
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "default":
                      image:
                        name: "image:1"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow image by block configuration `() {
        // given
        gitlabCi(writer = writer) {
            default {
                image {
                    name = "image:1"
                }
            }
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "default":
                      image:
                        name: "image:1"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow image by name and block configuration `() {
        // given
        gitlabCi(writer = writer) {
            default {
                image("image:1") {
                    entrypoint("entry", "point")
                }
            }
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "default":
                      image:
                        name: "image:1"
                        entrypoint:
                        - "entry"
                        - "point"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow different services options`() {
        // given
        gitlabCi(writer = writer) {
            default {
                services("ser1")
                services(listOf("ser2"))
                services {
                    service("ser3")
                }
            }
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "default":
                      services:
                      - name: "ser1"
                      - name: "ser2"
                      - name: "ser3"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow different cache options`() {
        // given
        gitlabCi(writer = writer) {
            default {
                cache {
                    paths("p1")
                }
                cache("p2")
                cache(listOf("p3"))
            }
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "default":
                      cache:
                        paths:
                        - "p1"
                        - "p2"
                        - "p3"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val imageDsl = createImage("testImage")
        val servicesDsl = createServices("testService")
        val cacheDsl = createCache("testCache")
        val beforeScriptDsl = createBeforeScript("before")
        val afterScriptDsl = createAfterScript("after")

        gitlabCi(writer = writer) {
            default {
                image = imageDsl
                services = servicesDsl
                cache = cacheDsl
                beforeScript = beforeScriptDsl
                afterScript = afterScriptDsl
            }
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "default":
                      image:
                        name: "testImage"
                      services:
                      - name: "testService"
                      cache:
                        paths:
                        - "testCache"
                      before_script:
                      - "before"
                      after_script:
                      - "after"
                """.trimIndent()
        )
    }
}