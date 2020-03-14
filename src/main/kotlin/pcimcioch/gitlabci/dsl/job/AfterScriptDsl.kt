package pcimcioch.gitlabci.dsl.job

import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker

@GitlabCiDslMarker
class AfterScriptDsl : DslBase {
    private val commands: MutableList<String> = mutableListOf()

    fun exec(command: String) = commands.add(command)
}

fun afterScript(block: AfterScriptDsl.() -> Unit) = AfterScriptDsl().apply(block)