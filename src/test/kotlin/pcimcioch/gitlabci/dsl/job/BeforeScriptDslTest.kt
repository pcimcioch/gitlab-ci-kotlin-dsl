package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class BeforeScriptDslTest : DslTestBase() {

    @Test
    fun `should create script from block`() {
        // given
        val testee = beforeScript {
            exec("command 1")
        }

        // then
        assertDsl(BeforeScriptDsl.serializer(), testee,
                """
                    - "command 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create script from vararg`() {
        // given
        val testee = beforeScript("command 1", "command 2")

        // then
        assertDsl(BeforeScriptDsl.serializer(), testee,
                """
                    - "command 1"
                    - "command 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create script from list`() {
        // given
        val testee = beforeScript(listOf("command 1", "command 2"))

        // then
        assertDsl(BeforeScriptDsl.serializer(), testee,
                """
                    - "command 1"
                    - "command 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create script with multiple command`() {
        // given
        val testee = beforeScript {
            exec("command 1")
            exec("command 2")
        }

        // then
        assertDsl(BeforeScriptDsl.serializer(), testee,
                """
                    - "command 1"
                    - "command 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create script with one command`() {
        // given
        val testee = beforeScript {
            exec("command 1")
        }

        // then
        assertDsl(BeforeScriptDsl.serializer(), testee,
                """
                    - "command 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create script with no commands`() {
        // given
        val testee = beforeScript {}

        // then
        assertDsl(BeforeScriptDsl.serializer(), testee,
                """
                    []
                """.trimIndent()
        )
    }

    @Test
    fun `should add commands with unary plus`() {
        // given
        val testee = beforeScript {
            exec("command 1")
            +"command 2"
        }

        // then
        assertDsl(BeforeScriptDsl.serializer(), testee,
                """
                    - "command 1"
                    - "command 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val testee = beforeScript {
            commands = mutableListOf("command 1", "command 2")
        }

        // then
        assertDsl(BeforeScriptDsl.serializer(), testee,
                """
                    - "command 1"
                    - "command 2"
                """.trimIndent()
        )
    }
}