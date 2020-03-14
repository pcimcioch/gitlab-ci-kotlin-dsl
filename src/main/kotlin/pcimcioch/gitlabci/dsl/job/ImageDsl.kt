package pcimcioch.gitlabci.dsl.job

import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.isEmpty

@GitlabCiDslMarker
class ImageDsl(var name: String? = null) : DslBase {
    private var entrypoint: List<String> = listOf()

    fun entrypoint(vararg elements: String) = entrypoint(elements.toList())
    fun entrypoint(elements: Iterable<String>) {
        entrypoint = elements.toList()
    }

    override fun validate(errors: MutableList<String>) {
        addError(errors, isEmpty(name), "[image] name '$name' is incorrect")
    }
}

fun image(name: String) = ImageDsl(name)
fun image(block: ImageDsl.() -> Unit) = ImageDsl().apply(block)
fun image(name: String, block: ImageDsl.() -> Unit) = ImageDsl(name).apply(block)
