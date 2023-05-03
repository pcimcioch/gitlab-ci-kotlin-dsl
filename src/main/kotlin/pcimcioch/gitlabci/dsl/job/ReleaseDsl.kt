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

    @SerialName("tag_message")
    var tagMessage: String? = null
    var ref: String? = null
    var milestones: MutableSet<String>? = null

    @SerialName("released_at")
    @Serializable(with = InstantSerializer::class)
    var releasedAt: Instant? = null

    fun milestones(vararg elements: String) = milestones(elements.toList())
    fun milestones(elements: Iterable<String>) = ensureMilestones().addAll(elements)

    private fun ensureMilestones() = milestones ?: mutableSetOf<String>().also { milestones = it }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReleaseDsl

        if (tagName != other.tagName) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (tagMessage != other.tagMessage) return false
        if (ref != other.ref) return false
        if (milestones != other.milestones) return false
        if (releasedAt != other.releasedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tagName?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (tagMessage?.hashCode() ?: 0)
        result = 31 * result + (ref?.hashCode() ?: 0)
        result = 31 * result + (milestones?.hashCode() ?: 0)
        result = 31 * result + (releasedAt?.hashCode() ?: 0)
        return result
    }

    object InstantSerializer : ValueSerializer<Instant, String>(String.serializer(), Instant::toString)
    companion object {
        init {
            addSerializer(ReleaseDsl::class, serializer())
        }
    }
}

fun createRelease(tagName: String? = null, block: ReleaseDsl.() -> Unit = {}) = ReleaseDsl(tagName).apply(block)