package pcimcioch.gitlabci.dsl.stage

import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.isEmpty

@GitlabCiDslMarker
class StageDsl(var name: String? = null) : DslBase {

    companion object {
        const val PRE_STAGE = ".pre"
        const val POST_STAGE = ".post"
    }

    override fun validate(errors: MutableList<String>) {
        addError(errors, isEmpty(name), "[service name='$name'] name '$name' is incorrect")
    }
}

fun stage(name: String) = StageDsl(name)
fun stage(block: StageDsl.() -> Unit) = StageDsl().apply(block)
fun stage(name: String, block: StageDsl.() -> Unit) = StageDsl(name).apply(block)