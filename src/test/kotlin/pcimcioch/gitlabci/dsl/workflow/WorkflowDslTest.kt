package pcimcioch.gitlabci.dsl.workflow

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase
import pcimcioch.gitlabci.dsl.Duration
import pcimcioch.gitlabci.dsl.job.WhenRunType
import pcimcioch.gitlabci.dsl.job.createRules

internal class WorkflowDslTest : DslTestBase<WorkflowDsl>(WorkflowDsl.serializer()) {

    @Test
    fun `should validate nested objects`() {
        // when
        val testee = WorkflowDsl().apply {
            rules {
                rule {
                    whenRun = WhenRunType.MANUAL
                    startIn = Duration(minutes = 10)
                }
            }
        }

        // then
        assertDsl(testee,
                """
                    rules:
                    - when: "manual"
                      start_in: "10 min"
                """,
                "[workflow][rule] startIn can be used only with when=delayed jobs"
        )
    }

    @Test
    fun `should create empty`() {
        // given
        val testee = WorkflowDsl()

        // then
        assertDsl(testee,
                """
                    {}
                """
        )
    }

    @Test
    fun `should create full`() {
        // given
        val testee = WorkflowDsl().apply {
            rules {
                rule {
                    ifCondition = "condition"
                    whenRun = WhenRunType.MANUAL
                }
                rule {
                    whenRun = WhenRunType.NEVER
                }
            }
        }

        // then
        assertDsl(testee,
                """
                    rules:
                    - if: "condition"
                      when: "manual"
                    - when: "never"
                """
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val rulesDsl = createRules {
            rule {
                ifCondition = "condition"
                whenRun = WhenRunType.MANUAL
            }
            rule {
                whenRun = WhenRunType.NEVER
            }
        }
        val testee = WorkflowDsl().apply {
            rules = rulesDsl
        }

        // then
        assertDsl(testee,
                """
                    rules:
                    - if: "condition"
                      when: "manual"
                    - when: "never"
                """
        )
    }
}
