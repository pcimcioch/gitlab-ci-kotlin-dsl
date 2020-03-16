package pcimcioch.gitlabci.dsl.serializer

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerialDescriptor
import pcimcioch.gitlabci.dsl.StringRepresentation

open class StringRepresentationSerializer<T : StringRepresentation>(
        val name: String
) : KSerializer<T> {
    override val descriptor: SerialDescriptor = PrimitiveDescriptor(name, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeString(value.stringRepresentation)
    }

    override fun deserialize(decoder: Decoder): T {
        throw IllegalStateException(descriptor.serialName)
    }
}