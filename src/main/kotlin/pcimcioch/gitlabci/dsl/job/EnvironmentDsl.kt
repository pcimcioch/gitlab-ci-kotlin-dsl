package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.DslBase.Companion.addErrors
import pcimcioch.gitlabci.dsl.DslBase.Companion.isEmpty
import pcimcioch.gitlabci.dsl.Duration
import pcimcioch.gitlabci.dsl.StringRepresentation
import pcimcioch.gitlabci.dsl.serializer.StringRepresentationSerializer

@Serializable
class EnvironmentDsl(
        var name: String? = null
) : DslBase {
    var url: String? = null

    @SerialName("on_stop")
    var onStop: String? = null
    var action: EnvironmentAction? = null

    @SerialName("auto_stop_in")
    var autoStopIn: Duration? = null
    var kubernetes: KubernetesEnvironmentDsl? = null

    fun onStop(job: JobDsl) {
        onStop = job.name
    }

    fun kubernetes(namespace: String) = ensureKubernetes().apply { this.namespace = namespace }
    fun kubernetes(block: KubernetesEnvironmentDsl.() -> Unit) = ensureKubernetes().apply(block)
    fun kubernetes(namespace: String, block: KubernetesEnvironmentDsl.() -> Unit) = ensureKubernetes().apply { this.namespace = namespace }.apply(block)

    override fun validate(errors: MutableList<String>) {
        val prefix = "[environment name='$name']"

        addError(errors, isEmpty(name), "$prefix name '$name' is incorrect. Cannot be empty")
        addError(errors, name?.matches(Validation.NAME_PATTERN) == false, "$prefix name '$name' is incorrect. Contains forbidden characters")

        addErrors(errors, prefix, kubernetes)
    }

    private fun ensureKubernetes() = kubernetes ?: KubernetesEnvironmentDsl().also { kubernetes = it }

    object Validation {
        val NAME_PATTERN = Regex("[a-zA-Z0-9_{}\$/ -]*")
    }
}

fun createEnvironment(name: String) = EnvironmentDsl(name)
fun createEnvironment(block: EnvironmentDsl.() -> Unit) = EnvironmentDsl().apply(block)
fun createEnvironment(name: String, block: EnvironmentDsl.() -> Unit) = EnvironmentDsl(name).apply(block)

@Serializable
class KubernetesEnvironmentDsl(
        var namespace: String? = null
) : DslBase

fun createKubernetesEnvironment(namespace: String) = KubernetesEnvironmentDsl(namespace)
fun createKubernetesEnvironment(block: KubernetesEnvironmentDsl.() -> Unit) = KubernetesEnvironmentDsl().apply(block)
fun createKubernetesEnvironment(namespace: String, block: KubernetesEnvironmentDsl.() -> Unit) = KubernetesEnvironmentDsl(namespace).apply(block)

@Serializable(with = EnvironmentAction.EnvironmentActionSerializer::class)
enum class EnvironmentAction(
        override val stringRepresentation: String
) : StringRepresentation {
    START("start"),
    STOP("stop");

    object EnvironmentActionSerializer : StringRepresentationSerializer<EnvironmentAction>("EnvironmentAction")
}