package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase
import pcimcioch.gitlabci.dsl.Duration
import kotlin.math.min

internal class JobDslTest : DslTestBase() {

    @Test
    fun `should create job from block`() {
        // given
        val testee = job {
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
        val testee = job("test") {
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
        val testee = job {
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
        val testee = job {
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
        val testee = job {
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
        val testee = job {
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
        val testee = job {
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
        val testee = job {
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
        val testee = job {
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
        val testee = job {
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
        val testee = job {
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
        val testee = job {
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
        val testee = job {}

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
        val testee = job {
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
        val testee = job {
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
        val testee = job {
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
        val testee = job {
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
        val testee = job {
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
        val job1 = job("job1") {}
        val job2 = job("job2") {}
        val job3 = job("job3") {}
        val job4 = job("job4") {}

        val testee = job {
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
}