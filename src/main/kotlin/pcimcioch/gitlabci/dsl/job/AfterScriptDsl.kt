package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
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

    object AfterScriptDslSerializer : ValueSerializer<AfterScriptDsl, List<String>>(String.serializer().list, AfterScriptDsl::commands)
}

fun afterScript(block: AfterScriptDsl.() -> Unit) = AfterScriptDsl().apply(block)