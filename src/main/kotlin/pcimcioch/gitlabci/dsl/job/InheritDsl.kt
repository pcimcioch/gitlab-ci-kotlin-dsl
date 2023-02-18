package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.StringRepresentation
import pcimcioch.gitlabci.dsl.serializer.StringRepresentationSerializer
import pcimcioch.gitlabci.dsl.serializer.TwoTypeSerializer

@Serializable
class InheritDsl : DslBase() {
    @Transient
    private var defaultBoolean: Boolean? = null

    @Transient
    private var variablesBoolean: Boolean? = null

    @Transient
    private var defaultSet: MutableSet<InheritDefaultType>? = null

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

    fun default(vararg elements: InheritDefaultType) = default(elements.toList())
    fun default(elements: Iterable<InheritDefaultType>) {
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

    private fun ensureDefaultSet() = defaultSet ?: mutableSetOf<InheritDefaultType>().also { defaultSet = it }
    private fun ensureVariablesSet() = variablesSet ?: mutableSetOf<String>().also { variablesSet = it }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InheritDsl

        if (default != other.default) return false
        if (variables != other.variables) return false

        return true
    }

    override fun hashCode(): Int {
        var result = default?.hashCode() ?: 0
        result = 31 * result + (variables?.hashCode() ?: 0)
        return result
    }


    object VariablesSerializer : TwoTypeSerializer<Any>(
        PrimitiveSerialDescriptor("Variables", PrimitiveKind.BOOLEAN),
        Boolean::class, Boolean.serializer(),
        Set::class, SetSerializer(String.serializer())
    )

    object DefaultSerializer : TwoTypeSerializer<Any>(
        PrimitiveSerialDescriptor("Default", PrimitiveKind.BOOLEAN),
        Boolean::class, Boolean.serializer(),
        Set::class, SetSerializer(InheritDefaultType.InheritDefaultTypeSerializer)
    )

    companion object {
        init {
            addSerializer(InheritDsl::class, serializer())
        }
    }
}

fun createInherit(block: InheritDsl.() -> Unit = {}) = InheritDsl().apply(block)

@Serializable(with = InheritDefaultType.InheritDefaultTypeSerializer::class)
enum class InheritDefaultType(
    override val stringRepresentation: String
) : StringRepresentation {
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

    object InheritDefaultTypeSerializer : StringRepresentationSerializer<InheritDefaultType>("InheritDefaultType")
}