package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class OnlyExceptDslTest : DslTestBase<OnlyExceptDsl>(OnlyExceptDsl.serializer()) {

    @Test
    fun `should create from block`() {
        // given
        val testee = createOnlyExcept {
            kubernetes = KubernetesState.ACTIVE
        }

        // then
        assertDsl(
            testee,
            """
                    kubernetes: "active"
                """
        )
    }

    @Test
    fun `should create from vararg`() {
        // given
        val testee = createOnlyExcept("master", "issue")

        // then
        assertDsl(
            testee,
            """
                    refs:
                    - "master"
                    - "issue"
                """
        )
    }

    @Test
    fun `should create from list`() {
        // given
        val testee = createOnlyExcept(listOf("master", "issue"))

        // then
        assertDsl(
            testee,
            """
                    refs:
                    - "master"
                    - "issue"
                """
        )
    }

    @Test
    fun `should create empty`() {
        // given
        val testee = createOnlyExcept {}

        // then
        assertDsl(
            testee,
            """
                    {}
                """
        )
    }

    @Test
    fun `should create full`() {
        // given
        val testee = createOnlyExcept {
            refs("master", "branches")
            changes("file 1", "file 2")
            variables("\$VAR1", "\$VAR2 == 'test'")
            kubernetes = KubernetesState.ACTIVE
        }

        // then
        assertDsl(
            testee,
            """
                    refs:
                    - "master"
                    - "branches"
                    changes:
                    - "file 1"
                    - "file 2"
                    variables:
                    - "${"$"}VAR1"
                    - "${"$"}VAR2 == 'test'"
                    kubernetes: "active"
                """
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val testee = createOnlyExcept {
            refs = mutableSetOf("master", "branches")
            changes = mutableSetOf("file 1", "file 2")
            variables = mutableSetOf("\$VAR1", "\$VAR2 == 'test'")
        }

        // then
        assertDsl(
            testee,
            """
                    refs:
                    - "master"
                    - "branches"
                    changes:
                    - "file 1"
                    - "file 2"
                    variables:
                    - "${"$"}VAR1"
                    - "${"$"}VAR2 == 'test'"
                """
        )
    }

    @Test
    fun `should allow empty collection`() {
        // given
        val testee = createOnlyExcept {
            refs()
            changes()
            variables()
        }

        // then
        assertDsl(
            testee,
            """
                    refs: []
                    changes: []
                    variables: []
                """
        )
    }

    @Test
    fun `should allow single element collection`() {
        // given
        val testee = createOnlyExcept {
            refs("master")
            changes("file 1")
            variables("\$VAR1")
        }

        // then
        assertDsl(
            testee,
            """
                    refs:
                    - "master"
                    changes:
                    - "file 1"
                    variables:
                    - "${"$"}VAR1"
                """
        )
    }

    @Test
    fun `should allow multi element collections`() {
        // given
        val testee = createOnlyExcept {
            refs("master", "branches")
            changes("file 1", "file 2")
            variables("\$VAR1", "\$VAR2 == 'test'")
        }

        // then
        assertDsl(
            testee,
            """
                    refs:
                    - "master"
                    - "branches"
                    changes:
                    - "file 1"
                    - "file 2"
                    variables:
                    - "${"$"}VAR1"
                    - "${"$"}VAR2 == 'test'"
                """
        )
    }

    @Test
    fun `should merge collections`() {
        // given
        val testee = createOnlyExcept {
            refs("master", "branches")
            refs(listOf("schedules", "web"))
            changes("file 1", "file 2")
            changes(listOf("file 3", "file 4"))
            variables("\$VAR1", "\$VAR2 == 'test'")
            variables(listOf("\$VAR3", "\$VAR4"))
        }

        // then
        assertDsl(
            testee,
            """
                    refs:
                    - "master"
                    - "branches"
                    - "schedules"
                    - "web"
                    changes:
                    - "file 1"
                    - "file 2"
                    - "file 3"
                    - "file 4"
                    variables:
                    - "${"$"}VAR1"
                    - "${"$"}VAR2 == 'test'"
                    - "${"$"}VAR3"
                    - "${"$"}VAR4"
                """
        )
    }

    @Test
    fun `should add preconfigured refs`() {
        // given
        val testee = createOnlyExcept {
            branches()
            tags()
            api()
            external()
            pipelines()
            pushes()
            schedules()
            triggers()
            web()
            mergeRequests()
            externalPullRequests()
            chat()
            master()
        }

        // then
        assertDsl(
            testee,
            """
                    refs:
                    - "branches"
                    - "tags"
                    - "api"
                    - "external"
                    - "pipelines"
                    - "pushes"
                    - "schedules"
                    - "triggers"
                    - "web"
                    - "merge_requests"
                    - "external_pull_requests"
                    - "chat"
                    - "master"
                """
        )
    }

    @Test
    fun `should be equal`() {
        // given
        val testee = createOnlyExcept {
            refs("master", "branches")
            changes("file 1", "file 2")
            variables("\$VAR1", "\$VAR2 == 'test'")
            kubernetes = KubernetesState.ACTIVE
        }

        val expected = createOnlyExcept {
            refs("master", "branches")
            changes("file 1", "file 2")
            variables("\$VAR1", "\$VAR2 == 'test'")
            kubernetes = KubernetesState.ACTIVE
        }

        // then
        assertEquals(expected, testee)
    }
}