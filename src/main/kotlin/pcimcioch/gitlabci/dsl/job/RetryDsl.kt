package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.StringRepresentation
import pcimcioch.gitlabci.dsl.serializer.StringRepresentationSerializer

@Serializable
class RetryDsl(
        var max: Int? = null
) : DslBase() {
    @SerialName("when")
    var whenRetry: MutableSet<WhenRetryType>? = null

    fun whenRetry(vararg elements: WhenRetryType) = whenRetry(elements.toList())
    fun whenRetry(elements: Iterable<WhenRetryType>) = ensureWhenRetry().addAll(elements)

    override fun validate(errors: MutableList<String>) {
        addError(errors, max != null && (max!! < 0 || max!! > 2), "[retry] max attempts must be in range [0, 2]")
    }

    private fun ensureWhenRetry() = whenRetry ?: mutableSetOf<WhenRetryType>().also { whenRetry = it }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RetryDsl

        if (max != other.max) return false
        if (whenRetry != other.whenRetry) return false

        return true
    }

    override fun hashCode(): Int {
        var result = max ?: 0
        result = 31 * result + (whenRetry?.hashCode() ?: 0)
        return result
    }

    companion object {
        init {
            addSerializer(RetryDsl::class, serializer())
        }
    }
}

fun createRetry(max: Int? = null, block: RetryDsl.() -> Unit = {}) = RetryDsl(max).apply(block)

@Serializable(with = WhenRetryType.WhenRetryTypeSerializer::class)
enum class WhenRetryType(
        override val stringRepresentation: String
) : StringRepresentation {
    ALWAYS("always"),
    UNKNOWN_FAILURE("unknown_failure"),
    SCRIPT_FAILURE("script_failure"),
    API_FAILURE("api_failure"),
    STUCK_OR_TIMEOUT_FAILURE("stuck_or_timeout_failure"),
    RUNNER_SYSTEM_FAILURE("runner_system_failure"),
    MISSING_DEPENDENCY_FAILURE("missing_dependency_failure"),
    RUNNER_UNSUPPORTED("runner_unsupported"),
    STALE_SCHEDULE("stale_schedule"),
    JOB_EXECUTION_TIMEOUT("job_execution_timeout"),
    ARCHIVED_FAILURE("archived_failure"),
    UNMET_PREREQUISITES("unmet_prerequisites"),
    SCHEDULER_FAILURE("scheduler_failure"),
    DATA_INTEGRITY_FAILURE("data_integrity_failure");

    object WhenRetryTypeSerializer : StringRepresentationSerializer<WhenRetryType>("WhenRetryType")
}