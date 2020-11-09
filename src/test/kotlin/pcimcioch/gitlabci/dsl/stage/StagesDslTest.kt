package pcimcioch.gitlabci.dsl.stage

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class StagesDslTest : DslTestBase<StagesDsl>(StagesDsl.serializer()) {

    @Test
    fun `should create stages with one command`() {
        // given
        val testee = StagesDsl().apply {
            stage("stage 1")
        }

        // then
        assertDsl(testee,
                """
                    - "stage 1"
                """
        )
    }

    @Test
    fun `should create stages with multiple commands`() {
        // given
        val testee = StagesDsl().apply {
            stage("stage 1")
            stage("stage 2")
        }

        // then
        assertDsl(testee,
                """
                    - "stage 1"
                    - "stage 2"
                """
        )
    }

    @Test
    fun `should create empty`() {
        // given
        val testee = StagesDsl()

        // then
        assertDsl(testee,
                """
                    []
                """
        )
    }

    @Test
    fun `should add stage with unary plus`() {
        // given
        val testee = StagesDsl().apply {
            stage("stage 1")
            +"stage 2"
        }

        // then
        assertDsl(testee,
                """
                    - "stage 1"
                    - "stage 2"
                """
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val testee = StagesDsl().apply {
            stages = mutableListOf("stage 1", "stage 2")
        }

        // then
        assertDsl(testee,
                """
                    - "stage 1"
                    - "stage 2"
                """
        )
    }
}