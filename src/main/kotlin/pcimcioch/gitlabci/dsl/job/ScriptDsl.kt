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
class ScriptDsl : DslBase {
    var commands: MutableList<String> = mutableListOf()

    fun exec(command: String) = commands.add(command)

    override fun validate(errors: MutableList<String>) {
        addError(errors, commands.isEmpty(), "[script] commands list cannot be empty")
    }

    @Serializer(forClass = ScriptDsl::class)
    companion object : KSerializer<ScriptDsl> {
        override val descriptor: SerialDescriptor = String.serializer().list.descriptor

        override fun serialize(encoder: Encoder, value: ScriptDsl) {
            String.serializer().list.serialize(encoder, value.commands)
        }
    }
}

fun script(block: ScriptDsl.() -> Unit) = ScriptDsl().apply(block)