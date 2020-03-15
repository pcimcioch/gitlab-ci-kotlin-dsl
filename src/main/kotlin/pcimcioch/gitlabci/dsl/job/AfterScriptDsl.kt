package pcimcioch.gitlabci.dsl.job

import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker

@GitlabCiDslMarker
class AfterScriptDsl : DslBase {
    var commands: MutableList<String> = mutableListOf()

    fun exec(command: String) = commands.add(command)

    override fun validate(errors: MutableList<String>) {
        addError(errors, commands.isEmpty(), "[after_script] commands list cannot be empty")
    }
}

fun afterScript(block: AfterScriptDsl.() -> Unit) = AfterScriptDsl().apply(block)