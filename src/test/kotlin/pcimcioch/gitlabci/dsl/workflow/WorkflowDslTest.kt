package pcimcioch.gitlabci.dsl.workflow

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import pcimcioch.gitlabci.dsl.DslTestBase
import pcimcioch.gitlabci.dsl.Duration
import pcimcioch.gitlabci.dsl.gitlabCi
import pcimcioch.gitlabci.dsl.job.WhenRunType
import pcimcioch.gitlabci.dsl.job.createRules

internal class WorkflowDslTest : DslTestBase() {

    @Test
    fun `should validate nested objects`() {
        // when
        val thrown = assertThrows<IllegalArgumentException> {
            gitlabCi(writer = writer) {
                workflow {
                    rules {
                        rule {
                            whenRun = WhenRunType.MANUAL
                            startIn = Duration(minutes = 10)
                        }
                    }
                }
            }
        }

        // then
        assertThat(thrown).hasMessage("""
            Configuration validation failed
            [workflow][rule] startIn can be used only with when=delayed jobs
            Validation can be disabled by calling 'gitlabCi(validate = false) {}'""".trimIndent())
        assertThat(writer.toString()).isEmpty()
    }

    @Test
    fun `should create empty`() {
        // given
        gitlabCi(writer = writer) {
            workflow {}
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "workflow": {}
                """.trimIndent()
        )
    }

    @Test
    fun `should create full`() {
        // given
        gitlabCi(writer = writer) {
            workflow {
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
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "workflow":
                      rules:
                      - if: "condition"
                        when: "manual"
                      - when: "never"
                """.trimIndent()
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
        gitlabCi(writer = writer) {
            workflow {
                rules = rulesDsl
            }
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "workflow":
                      rules:
                      - if: "condition"
                        when: "manual"
                      - when: "never"
                """.trimIndent()
        )
    }
}
