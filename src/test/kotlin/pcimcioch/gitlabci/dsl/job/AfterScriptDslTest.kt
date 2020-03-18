package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class AfterScriptDslTest : DslTestBase() {

    @Test
    fun `should create script from block`() {
        // given
        val testee = createAfterScript {
            exec("command 1")
        }

        // then
        assertDsl(AfterScriptDsl.serializer(), testee,
                """
                    - "command 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create script from vararg`() {
        // given
        val testee = createAfterScript("command 1", "command 2")

        // then
        assertDsl(AfterScriptDsl.serializer(), testee,
                """
                    - "command 1"
                    - "command 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create script from list`() {
        // given
        val testee = createAfterScript(listOf("command 1", "command 2"))

        // then
        assertDsl(AfterScriptDsl.serializer(), testee,
                """
                    - "command 1"
                    - "command 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create script with multiple commands`() {
        // given
        val testee = createAfterScript {
            exec("command 1")
            exec("command 2")
        }

        // then
        assertDsl(AfterScriptDsl.serializer(), testee,
                """
                    - "command 1"
                    - "command 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create script with one command`() {
        // given
        val testee = createAfterScript {
            exec("command 1")
        }

        // then
        assertDsl(AfterScriptDsl.serializer(), testee,
                """
                    - "command 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create script with no commands`() {
        // given
        val testee = createAfterScript {}

        // then
        assertDsl(AfterScriptDsl.serializer(), testee,
                """
                    []
                """.trimIndent()
        )
    }

    @Test
    fun `should add commands with unary plus`() {
        // given
        val testee = createAfterScript {
            exec("command 1")
            +"command 2"
        }

        // then
        assertDsl(AfterScriptDsl.serializer(), testee,
                """
                    - "command 1"
                    - "command 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val testee = createAfterScript {
            commands = mutableListOf("command 1", "command 2")
        }

        // then
        assertDsl(AfterScriptDsl.serializer(), testee,
                """
                    - "command 1"
                    - "command 2"
                """.trimIndent()
        )
    }
}