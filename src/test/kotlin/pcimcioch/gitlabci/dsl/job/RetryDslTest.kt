package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class RetryDslTest : DslTestBase<RetryDsl>(RetryDsl.serializer()) {

    @Test
    fun `should create from max`() {
        // given
        val testee = createRetry(1)

        // then
        assertDsl(testee,
                """
                    max: 1
                """
        )
    }

    @Test
    fun `should create from block`() {
        // given
        val testee = createRetry {
            max = 2
            whenRetry(WhenRetryType.ALWAYS)
        }

        // then
        assertDsl(testee,
                """
                    max: 2
                    when:
                    - "always"
                """
        )
    }

    @Test
    fun `should create from max and block`() {
        // given
        val testee = createRetry(2) {
            whenRetry(WhenRetryType.ALWAYS)
        }

        // then
        assertDsl(testee,
                """
                    max: 2
                    when:
                    - "always"
                """
        )
    }

    @Test
    fun `should create 0 retry rule`() {
        // given
        val testee = createRetry(0)

        // then
        assertDsl(testee,
                """
                    max: 0
                """
        )
    }

    @Test
    fun `should create 1 retry rule`() {
        // given
        val testee = createRetry(1)

        // then
        assertDsl(testee,
                """
                    max: 1
                """
        )
    }

    @Test
    fun `should create 2 retry rule`() {
        // given
        val testee = createRetry(2)

        // then
        assertDsl(testee,
                """
                    max: 2
                """
        )
    }

    @Test
    fun `should not validate 3 retry rule`() {
        // given
        val testee = createRetry(3)

        // then
        assertDsl(testee,
                """
                    max: 3
                """,
                "[retry] max attempts must be in range [0, 2]"
        )
    }

    @Test
    fun `should not validate -1 retry rule`() {
        // given
        val testee = createRetry(-1)

        // then
        assertDsl(testee,
                """
                    max: -1
                """,
                "[retry] max attempts must be in range [0, 2]"
        )
    }

    @Test
    fun `should allow one when attribute`() {
        // given
        val testee = createRetry {
            whenRetry(WhenRetryType.ALWAYS)
        }

        // then
        assertDsl(testee,
                """
                    when:
                    - "always"
                """
        )
    }

    @Test
    fun `should allow no when attribute`() {
        // given
        val testee = createRetry {
            max = 2
            whenRetry()
        }

        // then
        assertDsl(testee,
                """
                    max: 2
                    when: []
                """
        )
    }

    @Test
    fun `should allow multiple when attribute`() {
        // given
        val testee = createRetry {
            max = 2
            whenRetry(WhenRetryType.UNKNOWN_FAILURE, WhenRetryType.SCRIPT_FAILURE)
        }

        // then
        assertDsl(testee,
                """
                    max: 2
                    when:
                    - "unknown_failure"
                    - "script_failure"
                """
        )
    }

    @Test
    fun `should merge multiple attribute`() {
        // given
        val testee = createRetry {
            max = 2
            whenRetry(WhenRetryType.API_FAILURE, WhenRetryType.STUCK_OR_TIMEOUT_FAILURE)
            whenRetry(listOf(WhenRetryType.RUNNER_SYSTEM_FAILURE, WhenRetryType.MISSING_DEPENDENCY_FAILURE, WhenRetryType.RUNNER_UNSUPPORTED))
        }

        // then
        assertDsl(testee,
                """
                    max: 2
                    when:
                    - "api_failure"
                    - "stuck_or_timeout_failure"
                    - "runner_system_failure"
                    - "missing_dependency_failure"
                    - "runner_unsupported"
                """
        )
    }

    @Test
    fun `should allow direct access to when attribute`() {
        // given
        val testee = createRetry {
            max = 2
            whenRetry = mutableSetOf(WhenRetryType.STALE_SCHEDULE, WhenRetryType.JOB_EXECUTION_TIMEOUT, WhenRetryType.ARCHIVED_FAILURE)
        }

        // then
        assertDsl(testee,
                """
                    max: 2
                    when:
                    - "stale_schedule"
                    - "job_execution_timeout"
                    - "archived_failure"
                """
        )
    }
}