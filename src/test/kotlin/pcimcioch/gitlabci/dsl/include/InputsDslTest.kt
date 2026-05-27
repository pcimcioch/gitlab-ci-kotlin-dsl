package pcimcioch.gitlabci.dsl.include

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class InputsDslTest : DslTestBase<InputsDsl>(InputsDsl.serializer()) {

    @Test
    fun `should create from block`() {
        // given
        val testee = createInputs {
            add("key1", "value 1")
        }

        // then
        assertDsl(
            testee,
            """
                    "key1": "value 1"
                """
        )
    }

    @Test
    fun `should create from map`() {
        // given
        val testee = createInputs(mapOf("key1" to "value 1", "key2" to 42))

        // then
        assertDsl(
            testee,
            """
                    "key1": "value 1"
                    "key2": 42
                """
        )
    }

    @Test
    fun `should create from map and block`() {
        // given
        val testee = createInputs(mapOf("key1" to "value 1", "key2" to true)) {
            add("key3", "value 3")
        }

        // then
        assertDsl(
            testee,
            """
                    "key1": "value 1"
                    "key2": true
                    "key3": "value 3"
                """
        )
    }

    @Test
    fun `should create from enum map`() {
        // given
        val testee = createInputs(
            mapOf(
                TestEnum.FIRST to "1",
                TestEnum.SECOND to 2
            )
        )

        // then
        assertDsl(
            testee,
            """
                    "FIRST": "1"
                    "SECOND": 2
                """
        )
    }

    @Test
    fun `should create from enum map and block`() {
        // given
        val testee = createInputs(
            mapOf(
                TestEnum.FIRST to "1",
                TestEnum.SECOND to false
            )
        ) {
            add(TestEnum.THIRD, listOf("a", "b"))
        }

        // then
        assertDsl(
            testee,
            """
                    "FIRST": "1"
                    "SECOND": false
                    "THIRD":
                    - "a"
                    - "b"
                """
        )
    }

    @Test
    fun `should create empty inputs`() {
        // given
        val testee = createInputs {}

        // then
        assertDsl(
            testee,
            """
                    {}
                """,
            "[inputs] inputs map cannot be empty"
        )
    }

    @Test
    fun `should allow multiple types of adding`() {
        // given
        val testee = createInputs {
            add("key1", "value 1")
            add(TestEnum.FIRST, 123)
            "key3" to listOf(1, 2)
            TestEnum.SECOND to mapOf(Pair("a", "b"))
        }

        // then
        assertDsl(
            testee,
            """
                    "key1": "value 1"
                    "FIRST": 123
                    "key3":
                    - 1
                    - 2
                    "SECOND":
                      "a": "b"
                """
        )
    }

    @Test
    fun `should override on add`() {
        // given
        val testee = createInputs {
            add("key1", "value 1")
            add("key1", "value 2")
        }

        // then
        assertDsl(
            testee,
            """
                    "key1": "value 2"
                """
        )
    }

    @Test
    fun `should be equal`() {
        // given
        val testee = createInputs {
            add("one", 1)
            add("two", "two")
        }

        val expected = createInputs {
            add("one", 1)
            add("two", "two")
        }

        // then
        assertEquals(expected, testee)
    }

    private enum class TestEnum {
        FIRST,
        SECOND,
        THIRD
    }
}
