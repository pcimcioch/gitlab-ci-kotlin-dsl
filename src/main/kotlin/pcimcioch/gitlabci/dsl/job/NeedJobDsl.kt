package pcimcioch.gitlabci.dsl.job

import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.DslBase.Companion.addErrors
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.isEmpty

@GitlabCiDslMarker
class NeedJobDsl(var job: String? = null) : DslBase {
    var artifacts: Boolean? = null
    var project: String? = null
    var ref: String? = null

    override fun validate(errors: MutableList<String>) {
        addError(errors, isEmpty(job), "[need job='$job'] job '$job' is incorrect")
    }
}

fun needJob(block: NeedJobDsl.() -> Unit) = NeedJobDsl().apply(block)
fun needJob(job: String) = NeedJobDsl(job)
fun needJob(job: String, block: NeedJobDsl.() -> Unit) = NeedJobDsl(job).apply(block)
fun needJob(job: JobDsl) = NeedJobDsl(job.getName())
fun needJob(job: JobDsl, block: NeedJobDsl.() -> Unit) = NeedJobDsl(job.getName()).apply(block)

@GitlabCiDslMarker
class NeedsListDsl : DslBase {
    private val needs: MutableList<NeedJobDsl> = mutableListOf()

    fun needJob(block: NeedJobDsl.() -> Unit) = needs.add(NeedJobDsl().apply(block))
    fun needJob(job: String) = needs.add(NeedJobDsl(job))
    fun needJob(job: String, block: NeedJobDsl.() -> Unit) = needs.add(NeedJobDsl(job).apply(block))
    fun needJob(job: JobDsl) = needs.add(NeedJobDsl(job.getName()))
    fun needJob(job: JobDsl, block: NeedJobDsl.() -> Unit) = needs.add(NeedJobDsl(job.getName()).apply(block))
    operator fun NeedJobDsl.unaryPlus() = this@NeedsListDsl.needs.add(this)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, needs, "")
    }
}

fun needs(block: NeedsListDsl.() -> Unit) = NeedsListDsl().apply(block)
fun needs(vararg elements: String) = needs(elements.toList())
fun needs(elements: Iterable<String>) = NeedsListDsl().apply { elements.forEach { needJob(it) } }
fun needs(vararg elements: JobDsl) = needs(elements.toList())

@JvmName("needsJob")
fun needs(elements: Iterable<JobDsl>) = NeedsListDsl().apply { elements.forEach { needJob(it) } }