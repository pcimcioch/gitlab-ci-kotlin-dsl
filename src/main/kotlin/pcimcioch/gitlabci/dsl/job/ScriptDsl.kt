package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@Serializable(with = ScriptDsl.ScriptDslSerializer::class)
class ScriptDsl : DslBase() {
    var commands: MutableList<String> = mutableListOf()

    fun exec(command: String) = commands.add(command)
    operator fun String.unaryPlus() = this@ScriptDsl.commands.add(this)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ScriptDsl

        if (commands != other.commands) return false

        return true
    }

    override fun hashCode(): Int {
        return commands.hashCode()
    }

    object ScriptDslSerializer :
        ValueSerializer<ScriptDsl, List<String>>(ListSerializer(String.serializer()), ScriptDsl::commands)

    companion object {
        init {
            addSerializer(ScriptDsl::class, serializer())
        }
    }
}

fun createScript(block: ScriptDsl.() -> Unit = {}) = ScriptDsl().apply(block)
fun createScript(vararg elements: String, block: ScriptDsl.() -> Unit = {}) = createScript(elements.toList(), block)
fun createScript(elements: Iterable<String>, block: ScriptDsl.() -> Unit = {}) =
    ScriptDsl().apply { elements.forEach { exec(it) } }.apply(block)