package pcimcioch.gitlabci.dsl.serializer

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import java.time.Duration

// TODO implement and test
@Serializer(forClass = Duration::class)
object DurationSerializer : KSerializer<Duration> {
    override val descriptor: SerialDescriptor = PrimitiveDescriptor("Duration", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Duration) {
        encoder.encodeString("1")
    }

    override fun deserialize(decoder: Decoder): Duration {
        throw IllegalStateException(descriptor.serialName)
    }
}