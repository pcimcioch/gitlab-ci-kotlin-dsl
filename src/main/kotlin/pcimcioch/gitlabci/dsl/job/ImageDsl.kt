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
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageDsl

        if (name != other.name) return false
        if (entrypoint != other.entrypoint) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (entrypoint?.hashCode() ?: 0)
        return result
    }


    companion object {
        init {
            addSerializer(ImageDsl::class, serializer())
        }
    }
}

fun createImage(name: String? = null, block: ImageDsl.() -> Unit = {}) = ImageDsl(name).apply(block)
