package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class ScriptDslTest : DslTestBase<ScriptDsl>(ScriptDsl.serializer()) {

    @Test
    fun `should create script from block`() {
        // given
        val testee = createScript {
            exec("command 1")
        }

        // then
        assertDsl(testee,
                """
                    - "command 1"
                """
        )
    }

    @Test
    fun `should create script from vararg`() {
        // given
        val testee = createScript("command 1", "command 2")

        // then
        assertDsl(testee,
                """
                    - "command 1"
                    - "command 2"
                """
        )
    }

    @Test
    fun `should create script from list`() {
        // given
        val testee = createScript(listOf("command 1", "command 2"))

        // then
        assertDsl(testee,
                """
                    - "command 1"
                    - "command 2"
                """
        )
    }

    @Test
    fun `should create script with multiple commands`() {
        // given
        val testee = createScript {
            exec("command 1")
            exec("command 2")
        }

        // then
        assertDsl(testee,
                """
                    - "command 1"
                    - "command 2"
                """
        )
    }

    @Test
    fun `should create script with one command`() {
        // given
        val testee = createScript {
            exec("command 1")
        }

        // then
        assertDsl(testee,
                """
                    - "command 1"
                """
        )
    }

    @Test
    fun `should add commands with unary plus`() {
        // given
        val testee = createScript {
            exec("command 1")
            +"command 2"
        }

        // then
        assertDsl(testee,
                """
                    - "command 1"
                    - "command 2"
                """
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val testee = createScript {
            commands = mutableListOf("command 1", "command 2")
        }

        // then
        assertDsl(testee,
                """
                    - "command 1"
                    - "command 2"
                """
        )
    }
}