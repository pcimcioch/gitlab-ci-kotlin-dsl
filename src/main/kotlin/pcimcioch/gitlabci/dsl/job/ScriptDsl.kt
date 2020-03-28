package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.list
import kotlinx.serialization.builtins.serializer
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@Serializable(with = ScriptDsl.ScriptDslSerializer::class)
class ScriptDsl : DslBase {
    var commands: MutableList<String> = mutableListOf()

    fun exec(command: String) = commands.add(command)
    operator fun String.unaryPlus() = this@ScriptDsl.commands.add(this)

    override fun validate(errors: MutableList<String>) {
        addError(errors, commands.isEmpty(), "[script] commands list cannot be empty")
    }

    object ScriptDslSerializer : ValueSerializer<ScriptDsl, List<String>>(String.serializer().list, ScriptDsl::commands)
}

fun createScript(block: ScriptDsl.() -> Unit) = ScriptDsl().apply(block)
fun createScript(vararg elements: String) = createScript(elements.toList())
fun createScript(elements: Iterable<String>) = ScriptDsl().apply { elements.forEach { exec(it) } }