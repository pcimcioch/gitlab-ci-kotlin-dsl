package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.list
import kotlinx.serialization.builtins.serializer
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker

@GitlabCiDslMarker
@Serializable
class BeforeScriptDsl : DslBase {
    var commands: MutableList<String> = mutableListOf()

    fun exec(command: String) = commands.add(command)

    @Serializer(forClass = BeforeScriptDsl::class)
    companion object : KSerializer<BeforeScriptDsl> {
        override val descriptor: SerialDescriptor = String.serializer().list.descriptor

        override fun serialize(encoder: Encoder, value: BeforeScriptDsl) {
            String.serializer().list.serialize(encoder, value.commands)
        }
    }
}

fun beforeScript(block: BeforeScriptDsl.() -> Unit) = BeforeScriptDsl().apply(block)