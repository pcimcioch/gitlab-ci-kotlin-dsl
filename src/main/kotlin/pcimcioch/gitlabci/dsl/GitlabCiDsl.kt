package pcimcioch.gitlabci.dsl

import pcimcioch.gitlabci.dsl.DslBase.Companion.addErrors
import pcimcioch.gitlabci.dsl.job.JobDsl
import pcimcioch.gitlabci.dsl.stage.StageDsl

@GitlabCiDslMarker
class GitlabCiDsl : DslBase {
    private val jobs: MutableList<JobDsl> = mutableListOf()
    private val stages: MutableList<StageDsl> = mutableListOf()

    fun job(name: String) = addAndReturn(jobs, JobDsl(name))
    fun job(block: JobDsl.() -> Unit) = addAndReturn(jobs, JobDsl()).apply(block)
    fun job(name: String, block: JobDsl.() -> Unit) = addAndReturn(jobs, JobDsl(name)).apply(block)
    operator fun JobDsl.unaryPlus() = this@GitlabCiDsl.jobs.add(this)

    fun stage(name: String) = addAndReturn(stages, StageDsl(name))
    fun stage(block: StageDsl.() -> Unit) = addAndReturn(stages, StageDsl().apply(block))
    fun stage(name: String, block: StageDsl.() -> Unit) = addAndReturn(stages, StageDsl(name).apply(block))
    operator fun StageDsl.unaryPlus() = this@GitlabCiDsl.stages.add(this)

    fun stages(vararg elements: String) = stages(elements.toList())
    fun stages(elements: Iterable<String>) = elements.forEach {stages.add(StageDsl(it))}

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, stages, "")
        addErrors(errors, jobs, "")
    }
}

fun gitlabCi(block: GitlabCiDsl.() -> Unit): GitlabCiDsl {
    val dsl = GitlabCiDsl()
    dsl.apply(block)

    return dsl
}