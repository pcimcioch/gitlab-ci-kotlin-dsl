package pcimcioch.gitlabci.dsl.include

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer
import pcimcioch.gitlabci.dsl.serializer.AnySerializer

@Serializable(with = InputsDsl.InputsDslSerializer::class)
class InputsDsl : DslBase() {
    val inputs: MutableMap<String, Any> = mutableMapOf()

    fun add(name: String, value: Any) = inputs.put(name, value)
    fun <T : Enum<T>> add(name: T, value: Any) = inputs.put(name.toString(), value)

    infix fun String.to(value: Any) = add(this, value)
    infix fun <T : Enum<T>> T.to(value: Any) = add(this, value)

    override fun validate(errors: MutableList<String>) {
        addError(errors, inputs.isEmpty(), "[inputs] inputs map cannot be empty")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InputsDsl
        return inputs == other.inputs
    }

    override fun hashCode(): Int = inputs.hashCode()

    object InputsDslSerializer : ValueSerializer<InputsDsl, Map<String, Any>>(
        MapSerializer(String.serializer(), AnySerializer),
        InputsDsl::inputs
    )

    companion object {
        init {
            addSerializer(InputsDsl::class, serializer())
        }
    }
}

fun createInputs(block: InputsDsl.() -> Unit = {}) = InputsDsl().apply(block)
fun createInputs(elements: Map<String, Any>, block: InputsDsl.() -> Unit = {}) =
    InputsDsl().apply { elements.forEach { add(it.key, it.value) } }.apply(block)

@JvmName("inputsEnum")
fun <T : Enum<T>> createInputs(elements: Map<T, Any>, block: InputsDsl.() -> Unit = {}) =
    InputsDsl().apply { elements.forEach { add(it.key, it.value) } }.apply(block)

