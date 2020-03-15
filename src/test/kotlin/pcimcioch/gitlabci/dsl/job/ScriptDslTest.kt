package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class ScriptDslTest : DslTestBase() {

    @Test
    fun `should create script with multiple commands`() {
        // given
        val testee = script {
            exec("command 1")
            exec("command 2")
        }

        // then
        assertDsl(ScriptDsl.serializer(), testee,
                """
                    - "command 1"
                    - "command 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create script with one command`() {
        // given
        val testee = script {
            exec("command 1")
        }

        // then
        assertDsl(ScriptDsl.serializer(), testee,
                """
                    - "command 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should not validate script with no commands`() {
        // given
        val testee = script {}

        // then
        assertDsl(ScriptDsl.serializer(), testee,
                """
                    []
                """.trimIndent(),
                "[script] commands list cannot be empty"
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val testee = script {
            commands = mutableListOf("command 1", "command 2")
        }

        // then
        assertDsl(ScriptDsl.serializer(), testee,
                """
                    - "command 1"
                    - "command 2"
                """.trimIndent()
        )
    }
}