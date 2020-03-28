package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.list
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addAndReturn
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.DslBase.Companion.addErrors
import pcimcioch.gitlabci.dsl.DslBase.Companion.isEmpty
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@GitlabCiDslMarker
@Serializable
class NeedJobDsl(
        var job: String? = null
) : DslBase {
    var artifacts: Boolean? = null
    var project: String? = null
    var ref: String? = null

    override fun validate(errors: MutableList<String>) {
        addError(errors, isEmpty(job), "[need job='$job'] job '$job' is incorrect")
    }
}

fun createNeedJob(block: NeedJobDsl.() -> Unit) = NeedJobDsl().apply(block)
fun createNeedJob(job: String) = NeedJobDsl(job)
fun createNeedJob(job: String, block: NeedJobDsl.() -> Unit) = NeedJobDsl(job).apply(block)
fun createNeedJob(job: JobDsl) = NeedJobDsl(job.name)
fun createNeedJob(job: JobDsl, block: NeedJobDsl.() -> Unit) = NeedJobDsl(job.name).apply(block)

@GitlabCiDslMarker
@Serializable(with = NeedsListDsl.NeedsListDslSerializer::class)
class NeedsListDsl : DslBase {
    private val needs: MutableList<NeedJobDsl> = mutableListOf()

    fun needJob(block: NeedJobDsl.() -> Unit) = addAndReturn(needs, NeedJobDsl().apply(block))
    fun needJob(job: String) = addAndReturn(needs, NeedJobDsl(job))
    fun needJob(job: String, block: NeedJobDsl.() -> Unit) = addAndReturn(needs, NeedJobDsl(job).apply(block))
    fun needJob(job: JobDsl) = addAndReturn(needs, NeedJobDsl(job.name))
    fun needJob(job: JobDsl, block: NeedJobDsl.() -> Unit) = addAndReturn(needs, NeedJobDsl(job.name).apply(block))
    operator fun NeedJobDsl.unaryPlus() = this@NeedsListDsl.needs.add(this)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, "", needs)
    }

    object NeedsListDslSerializer : ValueSerializer<NeedsListDsl, List<NeedJobDsl>>(NeedJobDsl.serializer().list, NeedsListDsl::needs)
}

fun createNeeds(block: NeedsListDsl.() -> Unit) = NeedsListDsl().apply(block)
fun createNeeds(vararg elements: String) = createNeeds(elements.toList())
fun createNeeds(elements: Iterable<String>) = NeedsListDsl().apply { elements.forEach { needJob(it) } }
fun createNeeds(vararg elements: JobDsl) = createNeeds(elements.toList())

@JvmName("createNeedsJob")
fun createNeeds(elements: Iterable<JobDsl>) = NeedsListDsl().apply { elements.forEach { needJob(it) } }