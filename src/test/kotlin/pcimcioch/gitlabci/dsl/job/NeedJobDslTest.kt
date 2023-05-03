package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class NeedJobDslTest : DslTestBase<NeedJobDsl>(NeedJobDsl.serializer()) {

    @Test
    fun `should create empty need job`() {
        // given
        val testee = createNeedJob {}

        // then
        assertDsl(
            testee,
            """
                    {}
                """,
            "[need job='null'] job 'null' is incorrect"
        )
    }

    @Test
    fun `should not validate empty job name`() {
        // given
        val testee = createNeedJob("")

        // then
        assertDsl(
            testee,
            """
                    job: ""
                """,
            "[need job=''] job '' is incorrect"
        )
    }

    @Test
    fun `should create from name`() {
        // given
        val testee = createNeedJob("test")

        // then
        assertDsl(
            testee,
            """
                    job: "test"
                """
        )
    }

    @Test
    fun `should create from block`() {
        // given
        val testee = createNeedJob {
            job = "test"
        }

        // then
        assertDsl(
            testee,
            """
                    job: "test"
                """
        )
    }

    @Test
    fun `should create from name and block`() {
        // given
        val testee = createNeedJob("test") {
            project = "proj"
        }

        // then
        assertDsl(
            testee,
            """
                    job: "test"
                    project: "proj"
                """
        )
    }

    @Test
    fun `should create from job`() {
        // given
        val job = createJob("test") {}
        val testee = createNeedJob(job)

        // then
        assertDsl(
            testee,
            """
                    job: "test"
                """
        )
    }

    @Test
    fun `should create from job and block`() {
        // given
        val job = createJob("test") {}
        val testee = createNeedJob(job) {
            project = "proj"
        }

        // then
        assertDsl(
            testee,
            """
                    job: "test"
                    project: "proj"
                """
        )
    }

    @Test
    fun `should create full need job`() {
        // given
        val testee = createNeedJob("test") {
            project = "proj"
            artifacts = true
            ref = "ref"
            optional = true
            pipeline = "pip"
        }

        // then
        assertDsl(
            testee,
            """
                    job: "test"
                    artifacts: true
                    project: "proj"
                    ref: "ref"
                    pipeline: "pip"
                    optional: true
                """
        )
    }

    @Test
    fun `should be equal`() {
        // given
        val testee = createNeedJob("test") {
            project = "proj"
            artifacts = true
            ref = "ref"
        }

        val expected = createNeedJob("test") {
            project = "proj"
            artifacts = true
            ref = "ref"
        }

        // then
        assertEquals(expected, testee)
    }
}

internal class NeedsListDslTest : DslTestBase<NeedsListDsl>(NeedsListDsl.serializer()) {

    @Test
    fun `should create from block`() {
        // given
        val testee = createNeeds {
            needJob("test")
        }

        // then
        assertDsl(
            testee,
            """
                    - job: "test"
                """
        )
    }

    @Test
    fun `should create from string vararg`() {
        // given
        val testee = createNeeds("test1", "test2")

        // then
        assertDsl(
            testee,
            """
                    - job: "test1"
                    - job: "test2"
                """
        )
    }

    @Test
    fun `should create from string list`() {
        // given
        val testee = createNeeds(listOf("test1", "test2"))

        // then
        assertDsl(
            testee,
            """
                    - job: "test1"
                    - job: "test2"
                """
        )
    }

    @Test
    fun `should create from job vararg`() {
        // given
        val job1 = createJob("test1") {}
        val job2 = createJob("test2") {}
        val testee = createNeeds(job1, job2)

        // then
        assertDsl(
            testee,
            """
                    - job: "test1"
                    - job: "test2"
                """
        )
    }

    @Test
    fun `should create from job list`() {
        // given
        val job1 = createJob("test1") {}
        val job2 = createJob("test2") {}
        val testee = createNeeds(listOf(job1, job2))

        // then
        assertDsl(
            testee,
            """
                    - job: "test1"
                    - job: "test2"
                """
        )
    }

    @Test
    fun `should validate jobs`() {
        // given
        val testee = createNeeds {
            needJob("")
            needJob("test")
            needJob {}
        }

        // then
        assertDsl(
            testee,
            """
                    - job: ""
                    - job: "test"
                    - {}
                """,
            "[need job=''] job '' is incorrect",
            "[need job='null'] job 'null' is incorrect"
        )
    }

    @Test
    fun `should create need jobs using all methods`() {
        // given
        val job1 = createJob("test4") {}
        val job2 = createJob("test5") {}
        val needJob = createNeedJob("test6")
        val testee = createNeeds {
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
        assertDsl(
            testee,
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
                """
        )
    }

    @Test
    fun `should be equal`() {
        val testee = createNeeds {
            needJob("lol")
            needJob("gottem")
        }

        val other = createNeeds {
            needJob("lol")
            needJob("gottem")
        }

        assertEquals(other, testee)
    }
}