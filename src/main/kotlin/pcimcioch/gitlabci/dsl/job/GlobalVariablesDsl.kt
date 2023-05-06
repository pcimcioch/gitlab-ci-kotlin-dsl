package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@Serializable(with = GlobalVariablesDsl.GlobalVariablesDslSerializer::class)
class GlobalVariablesDsl : DslBase() {
    var variables: MutableMap<String, GlobalVariableDsl> = mutableMapOf()

    fun add(name: String, block: GlobalVariableDsl.() -> Unit) = variables.put(name, createGlobalVariable(block))
    fun add(name: String, value: GlobalVariableDsl) = variables.put(name, value)
    fun add(name: String, value: Any) = variables.put(name, createGlobalVariable(value.toString()))

    fun <T : Enum<T>> add(name: T, block: GlobalVariableDsl.() -> Unit) = variables.put(name.toString(), createGlobalVariable(block))
    fun <T : Enum<T>> add(name: T, value: GlobalVariableDsl) = variables.put(name.toString(), value)
    fun <T : Enum<T>> add(name: T, value: Any) = variables.put(name.toString(), createGlobalVariable(value.toString()))

    infix fun String.to(block: GlobalVariableDsl.() -> Unit) = add(this, createGlobalVariable(block))
    infix fun String.to(value: GlobalVariableDsl) = add(this, value)
    infix fun String.to(value: Any) = add(this, value)

    infix fun <T : Enum<T>> T.to(block: GlobalVariableDsl.() -> Unit) = add(this, createGlobalVariable(block))
    infix fun <T : Enum<T>> T.to(value: GlobalVariableDsl) = add(this, value)
    infix fun <T : Enum<T>> T.to(value: Any) = add(this, value)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GlobalVariablesDsl

        if (variables != other.variables) return false

        return true
    }

    override fun hashCode(): Int {
        return variables.hashCode()
    }

    object GlobalVariablesDslSerializer : ValueSerializer<GlobalVariablesDsl, Map<String, GlobalVariableDsl>>(
        MapSerializer(String.serializer(), GlobalVariableDsl.serializer()),
        GlobalVariablesDsl::variables
    )

    companion object {
        init {
            addSerializer(GlobalVariablesDsl::class, serializer())
        }
    }
}

fun createGlobalVariables(block: GlobalVariablesDsl.() -> Unit = {}) = GlobalVariablesDsl().apply(block)

@Serializable
class GlobalVariableDsl : DslBase() {
    var description: String? = null
    var value: String? = null
    var options: MutableList<String>? = null
    var expand: Boolean? = null

    fun options(vararg elements: String) = options(elements.toList())
    fun options(elements: Iterable<String>) = ensureOptions().addAll(elements)

    private fun ensureOptions() = options ?: mutableListOf<String>().also { options = it }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GlobalVariableDsl

        if (description != other.description) return false
        if (value != other.value) return false
        if (options != other.options) return false
        if (expand != other.expand) return false

        return true
    }

    override fun hashCode(): Int {
        var result = description?.hashCode() ?: 0
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + (options?.hashCode() ?: 0)
        result = 31 * result + (expand?.hashCode() ?: 0)
        return result
    }

    companion object {
        init {
            addSerializer(GlobalVariableDsl::class, serializer())
        }
    }
}

fun createGlobalVariable(block: GlobalVariableDsl.() -> Unit = {}) = GlobalVariableDsl().apply(block)
fun createGlobalVariable(value: String, block: GlobalVariableDsl.() -> Unit = {}) = GlobalVariableDsl().apply {
    this.value = value
}.apply(block)
