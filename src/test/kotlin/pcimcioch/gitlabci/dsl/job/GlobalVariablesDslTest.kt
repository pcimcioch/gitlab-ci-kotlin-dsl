package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class GlobalVariablesDslTest : DslTestBase<GlobalVariablesDsl>(GlobalVariablesDsl.serializer()) {

    @Test
    fun `should create from block`() {
        // given
        val testee = createGlobalVariables {
            add("key1", "value 1")
        }

        // then
        assertDsl(
            testee,
            """
                    "key1":
                      value: "value 1"
                """
        )
    }

    @Test
    fun `should create empty variables`() {
        // given
        val testee = createGlobalVariables {}

        // then
        assertDsl(
            testee,
            """
                    {}
                """
        )
    }

    @Test
    fun `should allow multiple types of adding`() {
        // given
        val testee = createGlobalVariables {
            add("key1", "value1")
            add("key2", createGlobalVariable("value2"))
            add("key3") {
                description = "value 3 description"
                options = mutableListOf("t1", "t2")
            }

            add(RunnerSettingsVariables.GIT_CLONE_PATH, "value4")
            add(RunnerSettingsVariables.ARTIFACT_DOWNLOAD_ATTEMPTS, createGlobalVariable("value5"))
            add(RunnerSettingsVariables.CACHE_FALLBACK_KEY) {
                description = "value 6 description"
                expand = false
            }

            "key7" to "value7"
            "key8" to createGlobalVariable("value8")
            "key9" to {
                description = "value 9 description"
            }

            RunnerSettingsVariables.GIT_DEPTH to "value10"
            RunnerSettingsVariables.GIT_STRATEGY to createGlobalVariable("value11")
            RunnerSettingsVariables.GIT_SUBMODULE_STRATEGY to {
                description = "value 12 description"
            }
        }

        // then
        assertDsl(
            testee,
            """
                    "key1":
                      value: "value1"
                    "key2":
                      value: "value2"
                    "key3":
                      description: "value 3 description"
                      options:
                      - "t1"
                      - "t2"
                    "GIT_CLONE_PATH":
                      value: "value4"
                    "ARTIFACT_DOWNLOAD_ATTEMPTS":
                      value: "value5"
                    "CACHE_FALLBACK_KEY":
                      description: "value 6 description"
                      expand: false
                    "key7":
                      value: "value7"
                    "key8":
                      value: "value8"
                    "key9":
                      description: "value 9 description"
                    "GIT_DEPTH":
                      value: "value10"
                    "GIT_STRATEGY":
                      value: "value11"
                    "GIT_SUBMODULE_STRATEGY":
                      description: "value 12 description"
                """
        )
    }

    @Test
    fun `should create direct access`() {
        // given
        val map = mutableMapOf(
            "key1" to createGlobalVariable("value 1"),
            "key2" to createGlobalVariable("value 2")
        )

        val testee = createGlobalVariables {
            variables = map
        }

        // then
        assertDsl(
            testee,
            """
                    "key1":
                      value: "value 1"
                    "key2":
                      value: "value 2"
                """
        )
    }

    @Test
    fun `should override on add`() {
        // given
        val testee = createGlobalVariables {
            add("key1", "value 1")
            add("key1", "value 2")
        }

        // then
        assertDsl(
            testee,
            """
                    "key1":
                      value: "value 2"
                """
        )
    }

    @Test
    fun `should be equal`() {
        // given
        val testee = createGlobalVariables {
            add("one", "one")
            add("two", "two")
        }

        val expected = createGlobalVariables {
            add("one", "one")
            add("two", "two")
        }

        // then
        assertEquals(expected, testee)
    }
}