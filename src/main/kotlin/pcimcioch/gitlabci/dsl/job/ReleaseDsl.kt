package pcimcioch.gitlabci.dsl.job

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@Serializable
class ReleaseDsl(
        @SerialName("tag_name")
        var tagName: String? = null
) : DslBase() {
    var name: String? = null
    var description: String? = null
    var ref: String? = null
    var milestones: MutableSet<String>? = null
    @SerialName("released_at")
    @Serializable(with = InstantSerializer::class)
    var releasedAt: Instant? = null

    fun milestones(vararg elements: String) = milestones(elements.toList())
    fun milestones(elements: Iterable<String>) = ensureMilestones().addAll(elements)

    private fun ensureMilestones() = milestones ?: mutableSetOf<String>().also { milestones = it }

    object InstantSerializer : ValueSerializer<Instant, String>(String.serializer(), Instant::toString)
    companion object {
        init {
            addSerializer(ReleaseDsl::class, serializer())
        }
    }
}

fun createRelease(tagName: String? = null, block: ReleaseDsl.() -> Unit = {}) = ReleaseDsl(tagName).apply(block)