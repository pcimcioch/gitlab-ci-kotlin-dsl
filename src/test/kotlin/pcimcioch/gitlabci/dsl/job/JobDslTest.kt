package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase
import pcimcioch.gitlabci.dsl.Duration
import pcimcioch.gitlabci.dsl.stage.createStage

internal class JobDslTest : DslTestBase() {

    @Test
    fun `should create job from block`() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    script:
                    - "test command"
                """.trimIndent()
        )
    }

    @Test
    fun `should create job from name and block`() {
        // given
        val testee = createJob("test") {
            script("test command")
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    script:
                    - "test command"
                """.trimIndent()
        )
    }

    @Test
    fun `should validate null name`() {
        // given
        val testee = createJob {
            script("test command")
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    script:
                    - "test command"
                """.trimIndent(),
                "[job name='null'] name 'null' is incorrect"
        )
    }

    @Test
    fun `should validate empty name`() {
        // given
        val testee = createJob {
            name = ""
            script("test command")
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    script:
                    - "test command"
                """.trimIndent(),
                "[job name=''] name '' is incorrect"
        )
    }

    @Test
    fun `should validate restricted name`() {
        // given
        val testee = createJob {
            name = "image"
            script("test command")
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    script:
                    - "test command"
                """.trimIndent(),
                "[job name='image'] name 'image' is incorrect"
        )
    }

    @Test
    fun `should validate startIn without when`() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")
            startIn = Duration(minutes = 10)
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    start_in: "10 min"
                    script:
                    - "test command"
                """.trimIndent(),
                "[job name='test'] startIn can be used only with when=delayed jobs"
        )
    }

    @Test
    fun `should validate startIn without delayed when`() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")
            whenRun = WhenRunType.ALWAYS
            startIn = Duration(minutes = 10)
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    when: "always"
                    start_in: "10 min"
                    script:
                    - "test command"
                """.trimIndent(),
                "[job name='test'] startIn can be used only with when=delayed jobs"
        )
    }

    @Test
    fun `should validate no script`() {
        // given
        val testee = createJob {
            name = "test"
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    {}
                """.trimIndent(),
                "[job name='test'] at least one script command must be configured"
        )
    }

    @Test
    fun `should validate empty script`() {
        // given
        val testee = createJob {
            name = "test"
            script()
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    script: []
                """.trimIndent(),
                "[job name='test'][script] commands list cannot be empty"
        )
    }

    @Test
    fun `should validate to small parallel`() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")
            parallel = 1
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    parallel: 1
                    script:
                    - "test command"
                """.trimIndent(),
                "[job name='test'] parallel must be in range [2, 50]"
        )
    }

    @Test
    fun `should validate to big parallel`() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")
            parallel = 51
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    parallel: 51
                    script:
                    - "test command"
                """.trimIndent(),
                "[job name='test'] parallel must be in range [2, 50]"
        )
    }

    @Test
    fun `should validate nested objects`() {
        val testee = createJob {
            name = "test"

            beforeScript("before 1", "before 2")
            script("script 1", "script 2")
            afterScript("after 1", "after 2")

            inherit {
                default(true)
            }
            retry(5)
            image("")
            services("service 1", "")
            needs("job 2", "")
            variables {}
            cache {
                paths("path")
                key {
                    prefix = "pre/fix"
                    files("file")
                }
            }
            artifacts("artifact")
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    image:
                      name: ""
                    inherit:
                      default: true
                    retry:
                      max: 5
                    services:
                    - name: "service 1"
                    - name: ""
                    needs:
                    - job: "job 2"
                    - job: ""
                    cache:
                      paths:
                      - "path"
                      key:
                        prefix: "pre/fix"
                        files:
                        - "file"
                    artifacts:
                      paths:
                      - "artifact"
                    before_script:
                    - "before 1"
                    - "before 2"
                    script:
                    - "script 1"
                    - "script 2"
                    after_script:
                    - "after 1"
                    - "after 2"
                    variables: {}
                """.trimIndent(),
                "[job name='test'][retry] max attempts must be in range [0, 2]",
                "[job name='test'][image] name '' is incorrect",
                "[job name='test'][service name=''] name '' is incorrect",
                "[job name='test'][need job=''] job '' is incorrect",
                "[job name='test'][variables] variables map cannot be empty",
                "[job name='test'][cache][key] prefix value 'pre/fix' can't contain '/' nor '%2F'"
        )
    }

    @Test
    fun `should create empty job`() {
        // given
        val testee = createJob {}

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    {}
                """.trimIndent(),
                "[job name='null'] name 'null' is incorrect",
                "[job name='null'] at least one script command must be configured"
        )
    }

    @Test
    fun `should create full job`() {
        // TODO implement
    }

    @Test
    fun `should create job with empty collections`() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")

            extends(listOf<String>())
            tags()
            dependencies(listOf<String>())
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    extends: []
                    tags: []
                    dependencies: []
                    script:
                    - "test command"
                """.trimIndent()
        )
    }

    @Test
    fun `should create job with single element collections`() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")

            extends("extend 1")
            tags("tag 1")
            dependencies("depend 1")
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    extends:
                    - "extend 1"
                    tags:
                    - "tag 1"
                    dependencies:
                    - "depend 1"
                    script:
                    - "test command"
                """.trimIndent()
        )
    }

    @Test
    fun `should create job with multiple element collections`() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")

            extends("extend 1", "extend 2")
            tags("tag 1", "tag 2")
            dependencies("depend 1", "depend 2")
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    extends:
                    - "extend 1"
                    - "extend 2"
                    tags:
                    - "tag 1"
                    - "tag 2"
                    dependencies:
                    - "depend 1"
                    - "depend 2"
                    script:
                    - "test command"
                """.trimIndent()
        )
    }

    @Test
    fun `should merge collections`() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")

            extends("extend 1", "extend 2")
            extends(listOf("extend 3", "extend 4"))
            tags("tag 1", "tag 2")
            tags(listOf("tag 3", "tag 4"))
            dependencies("depend 1", "depend 2")
            dependencies(listOf("depend 3", "depend 4"))
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    extends:
                    - "extend 1"
                    - "extend 2"
                    - "extend 3"
                    - "extend 4"
                    tags:
                    - "tag 1"
                    - "tag 2"
                    - "tag 3"
                    - "tag 4"
                    dependencies:
                    - "depend 1"
                    - "depend 2"
                    - "depend 3"
                    - "depend 4"
                    script:
                    - "test command"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow reset dependencies`() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")

            emptyDependencies()
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    dependencies: []
                    script:
                    - "test command"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow objects in collections`() {
        // given
        val job1 = createJob("job1") {}
        val job2 = createJob("job2") {}
        val job3 = createJob("job3") {}
        val job4 = createJob("job4") {}

        val testee = createJob {
            name = "test"
            script("test command")

            extends(job1, job2)
            extends(listOf(job3, job4))
            dependencies(job1, job2)
            dependencies(listOf(job3, job4))
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    extends:
                    - "job1"
                    - "job2"
                    - "job3"
                    - "job4"
                    dependencies:
                    - "job1"
                    - "job2"
                    - "job3"
                    - "job4"
                    script:
                    - "test command"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow different script options`() {
        // given
        val testee = createJob {
            name = "test"

            script {
                +"command 1"
            }
            script("command 2")
            script(listOf("command 3"))
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    script:
                    - "command 1"
                    - "command 2"
                    - "command 3"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow different beforeScript options`() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")

            beforeScript {
                +"command 1"
            }
            beforeScript("command 2")
            beforeScript(listOf("command 3"))
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    before_script:
                    - "command 1"
                    - "command 2"
                    - "command 3"
                    script:
                    - "test command"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow different afterScript options`() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")

            afterScript {
                +"command 1"
            }
            afterScript("command 2")
            afterScript(listOf("command 3"))
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    script:
                    - "test command"
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
        val testee = createJob {
            name = "test"
            script("test command")

            image("image:1")
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    image:
                      name: "image:1"
                    script:
                    - "test command"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow image by block configuration `() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")

            image {
                name = "image:1"
            }
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    image:
                      name: "image:1"
                    script:
                    - "test command"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow image by name and block configuration `() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")

            image("image:1") {
                entrypoint("entry", "point")
            }
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    image:
                      name: "image:1"
                      entrypoint:
                      - "entry"
                      - "point"
                    script:
                    - "test command"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow different services options`() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")

            services("ser1")
            services(listOf("ser2"))
            services {
                service("ser3")
            }
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    services:
                    - name: "ser1"
                    - name: "ser2"
                    - name: "ser3"
                    script:
                    - "test command"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow different needs options`() {
        // given
        val job3 = createJob("job3") {}
        val job4 = createJob("job4") {}
        val testee = createJob {
            name = "test"
            script("test command")

            needs("job1")
            needs(listOf("job2"))
            needs(job3)
            needs(listOf(job4))
            needs {
                needJob("job5")
            }
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    needs:
                    - job: "job1"
                    - job: "job2"
                    - job: "job3"
                    - job: "job4"
                    - job: "job5"
                    script:
                    - "test command"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow to set stage from object`() {
        // given
        val testStage = createStage("testStage")
        val testee = createJob {
            name = "test"
            script("test command")

            stage(testStage)
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    stage: "testStage"
                    script:
                    - "test command"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow retry from number`() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")

            retry(2)
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    retry:
                      max: 2
                    script:
                    - "test command"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow retry from block`() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")

            retry {
                max = 2
            }
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    retry:
                      max: 2
                    script:
                    - "test command"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow retry from number and block`() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")

            retry(2) {
                whenRetry(WhenRetryType.API_FAILURE)
            }
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    retry:
                      max: 2
                      when:
                      - "api_failure"
                    script:
                    - "test command"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow different variables options`() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")

            variables {
                "k1" to "v1"
            }
            variables(mapOf("k2" to "v2"))
            variables(mapOf("k3" to "v3")) {
                "k4" to "v4"
            }
            variables(mapOf(RunnerSettingsVariables.GIT_DEPTH to "2"))
            variables(mapOf(RunnerSettingsVariables.GET_SOURCES_ATTEMPTS to 1)) {
                RunnerSettingsVariables.ARTIFACT_DOWNLOAD_ATTEMPTS to 3
            }
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    script:
                    - "test command"
                    variables:
                      "k1": "v1"
                      "k2": "v2"
                      "k3": "v3"
                      "k4": "v4"
                      "GIT_DEPTH": "2"
                      "GET_SOURCES_ATTEMPTS": "1"
                      "ARTIFACT_DOWNLOAD_ATTEMPTS": "3"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow different cache options`() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")

            cache {
                paths("p1")
            }
            cache("p2")
            cache(listOf("p3"))
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    cache:
                      paths:
                      - "p1"
                      - "p2"
                      - "p3"
                    script:
                    - "test command"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow different artifacts options`() {
        // given
        val testee = createJob {
            name = "test"
            script("test command")

            artifacts {
                paths("a1")
            }
            artifacts("a2")
            artifacts(listOf("a3"))
        }

        // then
        assertDsl(JobDsl.serializer(), testee,
                """
                    artifacts:
                      paths:
                      - "a1"
                      - "a2"
                      - "a3"
                    script:
                    - "test command"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow direct access`() {
        // TODO implement
    }
}