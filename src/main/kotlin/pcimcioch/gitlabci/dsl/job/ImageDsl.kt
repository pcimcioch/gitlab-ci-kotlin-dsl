package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.isEmpty

@GitlabCiDslMarker
@Serializable
class ImageDsl(
        var name: String? = null
) : DslBase {
    var entrypoint: MutableList<String>? = null

    fun entrypoint(vararg elements: String) = entrypoint(elements.toList())
    fun entrypoint(elements: Iterable<String>) = ensureEntrypoint().addAll(elements)

    override fun validate(errors: MutableList<String>) {
        addError(errors, isEmpty(name), "[image] name '$name' is incorrect")
    }

    private fun ensureEntrypoint() = entrypoint ?: mutableListOf<String>().also { entrypoint = it }
}

fun createImage(name: String) = ImageDsl(name)
fun createImage(block: ImageDsl.() -> Unit) = ImageDsl().apply(block)
fun createImage(name: String, block: ImageDsl.() -> Unit) = ImageDsl(name).apply(block)
