package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.builtins.set
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.StringRepresentation
import pcimcioch.gitlabci.dsl.serializer.MultiTypeSerializer
import pcimcioch.gitlabci.dsl.serializer.StringRepresentationSerializer

@GitlabCiDslMarker
@Serializable
class InheritDsl : DslBase {
    @Transient
    private var defaultBoolean: Boolean? = null

    @Transient
    private var variablesBoolean: Boolean? = null

    @Transient
    private var defaultSet: MutableSet<DefaultType>? = null

    @Transient
    private var variablesSet: MutableSet<String>? = null

    @Serializable(with = DefaultSerializer::class)
    var default: Any? = null
        get() = defaultBoolean ?: defaultSet
        private set

    @Serializable(with = VariablesSerializer::class)
    var variables: Any? = null
        get() = variablesBoolean ?: variablesSet
        private set

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

    object VariablesSerializer : MultiTypeSerializer(
            PrimitiveDescriptor("Variables", PrimitiveKind.BOOLEAN),
            mapOf(Boolean::class to Boolean.serializer(), Set::class to String.serializer().set))

    object DefaultSerializer : MultiTypeSerializer(
            PrimitiveDescriptor("Default", PrimitiveKind.BOOLEAN),
            mapOf(Boolean::class to Boolean.serializer(), Set::class to DefaultType.DefaultTypeSerializer.set))
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