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

    @Serializable(with = BooleanOrDefaultTypeSetSerializer::class)
    var default: Any? = null
        get() = defaultBoolean ?: defaultSet
        private set

    @Serializable(with = BooleanOrStringSetSerializer::class)
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

    // TODO do something with this...
    object BooleanOrStringSetSerializer : KSerializer<Any> {
        override val descriptor: SerialDescriptor = PrimitiveDescriptor("Variables", PrimitiveKind.BOOLEAN)

        override fun serialize(encoder: Encoder, value: Any) {
            if (value is Boolean) {
                Boolean.serializer().serialize(encoder, value)
            }
            if (value is Set<*>) {
                String.serializer().set.serialize(encoder, value as Set<String>)
            }
        }

        override fun deserialize(decoder: Decoder): Any {
            throw IllegalStateException(descriptor.serialName)
        }
    }

    object BooleanOrDefaultTypeSetSerializer : KSerializer<Any> {
        override val descriptor: SerialDescriptor = PrimitiveDescriptor("Default", PrimitiveKind.BOOLEAN)

        override fun serialize(encoder: Encoder, value: Any) {
            if (value is Boolean) {
                Boolean.serializer().serialize(encoder, value)
            }
            if (value is Set<*>) {
                DefaultType.DefaultTypeSerializer.set.serialize(encoder, value as Set<DefaultType>)
            }
        }

        override fun deserialize(decoder: Decoder): Any {
            throw IllegalStateException(descriptor.serialName)
        }
    }
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