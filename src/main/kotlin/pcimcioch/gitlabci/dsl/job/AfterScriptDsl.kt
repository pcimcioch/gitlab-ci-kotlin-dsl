package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.list
import kotlinx.serialization.builtins.serializer
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@GitlabCiDslMarker
@Serializable(with = AfterScriptDsl.AfterScriptDslSerializer::class)
class AfterScriptDsl : DslBase {
    var commands: MutableList<String> = mutableListOf()

    fun exec(command: String) = commands.add(command)
    operator fun String.unaryPlus() = this@AfterScriptDsl.commands.add(this)

    object AfterScriptDslSerializer : ValueSerializer<AfterScriptDsl, List<String>>(String.serializer().list, AfterScriptDsl::commands)
}

// TODO rename all constructor methods to create*, so they are not easily misused in jobs. Also - use them in all calling methods
fun afterScript(block: AfterScriptDsl.() -> Unit) = AfterScriptDsl().apply(block)
fun afterScript(vararg elements: String) = afterScript(elements.toList())
fun afterScript(elements: Iterable<String>) = AfterScriptDsl().apply { elements.forEach { exec(it) } }
