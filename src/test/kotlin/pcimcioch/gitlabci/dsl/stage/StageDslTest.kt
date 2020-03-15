package pcimcioch.gitlabci.dsl.stage

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class StageDslTest : DslTestBase() {

    @Test
    fun `should create from name`() {
        // given
        val testee = stage("stage 1")

        // then
        assertDsl(StageDsl.serializer(), testee,
                """
                    name: "stage 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create from block`() {
        // given
        val testee = stage {
            name = "stage 1"
        }

        // then
        assertDsl(StageDsl.serializer(), testee,
                """
                    name: "stage 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create from name and block`() {
        // given
        val testee = stage("stage 1") {
            name = "stage 2"
        }

        // then
        assertDsl(StageDsl.serializer(), testee,
                """
                    name: "stage 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should not validate null name`() {
        // given
        val testee = stage {}

        // then
        assertDsl(StageDsl.serializer(), testee,
                """
                    {}
                """.trimIndent(),
                "[stage name='null'] name 'null' is incorrect"
        )
    }

    @Test
    fun `should not validate empty name`() {
        // given
        val testee = stage("")

        // then
        assertDsl(StageDsl.serializer(), testee,
                """
                    name: ""
                """.trimIndent(),
                "[stage name=''] name '' is incorrect"
        )
    }
}