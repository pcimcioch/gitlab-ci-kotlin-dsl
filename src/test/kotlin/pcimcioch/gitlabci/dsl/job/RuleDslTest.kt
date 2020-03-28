package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase
import pcimcioch.gitlabci.dsl.Duration
import pcimcioch.gitlabci.dsl.gitlabCi

internal class RuleDslTest : DslTestBase() {

    @Test
    fun `should create empty rule`() {
        // given
        val testee = createRule {}

        // then
        assertDsl(RuleDsl.serializer(), testee,
                """
                    {}
                """.trimIndent()
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
        }

        // then
        assertDsl(RuleDsl.serializer(), testee,
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
                """.trimIndent()
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
        assertDsl(RuleDsl.serializer(), testee,
                """
                    if: "condition"
                    when: "always"
                    start_in: "10 min"
                """.trimIndent(),
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
        assertDsl(RuleDsl.serializer(), testee,
                """
                    if: "condition"
                    changes: []
                    exists: []
                """.trimIndent()
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
        assertDsl(RuleDsl.serializer(), testee,
                """
                    if: "condition"
                    changes:
                    - "file 1"
                    exists:
                    - "file 2"
                """.trimIndent()
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
        assertDsl(RuleDsl.serializer(), testee,
                """
                    if: "condition"
                    changes:
                    - "file 1"
                    - "file 2"
                    exists:
                    - "file 3"
                    - "file 4"
                """.trimIndent()
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
        assertDsl(RuleDsl.serializer(), testee,
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
                """.trimIndent()
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val testee = createRule {
            ifCondition = "condition"
            changes = mutableSetOf("file 1", "file 2")
            exists = mutableSetOf("file 3", "file 4")
        }

        // then
        assertDsl(RuleDsl.serializer(), testee,
                """
                    if: "condition"
                    changes:
                    - "file 1"
                    - "file 2"
                    exists:
                    - "file 3"
                    - "file 4"
                """.trimIndent()
        )
    }
}

internal class RuleListDslTest : DslTestBase() {

    @Test
    fun `should create empty`() {
        // given
        val testee = createRules {}

        // then
        assertDsl(RuleListDsl.serializer(), testee,
                """
                    []
                """.trimIndent()
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
        assertDsl(RuleListDsl.serializer(), testee,
                """
                    - if: "condition"
                      when: "manual"
                    - when: "never"
                """.trimIndent()
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
        assertDsl(RuleListDsl.serializer(), testee,
                """
                    - when: "manual"
                      start_in: "10 min"
                """.trimIndent(),
                "[rule] startIn can be used only with when=delayed jobs"
        )
    }
}