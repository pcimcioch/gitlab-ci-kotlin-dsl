package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class BeforeScriptDslTest : DslTestBase<BeforeScriptDsl>(BeforeScriptDsl.serializer()) {

    @Test
    fun `should create script from block`() {
        // given
        val testee = createBeforeScript {
            exec("command 1")
        }

        // then
        assertDsl(
            testee,
            """
                    - "command 1"
                """
        )
    }

    @Test
    fun `should create script from vararg`() {
        // given
        val testee = createBeforeScript("command 1", "command 2")

        // then
        assertDsl(
            testee,
            """
                    - "command 1"
                    - "command 2"
                """
        )
    }

    @Test
    fun `should create script from list`() {
        // given
        val testee = createBeforeScript(listOf("command 1", "command 2"))

        // then
        assertDsl(
            testee,
            """
                    - "command 1"
                    - "command 2"
                """
        )
    }

    @Test
    fun `should create script with multiple command`() {
        // given
        val testee = createBeforeScript {
            exec("command 1")
            exec("command 2")
        }

        // then
        assertDsl(
            testee,
            """
                    - "command 1"
                    - "command 2"
                """
        )
    }

    @Test
    fun `should create script with one command`() {
        // given
        val testee = createBeforeScript {
            exec("command 1")
        }

        // then
        assertDsl(
            testee,
            """
                    - "command 1"
                """
        )
    }

    @Test
    fun `should create script with no commands`() {
        // given
        val testee = createBeforeScript {}

        // then
        assertDsl(
            testee,
            """
                    []
                """
        )
    }

    @Test
    fun `should add commands with unary plus`() {
        // given
        val testee = createBeforeScript {
            exec("command 1")
            +"command 2"
        }

        // then
        assertDsl(
            testee,
            """
                    - "command 1"
                    - "command 2"
                """
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val testee = createBeforeScript {
            commands = mutableListOf("command 1", "command 2")
        }

        // then
        assertDsl(
            testee,
            """
                    - "command 1"
                    - "command 2"
                """
        )
    }

    @Test
    fun `should be equal`() {
        // given
        val testee = createBeforeScript("command 1", "command 2")
        val expected = createBeforeScript("command 1", "command 2")

        // then
        assertEquals(expected, testee)
    }

    @Test
    fun `should not be equal`() {
        // given
        val testee = createBeforeScript("command 1", "command 2")
        val expected = createBeforeScript("command 2", "command 1")

        // then
        assertNotEquals(expected, testee)
    }
}