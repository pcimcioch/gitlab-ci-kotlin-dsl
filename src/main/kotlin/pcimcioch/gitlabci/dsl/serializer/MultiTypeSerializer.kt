package pcimcioch.gitlabci.dsl.serializer

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlin.reflect.KClass

open class MultiTypeSerializer<S: Any>(
        override val descriptor: SerialDescriptor,
        private val serializers: Map<KClass<out S>, KSerializer<out S>>
) : KSerializer<S> {

    override fun serialize(encoder: Encoder, value: S) {
        getSerializer(value).serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): S {
        throw IllegalStateException(descriptor.serialName)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : S> getSerializer(value: T): KSerializer<T> {
        for (serializer in serializers) {
            if (serializer.key.isInstance(value)) {
                return serializer.value as KSerializer<T>
            }
        }

        throw IllegalStateException("No serializer found for value of type '${value.javaClass.kotlin.qualifiedName}'")
    }
}