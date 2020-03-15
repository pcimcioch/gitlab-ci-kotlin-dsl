package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.list
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.internal.StringDescriptor
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker

@GitlabCiDslMarker
@Serializable
class RetryDsl(var max: Int? = null) : DslBase {
    @SerialName("when")
    var whenRetry: MutableSet<WhenRetryType>? = null

    fun whenRetry(vararg elements: WhenRetryType) = whenRetry(elements.toList())
    fun whenRetry(elements: Iterable<WhenRetryType>) = ensureWhenRetry().addAll(elements)

    override fun validate(errors: MutableList<String>) {
        addError(errors, max != null && (max!! < 0 || max!! > 2), "[retry] max attempts must be in range [0, 2]")
    }

    private fun ensureWhenRetry() = whenRetry ?: mutableSetOf<WhenRetryType>().also { whenRetry = it }
}

fun retry(max: Int) = RetryDsl(max)
fun retry(block: RetryDsl.() -> Unit) = RetryDsl().apply(block)
fun retry(max: Int, block: RetryDsl.() -> Unit) = RetryDsl(max).apply(block)

@Serializable
enum class WhenRetryType(private val value: String) {
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

    @Serializer(forClass = WhenRetryType::class)
    companion object : KSerializer<WhenRetryType> {
        override val descriptor: SerialDescriptor = PrimitiveDescriptor("WhenRetryType", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: WhenRetryType) {
            encoder.encodeString(value.value)
        }
    }
}