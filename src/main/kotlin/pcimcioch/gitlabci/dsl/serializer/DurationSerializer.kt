package pcimcioch.gitlabci.dsl.serializer

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import java.lang.StringBuilder
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

// TODO tests
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

private fun toString(duration: Duration): String {
    val builder = StringBuilder()

    val years = duration.get(ChronoUnit.YEARS)
    if (years > 0) builder.append(years).append(" y")

    val months = duration.get(ChronoUnit.MONTHS)
    if (months > 0) builder.append(months).append(" mo")

    val days = duration.get(ChronoUnit.DAYS)
    if (days > 0) builder.append(days).append(" day")

    val hours = duration.get(ChronoUnit.HOURS)
    if (hours > 0) builder.append(hours).append(" hr")

    val minutes = duration.get(ChronoUnit.MINUTES)
    if (minutes > 0) builder.append(minutes).append(" min")

    val seconds = duration.get(ChronoUnit.SECONDS)
    if (seconds > 0) builder.append(seconds).append(" sec")

    return builder.toString()
}