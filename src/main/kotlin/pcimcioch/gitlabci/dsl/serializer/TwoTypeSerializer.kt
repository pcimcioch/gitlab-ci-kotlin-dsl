package pcimcioch.gitlabci.dsl.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.reflect.KClass

// TODO use union type descriptors?
open class TwoTypeSerializer<S: Any>(
    override val descriptor: SerialDescriptor,
    private val firstClass: KClass<out S>,
    private val firstSerializer: KSerializer<out S>,
    private val secondClass: KClass<out S>,
    private val secondSerializer: KSerializer<out S>
) : KSerializer<S> {

    override fun serialize(encoder: Encoder, value: S) {
        getSerializer(value).serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): S {
        throw IllegalStateException("Deserialization is not supported for this DSL object")
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : S> getSerializer(value: T): KSerializer<T> {
        return when {
            firstClass.isInstance(value) -> firstSerializer as KSerializer<T>
            secondClass.isInstance(value) -> secondSerializer as KSerializer<T>
            else -> throw IllegalStateException("No serializer found for value of type '${value.javaClass.kotlin.qualifiedName}'")
        }
    }
}