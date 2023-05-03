package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase
import pcimcioch.gitlabci.dsl.Duration

internal class RuleDslTest : DslTestBase<RuleDsl>(RuleDsl.serializer()) {

    @Test
    fun `should create empty rule`() {
        // given
        val testee = createRule {}

        // then
        assertDsl(
            testee,
            """
                    {}
                """
        )
    }

    @Test
    fun `should create full rule`() {
        // given
        val testee = createRule {
            ifCondition = "condition"
            changes("file 1", "file 2")
            exists("file 3", "file 4")
            allowFailure = true
            whenRun = WhenRunType.DELAYED
            startIn = Duration(minutes = 10)
            variables {
                "TEST" to "value"
            }
        }

        // then
        assertDsl(
            testee,
            """
                    if: "condition"
                    changes:
                    - "file 1"
                    - "file 2"
                    exists:
                    - "file 3"
                    - "file 4"
                    allow_failure: true
                    when: "delayed"
                    start_in: "10 min"
                    variables:
                      "TEST": "value"
                """
        )
    }

    @Test
    fun `should validate startIn`() {
        // given
        val testee = createRule {
            ifCondition = "condition"
            whenRun = WhenRunType.ALWAYS
            startIn = Duration(minutes = 10)
        }

        // then
        assertDsl(
            testee,
            """
                    if: "condition"
                    when: "always"
                    start_in: "10 min"
                """,
            "[rule] startIn can be used only with when=delayed jobs"
        )
    }

    @Test
    fun `should allow empty collection`() {
        // given
        val testee = createRule {
            ifCondition = "condition"
            changes()
            exists()
        }

        // then
        assertDsl(
            testee,
            """
                    if: "condition"
                    changes: []
                    exists: []
                """
        )
    }

    @Test
    fun `should allow single element collection`() {
        // given
        val testee = createRule {
            ifCondition = "condition"
            changes("file 1")
            exists("file 2")
        }

        // then
        assertDsl(
            testee,
            """
                    if: "condition"
                    changes:
                    - "file 1"
                    exists:
                    - "file 2"
                """
        )
    }

    @Test
    fun `should allow multiple element collection`() {
        // given
        val testee = createRule {
            ifCondition = "condition"
            changes("file 1", "file 2")
            exists("file 3", "file 4")
        }

        // then
        assertDsl(
            testee,
            """
                    if: "condition"
                    changes:
                    - "file 1"
                    - "file 2"
                    exists:
                    - "file 3"
                    - "file 4"
                """
        )
    }

    @Test
    fun `should merge collections`() {
        // given
        val testee = createRule {
            ifCondition = "condition"
            changes("file 1", "file 2")
            changes(listOf("file 3", "file 4"))
            exists("file 5", "file 6")
            exists(listOf("file 7", "file 8"))
        }

        // then
        assertDsl(
            testee,
            """
                    if: "condition"
                    changes:
                    - "file 1"
                    - "file 2"
                    - "file 3"
                    - "file 4"
                    exists:
                    - "file 5"
                    - "file 6"
                    - "file 7"
                    - "file 8"
                """
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val testee = createRule {
            ifCondition = "condition"
            changes = mutableSetOf("file 1", "file 2")
            exists = mutableSetOf("file 3", "file 4")
            variables = createVariables {
                "TEST" to "value"
            }
        }

        // then
        assertDsl(
            testee,
            """
                    if: "condition"
                    changes:
                    - "file 1"
                    - "file 2"
                    exists:
                    - "file 3"
                    - "file 4"
                    variables:
                      "TEST": "value"
                """
        )
    }

    @Test
    fun `should be equal`() {
        // given
        val testee = createRule {
            ifCondition = "condition"
            changes("one", "two")
            exists("one", "two")
            whenRun = WhenRunType.ALWAYS
            startIn = Duration(minutes = 10)
        }

        val expected = createRule {
            ifCondition = "condition"
            changes("one", "two")
            exists("one", "two")
            whenRun = WhenRunType.ALWAYS
            startIn = Duration(minutes = 10)
        }

        // then
        assertEquals(expected, testee)
    }
}

internal class RuleListDslTest : DslTestBase<RuleListDsl>(RuleListDsl.serializer()) {

    @Test
    fun `should create empty`() {
        // given
        val testee = createRules {}

        // then
        assertDsl(
            testee,
            """
                    []
                """
        )
    }

    @Test
    fun `should create rules in multiple ways`() {
        // given
        val neverRule = createRule {
            whenRun = WhenRunType.NEVER
        }
        val testee = createRules {
            rule {
                ifCondition = "condition"
                whenRun = WhenRunType.MANUAL
            }
            +neverRule
        }

        // then
        assertDsl(
            testee,
            """
                    - if: "condition"
                      when: "manual"
                    - when: "never"
                """
        )
    }

    @Test
    fun `should validate rule`() {
        // given
        val testee = createRules {
            rule {
                whenRun = WhenRunType.MANUAL
                startIn = Duration(minutes = 10)
            }
        }

        // then
        assertDsl(
            testee,
            """
                    - when: "manual"
                      start_in: "10 min"
                """,
            "[rule] startIn can be used only with when=delayed jobs"
        )
    }

    @Test
    fun `should be equal`() {
        // given
        val testee = createRules {
            rule {
                whenRun = WhenRunType.MANUAL
                startIn = Duration(minutes = 10)
            }
        }

        val expected = createRules {
            rule {
                whenRun = WhenRunType.MANUAL
                startIn = Duration(minutes = 10)
            }
        }

        // then
        assertEquals(expected, testee)
    }
}