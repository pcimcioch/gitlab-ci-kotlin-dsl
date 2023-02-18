package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@Serializable(with = AfterScriptDsl.AfterScriptDslSerializer::class)
class AfterScriptDsl : DslBase() {
    var commands: MutableList<String> = mutableListOf()

    fun exec(command: String) = commands.add(command)
    operator fun String.unaryPlus() = this@AfterScriptDsl.commands.add(this)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AfterScriptDsl

        if (commands != other.commands) return false

        return true
    }

    override fun hashCode(): Int {
        return commands.hashCode()
    }

    object AfterScriptDslSerializer :
        ValueSerializer<AfterScriptDsl, List<String>>(ListSerializer(String.serializer()), AfterScriptDsl::commands)

    companion object {
        init {
            addSerializer(AfterScriptDsl::class, serializer())
        }
    }
}

fun createAfterScript(block: AfterScriptDsl.() -> Unit = {}) = AfterScriptDsl().apply(block)
fun createAfterScript(vararg elements: String, block: AfterScriptDsl.() -> Unit = {}) =
    createAfterScript(elements.toList(), block)

fun createAfterScript(elements: Iterable<String>, block: AfterScriptDsl.() -> Unit = {}) =
    AfterScriptDsl().apply { elements.forEach { exec(it) } }.apply(block)
