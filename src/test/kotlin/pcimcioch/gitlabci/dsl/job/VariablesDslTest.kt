package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class VariablesDslTest : DslTestBase<VariablesDsl>(VariablesDsl.serializer()) {

    @Test
    fun `should create from block`() {
        // given
        val testee = createVariables {
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
        val testee = createVariables(mapOf("key1" to "value 1", "key2" to "value 2"))

        // then
        assertDsl(
            testee,
            """
                    "key1": "value 1"
                    "key2": "value 2"
                """
        )
    }

    @Test
    fun `should create from map and block`() {
        // given
        val testee = createVariables(mapOf("key1" to "value 1", "key2" to "value 2")) {
            add("key3", "value 3")
        }

        // then
        assertDsl(
            testee,
            """
                    "key1": "value 1"
                    "key2": "value 2"
                    "key3": "value 3"
                """
        )
    }

    @Test
    fun `should create from enum map`() {
        // given
        val testee = createVariables(
            mapOf(
                RunnerSettingsVariables.GIT_CLONE_PATH to "1",
                RunnerSettingsVariables.RESTORE_CACHE_ATTEMPTS to "3"
            )
        )

        // then
        assertDsl(
            testee,
            """
                    "GIT_CLONE_PATH": "1"
                    "RESTORE_CACHE_ATTEMPTS": "3"
                """
        )
    }

    @Test
    fun `should create from enum map and block`() {
        // given
        val testee = createVariables(
            mapOf(
                RunnerSettingsVariables.GIT_CLONE_PATH to "1",
                RunnerSettingsVariables.RESTORE_CACHE_ATTEMPTS to "3"
            )
        ) {
            add(RunnerSettingsVariables.GET_SOURCES_ATTEMPTS, 4)
        }

        // then
        assertDsl(
            testee,
            """
                    "GIT_CLONE_PATH": "1"
                    "RESTORE_CACHE_ATTEMPTS": "3"
                    "GET_SOURCES_ATTEMPTS": "4"
                """
        )
    }

    @Test
    fun `should create empty variables`() {
        // given
        val testee = createVariables {}

        // then
        assertDsl(
            testee,
            """
                    {}
                """,
            "[variables] variables map cannot be empty"
        )
    }

    @Test
    fun `should create one element variables`() {
        // given
        val testee = createVariables {
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
    fun `should create multiple element variables`() {
        // given
        val testee = createVariables {
            add("key1", "value 1")
            add("key2", "value 2")
        }

        // then
        assertDsl(
            testee,
            """
                    "key1": "value 1"
                    "key2": "value 2"
                """
        )
    }

    @Test
    fun `should allow multiple types of adding`() {
        // given
        val testee = createVariables {
            add("key1", "value 1")
            add(RunnerSettingsVariables.GIT_CLONE_PATH, "value 2")
            "key3" to "value 3"
            RunnerSettingsVariables.GIT_CHECKOUT to "true"
        }

        // then
        assertDsl(
            testee,
            """
                    "key1": "value 1"
                    "GIT_CLONE_PATH": "value 2"
                    "key3": "value 3"
                    "GIT_CHECKOUT": "true"
                """
        )
    }

    @Test
    fun `should create direct access`() {
        // given
        val map = mutableMapOf(
            "key1" to "value 1",
            "key2" to "value 2"
        )

        val testee = createVariables {
            variables = map
        }

        // then
        assertDsl(
            testee,
            """
                    "key1": "value 1"
                    "key2": "value 2"
                """
        )
    }

    @Test
    fun `should override on add`() {
        // given
        val testee = createVariables {
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
    fun `should set runner settings`() {
        // given
        val testee = createVariables {
            gitStrategy(GitStrategyType.CLONE)
            gitSubmoduleStrategy(GitSubmoduleStrategyType.RECURSIVE)
            gitCheckout(true)
            gitClean("-ffdx")
            getResourcesAttempts(3)
            artifactDownloadAttempts(2)
            restoreCacheAttempts(1)
            gitDepth(4)
            gitClonePath("path/to/dir")
            gitFetchExtraFlags("extra")
            executorJobSectionAttempts(5)
            cacheFallbackKey("key")
        }

        // then
        assertDsl(
            testee,
            """
                    "GIT_STRATEGY": "clone"
                    "GIT_SUBMODULE_STRATEGY": "recursive"
                    "GIT_CHECKOUT": "true"
                    "GIT_CLEAN_FLAGS": "-ffdx"
                    "GET_SOURCES_ATTEMPTS": "3"
                    "ARTIFACT_DOWNLOAD_ATTEMPTS": "2"
                    "RESTORE_CACHE_ATTEMPTS": "1"
                    "GIT_DEPTH": "4"
                    "GIT_CLONE_PATH": "path/to/dir"
                    "GIT_FETCH_EXTRA_FLAGS": "extra"
                    "EXECUTOR_JOB_SECTION_ATTEMPTS": "5"
                    "CACHE_FALLBACK_KEY": "key"
                """
        )
    }

    @Test
    fun `should set git no clean settings`() {
        // given
        val testee = createVariables {
            disableGitClean()
        }

        // then
        assertDsl(
            testee,
            """
                    "GIT_CLEAN_FLAGS": "none"
                """
        )
    }

    @Test
    fun `should allow adding any objects`() {
        // given
        val testee = createVariables {
            add("key1", "string")
            add("key2", 15)
            add("key3", false)
            add("key4", object : Any() {
                override fun toString() = "test"
            })
        }

        // then
        assertDsl(
            testee,
            """
                    "key1": "string"
                    "key2": "15"
                    "key3": "false"
                    "key4": "test"
                """
        )
    }

    @Test
    fun `should be equal`() {
        // given
        val testee = createVariables {
            add("one", "one")
            add("two", "two")
        }

        val expected = createVariables {
            add("one", "one")
            add("two", "two")
        }

        // then
        assertEquals(expected, testee)
    }
}