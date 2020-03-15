package pcimcioch.gitlabci.dsl.job

import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker

@GitlabCiDslMarker
class BeforeScriptDsl : DslBase {
    var commands: MutableList<String> = mutableListOf()

    fun exec(command: String) = commands.add(command)

    override fun validate(errors: MutableList<String>) {
        addError(errors, commands.isEmpty(), "[before_script] commands list cannot be empty")
    }
}

fun beforeScript(block: BeforeScriptDsl.() -> Unit) = BeforeScriptDsl().apply(block)