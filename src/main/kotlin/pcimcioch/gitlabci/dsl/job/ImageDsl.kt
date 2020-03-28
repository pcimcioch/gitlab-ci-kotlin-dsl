package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import pcimcioch.gitlabci.dsl.DslBase

@Serializable
class ImageDsl(
        var name: String? = null
) : DslBase() {
    var entrypoint: MutableList<String>? = null

    fun entrypoint(vararg elements: String) = entrypoint(elements.toList())
    fun entrypoint(elements: Iterable<String>) = ensureEntrypoint().addAll(elements)

    override fun validate(errors: MutableList<String>) {
        addError(errors, isEmpty(name), "[image] name '$name' is incorrect")
    }

    private fun ensureEntrypoint() = entrypoint ?: mutableListOf<String>().also { entrypoint = it }

    companion object {
        init {
            addSerializer(ImageDsl::class, serializer())
        }
    }
}

fun createImage(name: String) = ImageDsl(name)
fun createImage(block: ImageDsl.() -> Unit) = ImageDsl().apply(block)
fun createImage(name: String, block: ImageDsl.() -> Unit) = ImageDsl(name).apply(block)
