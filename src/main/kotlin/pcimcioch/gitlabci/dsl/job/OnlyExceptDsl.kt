package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.StringRepresentation
import pcimcioch.gitlabci.dsl.serializer.StringRepresentationSerializer

@Serializable
class OnlyExceptDsl : DslBase() {
    var refs: MutableSet<String>? = null
    var changes: MutableSet<String>? = null
    var variables: MutableSet<String>? = null
    var kubernetes: KubernetesState? = null

    fun refs(vararg elements: String) = refs(elements.toList())
    fun refs(elements: Iterable<String>) = ensureRefs().addAll(elements)

    fun variables(vararg elements: String) = variables(elements.toList())
    fun variables(elements: Iterable<String>) = ensureVariables().addAll(elements)

    fun changes(vararg elements: String) = changes(elements.toList())
    fun changes(elements: Iterable<String>) = ensureChanges().addAll(elements)

    fun branches() = refs("branches")
    fun tags() = refs("tags")
    fun api() = refs("api")
    fun external() = refs("external")
    fun pipelines() = refs("pipelines")
    fun pushes() = refs("pushes")
    fun schedules() = refs("schedules")
    fun triggers() = refs("triggers")
    fun web() = refs("web")
    fun mergeRequests() = refs("merge_requests")
    fun externalPullRequests() = refs("external_pull_requests")
    fun chat() = refs("chat")
    fun master() = refs("master")

    private fun ensureRefs() = refs ?: mutableSetOf<String>().also { refs = it }
    private fun ensureVariables() = variables ?: mutableSetOf<String>().also { variables = it }
    private fun ensureChanges() = changes ?: mutableSetOf<String>().also { changes = it }

    companion object {
        init {
            addSerializer(OnlyExceptDsl::class, serializer())
        }
    }
}

fun createOnlyExcept(block: OnlyExceptDsl.() -> Unit = {}) = OnlyExceptDsl().apply(block)
fun createOnlyExcept(vararg elements: String, block: OnlyExceptDsl.() -> Unit = {}) = createOnlyExcept(elements.toList(), block)
fun createOnlyExcept(elements: Iterable<String>, block: OnlyExceptDsl.() -> Unit = {}) = OnlyExceptDsl().apply { elements.forEach { refs(it) } }.apply(block)

@Serializable(with = KubernetesState.KubernetesStateSerializer::class)
enum class KubernetesState(
        override val stringRepresentation: String
) : StringRepresentation {
    ACTIVE("active");

    object KubernetesStateSerializer : StringRepresentationSerializer<KubernetesState>("KubernetesState")
}