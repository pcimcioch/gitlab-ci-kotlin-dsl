package pcimcioch.gitlabci.dsl.serializer

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlin.reflect.KClass

open class MultiTypeSerializer(
        override val descriptor: SerialDescriptor,
        private val serializers: Map<KClass<*>, KSerializer<*>>
) : KSerializer<Any> {

    override fun serialize(encoder: Encoder, value: Any) {
        getSerializer(value).serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): Any {
        throw IllegalStateException(descriptor.serialName)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> getSerializer(value: T): KSerializer<T> {
        for (serializer in serializers) {
            if (serializer.key.isInstance(value)) {
                return serializer.value as KSerializer<T>
            }
        }

        throw IllegalStateException("No serializer found for value of type '${value.javaClass.kotlin.qualifiedName}'")
    }
}