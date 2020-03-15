package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.StringRepresentation
import pcimcioch.gitlabci.dsl.serializer.StringRepresentationSerializer

@GitlabCiDslMarker
class InheritDsl : DslBase {
    private var defaultBoolean: Boolean? = null
    private var variablesBoolean: Boolean? = null
    private var defaultSet: MutableSet<DefaultType>? = null
    private var variablesSet: MutableSet<String>? = null

    fun default(vararg elements: DefaultType) = default(elements.toList())
    fun default(elements: Iterable<DefaultType>) {
        defaultBoolean = null
        ensureDefaultSet().addAll(elements)
    }

    fun default(value: Boolean) {
        defaultSet = null
        defaultBoolean = value
    }

    fun variables(vararg elements: String) = variables(elements.toList())
    fun variables(elements: Iterable<String>) {
        variablesBoolean = null
        ensureVariablesSet().addAll(elements)
    }

    fun variables(value: Boolean) {
        variablesSet = null
        variablesBoolean = value
    }

    private fun ensureDefaultSet() = defaultSet ?: mutableSetOf<DefaultType>().also { defaultSet = it }
    private fun ensureVariablesSet() = variablesSet ?: mutableSetOf<String>().also { variablesSet = it }
}

fun inherit(block: InheritDsl.() -> Unit) = InheritDsl().apply(block)

@Serializable(with = DefaultType.DefaultTypeSerializer::class)
enum class DefaultType(override val stringRepresentation: String) : StringRepresentation {
    IMAGE("image"),
    SERVICES("services"),
    BEFORE_SCRIPT("before_script"),
    AFTER_SCRIPT("after_script"),
    TAGS("tags"),
    CACHE("cache"),
    ARTIFACTS("artifacts"),
    RETRY("retry"),
    TIMEOUT("timeout"),
    INTERRUPTIBLE("interruptible");

    object DefaultTypeSerializer : StringRepresentationSerializer<DefaultType>("DefaultType")
}