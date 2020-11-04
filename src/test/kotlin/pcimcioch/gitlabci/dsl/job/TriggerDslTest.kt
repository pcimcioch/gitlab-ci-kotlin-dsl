package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase
import pcimcioch.gitlabci.dsl.gitlabCi

internal class TriggerDslTest : DslTestBase() {

    @Test
    fun `should create empty`() {
        // given
        val testee = createTrigger {}

        // then
        assertDsl(TriggerDsl.serializer(), testee,
                """
                    {}
                """.trimIndent()
        )
    }

    @Test
    fun `should create full`() {
        // given
        val testee = createTrigger {
            project = "test/project"
            branch = "test-branch"
            strategy = TriggerStrategy.DEPEND
            include {
                local("localFile")
            }
        }

        // then
        assertDsl(TriggerDsl.serializer(), testee,
                """
                    project: "test/project"
                    branch: "test-branch"
                    strategy: "depend"
                    include:
                    - local: "localFile"
                """.trimIndent()
        )
    }

    @Test
    fun `should create from block`() {
        // given
        val testee = createTrigger {
            project = "testProject"
        }

        // then
        assertDsl(TriggerDsl.serializer(), testee,
                """
                    project: "testProject"
                """.trimIndent()
        )
    }

    @Test
    fun `should create from project name`() {
        // given
        val testee = createTrigger("testProject")

        // then
        assertDsl(TriggerDsl.serializer(), testee,
                """
                    project: "testProject"
                """.trimIndent()
        )
    }

    @Test
    fun `should create from project name, branch and strategy`() {
        // given
        val testee = createTrigger("testProject", "test-branch", TriggerStrategy.DEPEND)

        // then
        assertDsl(TriggerDsl.serializer(), testee,
                """
                    project: "testProject"
                    branch: "test-branch"
                    strategy: "depend"
                """.trimIndent()
        )
    }

    @Test
    fun `should create from project name, branch, strategy and block`() {
        // given
        val testee = createTrigger("testProject", "test-branch", TriggerStrategy.DEPEND) {
            branch = "test-branch-2"
        }

        // then
        assertDsl(TriggerDsl.serializer(), testee,
                """
                    project: "testProject"
                    branch: "test-branch-2"
                    strategy: "depend"
                """.trimIndent()
        )
    }

    @Test
    fun `should create from project name and block`() {
        // given
        val testee = createTrigger("testProject") {
            branch = "test-branch-2"
        }

        // then
        assertDsl(TriggerDsl.serializer(), testee,
                """
                    project: "testProject"
                    branch: "test-branch-2"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val includeDsl = createTriggerInclude {
            local("localFile")
        }
        val testee = createTrigger {
            include = includeDsl
        }

        // then
        assertDsl(TriggerDsl.serializer(), testee,
                """
                    include:
                    - local: "localFile"
                """.trimIndent()
        )
    }
}

internal class TriggerIncludeDslTest : DslTestBase() {

    @Test
    fun `should create empty`() {
        // given
        val testee = createTriggerInclude {}

        // then
        assertDsl(TriggerIncludeDsl.serializer(), testee,
                """
                    []
                """.trimIndent()
        )
    }

    @Test
    fun `should create full`() {
        // given
        val job1 = createJob("job 1") {}
        val testee = createTriggerInclude {
            local("localFile 1")
            local("localFile 2")
            file("project 1", "file 1", "ref 1")
            file("project 2", "file 2")
            artifact("artifact 1", job1)
            artifact("artifact 2", "job 2")
        }

        // then
        assertDsl(TriggerIncludeDsl.serializer(), testee,
                """
                    - local: "localFile 1"
                    - local: "localFile 2"
                    - project: "project 1"
                      file: "file 1"
                      ref: "ref 1"
                    - project: "project 2"
                      file: "file 2"
                    - artifact: "artifact 1"
                      job: "job 1"
                    - artifact: "artifact 2"
                      job: "job 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should add include using unary plus`() {
        // given
        val job1 = createJob("job 1") {}
        val local1 = createTriggerIncludeLocal("localFile 1")
        val local2 = createTriggerIncludeLocal("localFile 2")
        val file1 = createTriggerIncludeFile("project 1", "file 1", "ref 1")
        val file2 = createTriggerIncludeFile("project 2", "file 2")
        val artifact1 = createTriggerIncludeArtifact("artifact 1", job1)
        val artifact2 = createTriggerIncludeArtifact("artifact 2", "job 2")
        val testee = createTriggerInclude {
            +local1
            +local2
            +file1
            +file2
            +artifact1
            +artifact2
        }

        // then
        assertDsl(TriggerIncludeDsl.serializer(), testee,
                """
                    - local: "localFile 1"
                    - local: "localFile 2"
                    - project: "project 1"
                      file: "file 1"
                      ref: "ref 1"
                    - project: "project 2"
                      file: "file 2"
                    - artifact: "artifact 1"
                      job: "job 1"
                    - artifact: "artifact 2"
                      job: "job 2"
                """.trimIndent()
        )
    }
}