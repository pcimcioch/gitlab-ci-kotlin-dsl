package pcimcioch.gitlabci.dsl

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@GitlabCiDslMarker
@Serializable(with = DslBase.DslBaseSerializer::class)
abstract class DslBase {

    open fun validate(errors: MutableList<String>) {}

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

    object DslBaseSerializer: KSerializer<DslBase> {
        override val descriptor = PrimitiveDescriptor("DslBase", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): DslBase {
            throw IllegalStateException(descriptor.serialName)
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