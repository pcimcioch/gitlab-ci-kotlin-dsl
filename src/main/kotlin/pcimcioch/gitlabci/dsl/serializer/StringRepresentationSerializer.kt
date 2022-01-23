package pcimcioch.gitlabci.dsl.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import pcimcioch.gitlabci.dsl.StringRepresentation

open class StringRepresentationSerializer<T : StringRepresentation>(
        val name: String
) : KSerializer<T> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(name, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeString(value.stringRepresentation)
    }

    override fun deserialize(decoder: Decoder): T {
        throw IllegalStateException("Deserialization is not supported for this DSL object")
    }
}