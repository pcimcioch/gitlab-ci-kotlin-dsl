package pcimcioch.gitlabci.dsl.serializer

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor

open class ValueSerializer<T, K>(private val valueSerializer: KSerializer<K>, private val transform: (T) -> K) : KSerializer<T> {
    override val descriptor: SerialDescriptor = valueSerializer.descriptor

    override fun serialize(encoder: Encoder, value: T) {
        valueSerializer.serialize(encoder, transform.invoke(value))
    }

    override fun deserialize(decoder: Decoder): T {
        throw IllegalStateException(descriptor.serialName)
    }

}