package pcimcioch.gitlabci.dsl.stage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase
import pcimcioch.gitlabci.dsl.job.AfterScriptDsl
import pcimcioch.gitlabci.dsl.job.createAfterScript

internal class StagesDslTest : DslTestBase() {

    @Test
    fun `should create stages from block`() {
        // given
        val testee = createStages {
            stage("stage 1")
        }

        // then
        assertDsl(StagesDsl.serializer(), testee,
                """
                    - "stage 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create stages from vararg`() {
        // given
        val testee = createStages("stage 1", "stage 2")

        // then
        assertDsl(StagesDsl.serializer(), testee,
                """
                    - "stage 1"
                    - "stage 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create stages from list`() {
        // given
        val testee = createStages(listOf("stage 1", "stage 2"))

        // then
        assertDsl(StagesDsl.serializer(), testee,
                """
                    - "stage 1"
                    - "stage 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create stages with multiple commands`() {
        // given
        val testee = createStages {
            stage("stage 1")
            stage("stage 2")
        }

        // then
        assertDsl(StagesDsl.serializer(), testee,
                """
                    - "stage 1"
                    - "stage 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create stages with one command`() {
        // given
        val testee = createStages {
            stage("stage 1")
        }

        // then
        assertDsl(StagesDsl.serializer(), testee,
                """
                    - "stage 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create stages with no commands`() {
        // given
        val testee = createStages {}

        // then
        assertDsl(StagesDsl.serializer(), testee,
                """
                    []
                """.trimIndent()
        )
    }

    @Test
    fun `should add stage with unary plus`() {
        // given
        val testee = createStages {
            stage("stage 1")
            +"stage 2"
        }

        // then
        assertDsl(StagesDsl.serializer(), testee,
                """
                    - "stage 1"
                    - "stage 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val testee = createStages {
            stages = mutableListOf("stage 1", "stage 2")
        }

        // then
        assertDsl(StagesDsl.serializer(), testee,
                """
                    - "stage 1"
                    - "stage 2"
                """.trimIndent()
        )
    }
}