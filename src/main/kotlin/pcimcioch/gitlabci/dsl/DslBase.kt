package pcimcioch.gitlabci.dsl

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.reflect.KClass

@GitlabCiDslMarker
@Serializable(with = DslBase.DslBaseSerializer::class)
abstract class DslBase {

    open fun validate(errors: MutableList<String>) {}

    override fun toString(): String {
        val config = YamlConfiguration(encodeDefaults = false)
        val yaml = Yaml(configuration = config)

        return yaml.encodeToString(serializer(), this)
    }

    companion object {
        private val serializers: MutableMap<KClass<out DslBase>, KSerializer<out DslBase>> = mutableMapOf()

        internal fun addError(errors: MutableList<String>, condition: Boolean?, message: String) {
            if (condition == true) {
                errors.add(message)
            }
        }

        internal fun addErrors(errors: MutableList<String>, messagePrefix: String, objs: Collection<DslBase>) {
            objs.forEach { addErrors(errors, messagePrefix, it) }
        }

        internal fun addErrors(errors: MutableList<String>, messagePrefix: String, vararg obj: DslBase?) {
            val objErrors = mutableListOf<String>()
            obj.forEach { it?.validate(objErrors) }
            objErrors.forEach { errors.add("$messagePrefix$it") }
        }

        internal fun <T> addAndReturn(list: MutableList<T>, element: T): T {
            list.add(element)
            return element
        }

        internal fun isEmpty(tested: String?) = (tested == null || "" == tested)

        internal fun <T : DslBase> addSerializer(clazz: KClass<T>, serializer: KSerializer<T>) {
            serializers[clazz] = serializer
        }
    }

    object DslBaseSerializer : KSerializer<DslBase> {
        override val descriptor = PrimitiveSerialDescriptor("DslBase", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): DslBase {
            throw IllegalStateException("Deserialization is not supported for this DSL object")
        }

        override fun serialize(encoder: Encoder, value: DslBase) {
            getSerializer(value).serialize(encoder, value)
        }

        @Suppress("UNCHECKED_CAST")
        private fun <T : DslBase> getSerializer(value: T): KSerializer<T> {
            for (serializer in serializers) {
                if (serializer.key.isInstance(value)) {
                    return serializer.value as KSerializer<T>
                }
            }

            throw IllegalStateException("No serializer found for value of type '${value.javaClass.kotlin.qualifiedName}'")
        }
    }
}