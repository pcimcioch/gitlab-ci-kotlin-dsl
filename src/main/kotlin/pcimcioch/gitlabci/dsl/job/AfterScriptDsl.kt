package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.list
import kotlinx.serialization.builtins.serializer
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker

@GitlabCiDslMarker
@Serializable
class AfterScriptDsl : DslBase {
    var commands: MutableList<String> = mutableListOf()

    fun exec(command: String) = commands.add(command)

    @Serializer(forClass = AfterScriptDsl::class)
    companion object : KSerializer<AfterScriptDsl> {
        override val descriptor: SerialDescriptor = String.serializer().list.descriptor

        override fun serialize(encoder: Encoder, value: AfterScriptDsl) {
            String.serializer().list.serialize(encoder, value.commands)
        }
    }
}

fun afterScript(block: AfterScriptDsl.() -> Unit) = AfterScriptDsl().apply(block)