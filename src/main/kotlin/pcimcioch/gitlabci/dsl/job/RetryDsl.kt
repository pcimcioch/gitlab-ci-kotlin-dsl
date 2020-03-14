package pcimcioch.gitlabci.dsl.job

import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker

@GitlabCiDslMarker
class RetryDsl(var max: Int? = null) : DslBase {
    private val whenRetry: MutableSet<WhenRetryType> = mutableSetOf()

    fun whenRetry(vararg elements: WhenRetryType) = whenRetry(elements.toList())
    fun whenRetry(elements: Iterable<WhenRetryType>) = whenRetry.addAll(elements)

    override fun validate(errors: MutableList<String>) {
        addError(errors, max == null || max!! < 0 || max!! > 2, "[retry] max attempts must be in range [0, 2]")
    }
}

fun retry(max: Int) = RetryDsl(max)
fun retry(block: RetryDsl.() -> Unit) = RetryDsl().apply(block)
fun retry(max: Int, block: RetryDsl.() -> Unit) = RetryDsl(max).apply(block)

enum class WhenRetryType {
    ALWAYS,
    UNKNOWN_FAILURE,
    SCRIPT_FAILURE,
    API_FAILURE,
    STUCK_OR_TIMEOUT_FAILURE,
    RUNNER_SYSTEM_FAILURE,
    MISSING_DEPENDENCY_FAILURE,
    RUNNER_UNSUPPORTED,
    STALE_SCHEDULE,
    JOB_EXECUTION_TIMEOUT,
    ARCHIVED_FAILURE,
    UNMET_PREREQUISITES,
    SCHEDULER_FAILURE,
    DATA_INTEGRITY_FAILURE
}