package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class NeedJobDslTest : DslTestBase() {

    @Test
    fun `should create empty need job`() {
        // given
        val testee = needJob {}

        // then
        assertDsl(NeedJobDsl.serializer(), testee,
                """
                    {}
                """.trimIndent(),
                "[need job='null'] job 'null' is incorrect"
        )
    }

    @Test
    fun `should not validate empty job name`() {
        // given
        val testee = needJob("")

        // then
        assertDsl(NeedJobDsl.serializer(), testee,
                """
                    job: ""
                """.trimIndent(),
                "[need job=''] job '' is incorrect"
        )
    }

    @Test
    fun `should create from name`() {
        // given
        val testee = needJob("test")

        // then
        assertDsl(NeedJobDsl.serializer(), testee,
                """
                    job: "test"
                """.trimIndent()
        )
    }

    @Test
    fun `should create from block`() {
        // given
        val testee = needJob {
            job = "test"
        }

        // then
        assertDsl(NeedJobDsl.serializer(), testee,
                """
                    job: "test"
                """.trimIndent()
        )
    }

    @Test
    fun `should create from name and block`() {
        // given
        val testee = needJob("test") {
            project = "proj"
        }

        // then
        assertDsl(NeedJobDsl.serializer(), testee,
                """
                    job: "test"
                    project: "proj"
                """.trimIndent()
        )
    }

    @Test
    fun `should create from job`() {
        // given
        val job = job("test") {}
        val testee = needJob(job)

        // then
        assertDsl(NeedJobDsl.serializer(), testee,
                """
                    job: "test"
                """.trimIndent()
        )
    }

    @Test
    fun `should create from job and block`() {
        // given
        val job = job("test") {}
        val testee = needJob(job) {
            project = "proj"
        }

        // then
        assertDsl(NeedJobDsl.serializer(), testee,
                """
                    job: "test"
                    project: "proj"
                """.trimIndent()
        )
    }

    @Test
    fun `should create full need job`() {
        // given
        val testee = needJob("test") {
            project = "proj"
            artifacts = true
            ref = "ref"
        }

        // then
        assertDsl(NeedJobDsl.serializer(), testee,
                """
                    job: "test"
                    artifacts: true
                    project: "proj"
                    ref: "ref"
                """.trimIndent()
        )
    }
}

internal class NeedsListDslTest : DslTestBase() {

    @Test
    fun `should create from block`() {
        // given
        val testee = needs {
            needJob("test")
        }

        // then
        assertDsl(NeedsListDsl.serializer(), testee,
                """
                    - job: "test"
                """.trimIndent()
        )
    }

    @Test
    fun `should create from string vararg`() {
        // given
        val testee = needs("test1", "test2")

        // then
        assertDsl(NeedsListDsl.serializer(), testee,
                """
                    - job: "test1"
                    - job: "test2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create from string list`() {
        // given
        val testee = needs(listOf("test1", "test2"))

        // then
        assertDsl(NeedsListDsl.serializer(), testee,
                """
                    - job: "test1"
                    - job: "test2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create from job vararg`() {
        // given
        val job1 = job("test1") {}
        val job2 = job("test2") {}
        val testee = needs(job1, job2)

        // then
        assertDsl(NeedsListDsl.serializer(), testee,
                """
                    - job: "test1"
                    - job: "test2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create from job list`() {
        // given
        val job1 = job("test1") {}
        val job2 = job("test2") {}
        val testee = needs(listOf(job1, job2))

        // then
        assertDsl(NeedsListDsl.serializer(), testee,
                """
                    - job: "test1"
                    - job: "test2"
                """.trimIndent()
        )
    }

    @Test
    fun `should validate jobs`() {
        // given
        val testee = needs {
            needJob("")
            needJob("test")
            needJob {}
        }

        // then
        assertDsl(NeedsListDsl.serializer(), testee,
                """
                    - job: ""
                    - job: "test"
                    - {}
                """.trimIndent(),
                "[need job=''] job '' is incorrect",
                "[need job='null'] job 'null' is incorrect"
        )
    }

    @Test
    fun `should create need jobs using all methods`() {
        // given
        val job1 = job("test4") {}
        val job2 = job("test5") {}
        val needJob = needJob("test6")
        val testee = needs {
            needJob {
                job = "test1"
            }
            needJob("test2")
            needJob("test3") {
                artifacts = false
                project = "proj"
                ref = "ref"
            }
            needJob(job1)
            needJob(job2) {
                project = "proj2"
            }
            +needJob
        }

        // then
        assertDsl(NeedsListDsl.serializer(), testee,
                """
                    - job: "test1"
                    - job: "test2"
                    - job: "test3"
                      artifacts: false
                      project: "proj"
                      ref: "ref"
                    - job: "test4"
                    - job: "test5"
                      project: "proj2"
                    - job: "test6"
                """.trimIndent()
        )
    }
}