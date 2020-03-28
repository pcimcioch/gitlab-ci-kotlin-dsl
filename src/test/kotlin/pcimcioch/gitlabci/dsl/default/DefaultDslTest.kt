package pcimcioch.gitlabci.dsl.default

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase
import pcimcioch.gitlabci.dsl.job.createAfterScript
import pcimcioch.gitlabci.dsl.job.createBeforeScript
import pcimcioch.gitlabci.dsl.job.createCache
import pcimcioch.gitlabci.dsl.job.createImage
import pcimcioch.gitlabci.dsl.job.createServices

internal class DefaultDslTest : DslTestBase() {

    @Test
    fun `should validate nested objects`() {
        // when
        val testee = DefaultDsl().apply {
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

        // then
        assertDsl(DefaultDsl.serializer(), testee,
                """
                    image:
                      name: ""
                    services:
                    - name: "service 1"
                    - name: ""
                    cache:
                      paths:
                      - "path"
                      key:
                        prefix: "pre/fix"
                        files:
                        - "file"
                    before_script:
                    - "before 1"
                    - "before 2"
                    after_script:
                    - "after 1"
                    - "after 2"
                """.trimIndent(),
                "[default][image] name '' is incorrect",
                "[default][service name=''] name '' is incorrect",
                "[default][cache][key] prefix value 'pre/fix' can't contain '/' nor '%2F'"
        )
    }

    @Test
    fun `should create empty default`() {
        // given
        val testee = DefaultDsl()

        // then
        assertDsl(DefaultDsl.serializer(), testee,
                """
                    {}
                """.trimIndent()
        )
    }

    @Test
    fun `should create full default`() {
        // given
        val testee = DefaultDsl().apply {
            image("testImage")
            services("testService")
            cache("testCache")
            beforeScript("before")
            afterScript("after")
        }

        // then
        assertDsl(DefaultDsl.serializer(), testee,
                """
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
        val testee = DefaultDsl().apply {
            beforeScript {
                +"command 1"
            }
            beforeScript("command 2")
            beforeScript(listOf("command 3"))
        }

        // then
        assertDsl(DefaultDsl.serializer(), testee,
                """
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
        val testee = DefaultDsl().apply {
            afterScript {
                +"command 1"
            }
            afterScript("command 2")
            afterScript(listOf("command 3"))
        }

        // then
        assertDsl(DefaultDsl.serializer(), testee,
                """
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
        val testee = DefaultDsl().apply {
            image("image:1")
        }

        // then
        assertDsl(DefaultDsl.serializer(), testee,
                """
                    image:
                      name: "image:1"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow image by block configuration `() {
        // given
        val testee = DefaultDsl().apply {
            image {
                name = "image:1"
            }
        }

        // then
        assertDsl(DefaultDsl.serializer(), testee,
                """
                    image:
                      name: "image:1"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow image by name and block configuration `() {
        // given
        val testee = DefaultDsl().apply {
            image("image:1") {
                entrypoint("entry", "point")
            }
        }

        // then
        assertDsl(DefaultDsl.serializer(), testee,
                """
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
        val testee = DefaultDsl().apply {
            services("ser1")
            services(listOf("ser2"))
            services {
                service("ser3")
            }
        }

        // then
        assertDsl(DefaultDsl.serializer(), testee,
                """
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
        val testee = DefaultDsl().apply {
            cache {
                paths("p1")
            }
            cache("p2")
            cache(listOf("p3"))
        }

        // then
        assertDsl(DefaultDsl.serializer(), testee,
                """
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

        val testee = DefaultDsl().apply {
            image = imageDsl
            services = servicesDsl
            cache = cacheDsl
            beforeScript = beforeScriptDsl
            afterScript = afterScriptDsl
        }

        // then
        assertDsl(DefaultDsl.serializer(), testee,
                """
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