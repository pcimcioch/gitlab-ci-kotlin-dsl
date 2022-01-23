package pcimcioch.gitlabci.dsl.default

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase
import pcimcioch.gitlabci.dsl.Duration
import pcimcioch.gitlabci.dsl.job.WhenRetryType
import pcimcioch.gitlabci.dsl.job.createAfterScript
import pcimcioch.gitlabci.dsl.job.createArtifacts
import pcimcioch.gitlabci.dsl.job.createBeforeScript
import pcimcioch.gitlabci.dsl.job.createCache
import pcimcioch.gitlabci.dsl.job.createImage
import pcimcioch.gitlabci.dsl.job.createRetry
import pcimcioch.gitlabci.dsl.job.createServices

internal class DefaultDslTest : DslTestBase<DefaultDsl>(DefaultDsl.serializer()) {

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
            tags("tag 1", "tag 2")
            artifacts("art")
            retry(5)
            timeout = Duration(days = 5)
            interruptible = true
        }

        // then
        assertDsl(testee,
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
                    tags:
                    - "tag 1"
                    - "tag 2"
                    artifacts:
                      paths:
                      - "art"
                    retry:
                      max: 5
                    timeout: "5 day"
                    interruptible: true
                    before_script:
                    - "before 1"
                    - "before 2"
                    after_script:
                    - "after 1"
                    - "after 2"
                """,
                "[default][image] name '' is incorrect",
                "[default][service name=''] name '' is incorrect",
                "[default][cache][key] prefix value 'pre/fix' can't contain '/' nor '%2F'",
                "[default][retry] max attempts must be in range [0, 2]"
        )
    }

    @Test
    fun `should create empty default`() {
        // given
        val testee = DefaultDsl()

        // then
        assertDsl(testee,
                """
                    {}
                """
        )
    }

    @Test
    fun `should create full default`() {
        // given
        val testee = DefaultDsl().apply {
            image("testImage")
            services("testService")
            cache("testCache")
            tags("tag")
            artifacts("art")
            retry(2)
            timeout = Duration(days = 5)
            interruptible = true
            beforeScript("before")
            afterScript("after")
        }

        // then
        assertDsl(testee,
                """
                    image:
                      name: "testImage"
                    services:
                    - name: "testService"
                    cache:
                      paths:
                      - "testCache"
                    tags:
                    - "tag"
                    artifacts:
                      paths:
                      - "art"
                    retry:
                      max: 2
                    timeout: "5 day"
                    interruptible: true
                    before_script:
                    - "before"
                    after_script:
                    - "after"
                """
        )
    }

    @Test
    fun `should create default with empty collections`() {
        // given
        val testee = DefaultDsl().apply {
            tags()
        }

        // then
        assertDsl(testee,
                """
                    tags: []
                """
        )
    }

    @Test
    fun `should create default with single element collections`() {
        // given
        val testee = DefaultDsl().apply {
            tags("tag 1")
        }

        // then
        assertDsl(testee,
                """
                    tags:
                    - "tag 1"
                """
        )
    }

    @Test
    fun `should create default with multiple element collections`() {
        // given
        val testee = DefaultDsl().apply {
            tags("tag 1", "tag 2")
        }

        // then
        assertDsl(testee,
                """
                    tags:
                    - "tag 1"
                    - "tag 2"
                """
        )
    }

    @Test
    fun `should merge collections`() {
        // given
        val testee = DefaultDsl().apply {
            tags("tag 1", "tag 2")
            tags(listOf("tag 3", "tag 4"))
        }

        // then
        assertDsl(testee,
                """
                    tags:
                    - "tag 1"
                    - "tag 2"
                    - "tag 3"
                    - "tag 4"
                """
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
        assertDsl(testee,
                """
                    before_script:
                    - "command 1"
                    - "command 2"
                    - "command 3"
                """
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
        assertDsl(testee,
                """
                    after_script:
                    - "command 1"
                    - "command 2"
                    - "command 3"
                """
        )
    }

    @Test
    fun `should allow image by name configuration `() {
        // given
        val testee = DefaultDsl().apply {
            image("image:1")
        }

        // then
        assertDsl(testee,
                """
                    image:
                      name: "image:1"
                """
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
        assertDsl(testee,
                """
                    image:
                      name: "image:1"
                """
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
        assertDsl(testee,
                """
                    image:
                      name: "image:1"
                      entrypoint:
                      - "entry"
                      - "point"
                """
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
        assertDsl(testee,
                """
                    services:
                    - name: "ser1"
                    - name: "ser2"
                    - name: "ser3"
                """
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
        assertDsl(testee,
                """
                    cache:
                      paths:
                      - "p1"
                      - "p2"
                      - "p3"
                """
        )
    }

    @Test
    fun `should allow retry from number`() {
        // given
        val testee = DefaultDsl().apply {
            retry(2)
        }

        // then
        assertDsl(testee,
                """
                    retry:
                      max: 2
                """
        )
    }

    @Test
    fun `should allow retry from block`() {
        // given
        val testee = DefaultDsl().apply {
            retry {
                max = 2
            }
        }

        // then
        assertDsl(testee,
                """
                    retry:
                      max: 2
                """
        )
    }

    @Test
    fun `should allow retry from number and block`() {
        // given
        val testee = DefaultDsl().apply {
            retry(2) {
                whenRetry(WhenRetryType.API_FAILURE)
            }
        }

        // then
        assertDsl(testee,
                """
                    retry:
                      max: 2
                      when:
                      - "api_failure"
                """
        )
    }

    @Test
    fun `should allow different artifacts options`() {
        // given
        val testee = DefaultDsl().apply {
            artifacts {
                paths("a1")
            }
            artifacts("a2")
            artifacts(listOf("a3"))
        }

        // then
        assertDsl(testee,
                """
                    artifacts:
                      paths:
                      - "a1"
                      - "a2"
                      - "a3"
                """
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val imageDsl = createImage("testImage")
        val servicesDsl = createServices("testService")
        val cacheDsl = createCache("testCache")
        val artifactsDsl = createArtifacts("testArtifact")
        val retryDsl = createRetry(1)
        val beforeScriptDsl = createBeforeScript("before")
        val afterScriptDsl = createAfterScript("after")

        val testee = DefaultDsl().apply {
            image = imageDsl
            services = servicesDsl
            cache = cacheDsl
            tags = mutableSetOf("testTag")
            artifacts = artifactsDsl
            retry = retryDsl
            beforeScript = beforeScriptDsl
            afterScript = afterScriptDsl
        }

        // then
        assertDsl(testee,
                """
                    image:
                      name: "testImage"
                    services:
                    - name: "testService"
                    cache:
                      paths:
                      - "testCache"
                    tags:
                    - "testTag"
                    artifacts:
                      paths:
                      - "testArtifact"
                    retry:
                      max: 1
                    before_script:
                    - "before"
                    after_script:
                    - "after"
                """
        )
    }
}