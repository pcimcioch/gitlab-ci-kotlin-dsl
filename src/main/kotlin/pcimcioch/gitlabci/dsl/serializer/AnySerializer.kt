package pcimcioch.gitlabci.dsl.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import pcimcioch.gitlabci.dsl.DslBase

object AnySerializer : KSerializer<Any> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Any", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Any) {
        when (value) {
            is String -> encoder.encodeString(value)
            is Boolean -> encoder.encodeBoolean(value)
            is Int -> encoder.encodeInt(value)
            is Long -> encoder.encodeLong(value)
            is Double -> encoder.encodeDouble(value)
            is Float -> encoder.encodeFloat(value)
            is DslBase -> DslBase.DslBaseSerializer.serialize(encoder, value)
            is Map<*, *> -> {
                val mapSerializer = MapSerializer(String.serializer(), AnySerializer)
                @Suppress("UNCHECKED_CAST")
                mapSerializer.serialize(encoder, value as Map<String, Any>)
            }
            is List<*> -> {
                val listSerializer = ListSerializer(AnySerializer)
                @Suppress("UNCHECKED_CAST")
                listSerializer.serialize(encoder, value as List<Any>)
            }
            is Enum<*> -> encoder.encodeString(value.name)
            else -> encoder.encodeString(value.toString())
        }
    }

    override fun deserialize(decoder: Decoder): Any {
        throw IllegalStateException("Deserialization is not supported")
    }
}
