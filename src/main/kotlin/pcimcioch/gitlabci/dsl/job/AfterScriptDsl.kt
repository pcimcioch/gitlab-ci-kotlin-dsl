package pcimcioch.gitlabci.dsl.job

import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker

@GitlabCiDslMarker
class AfterScriptDsl : DslBase {
    // TODO add getters for every attribute that can be set?
    // TODO or maybe simple collections (collections of non-DSL) should be just public vars?
    private val commands: MutableList<String> = mutableListOf()

    fun exec(command: String) = commands.add(command)
}

fun afterScript(block: AfterScriptDsl.() -> Unit) = AfterScriptDsl().apply(block)