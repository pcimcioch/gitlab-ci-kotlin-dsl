package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@Serializable
class NeedJobDsl(
    var job: String? = null
) : DslBase() {
    var artifacts: Boolean? = null
    var project: String? = null
    var ref: String? = null

    override fun validate(errors: MutableList<String>) {
        addError(errors, isEmpty(job), "[need job='$job'] job '$job' is incorrect")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NeedJobDsl

        if (job != other.job) return false
        if (artifacts != other.artifacts) return false
        if (project != other.project) return false
        if (ref != other.ref) return false

        return true
    }

    override fun hashCode(): Int {
        var result = job?.hashCode() ?: 0
        result = 31 * result + (artifacts?.hashCode() ?: 0)
        result = 31 * result + (project?.hashCode() ?: 0)
        result = 31 * result + (ref?.hashCode() ?: 0)
        return result
    }

    companion object {
        init {
            addSerializer(NeedJobDsl::class, serializer())
        }
    }
}

fun createNeedJob(job: String? = null, block: NeedJobDsl.() -> Unit = {}) = NeedJobDsl(job).apply(block)
fun createNeedJob(job: JobDsl, block: NeedJobDsl.() -> Unit = {}) = NeedJobDsl(job.name).apply(block)

@Serializable(with = NeedsListDsl.NeedsListDslSerializer::class)
class NeedsListDsl : DslBase() {
    private val needs: MutableList<NeedJobDsl> = mutableListOf()

    fun needJob(job: String? = null, block: NeedJobDsl.() -> Unit = {}) =
        addAndReturn(needs, NeedJobDsl(job).apply(block))

    fun needJob(job: JobDsl, block: NeedJobDsl.() -> Unit = {}) = addAndReturn(needs, NeedJobDsl(job.name).apply(block))
    operator fun NeedJobDsl.unaryPlus() = this@NeedsListDsl.needs.add(this)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, "", needs)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NeedsListDsl

        if (needs != other.needs) return false

        return true
    }

    override fun hashCode(): Int {
        return needs.hashCode()
    }


    object NeedsListDslSerializer :
        ValueSerializer<NeedsListDsl, List<NeedJobDsl>>(ListSerializer(NeedJobDsl.serializer()), NeedsListDsl::needs)

    companion object {
        init {
            addSerializer(NeedsListDsl::class, serializer())
        }
    }
}

fun createNeeds(block: NeedsListDsl.() -> Unit = {}) = NeedsListDsl().apply(block)
fun createNeeds(vararg elements: String, block: NeedsListDsl.() -> Unit = {}) = createNeeds(elements.toList(), block)
fun createNeeds(elements: Iterable<String>, block: NeedsListDsl.() -> Unit = {}) =
    NeedsListDsl().apply { elements.forEach { needJob(it) } }.apply(block)

fun createNeeds(vararg elements: JobDsl, block: NeedsListDsl.() -> Unit = {}) = createNeeds(elements.toList(), block)

@JvmName("createNeedsJob")
fun createNeeds(elements: Iterable<JobDsl>, block: NeedsListDsl.() -> Unit = {}) =
    NeedsListDsl().apply { elements.forEach { needJob(it) } }.apply(block)