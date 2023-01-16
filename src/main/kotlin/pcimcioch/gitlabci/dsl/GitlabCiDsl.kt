package pcimcioch.gitlabci.dsl

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import pcimcioch.gitlabci.dsl.default.DefaultDsl
import pcimcioch.gitlabci.dsl.include.IncludeDsl
import pcimcioch.gitlabci.dsl.job.JobDsl
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer
import pcimcioch.gitlabci.dsl.stage.StagesDsl
import pcimcioch.gitlabci.dsl.workflow.WorkflowDsl
import java.io.FileWriter
import java.io.Writer
import kotlin.collections.set

@Serializable(with = GitlabCiDsl.GitlabCiDslSerializer::class)
class GitlabCiDsl : DslBase() {
    private val jobs: MutableList<JobDsl> = mutableListOf()
    private var stages: StagesDsl? = null
    private var default: DefaultDsl? = null
    private var workflow: WorkflowDsl? = null
    private var include: IncludeDsl? = null

    /**
     * creates a Job, adds it to the GitlabCi and returns it.
     */
    fun job(name: String, block: JobDsl.() -> Unit) = addAndReturn(jobs, JobDsl(name)).apply(block)

    /**
     * adds the Job to the GitlabCi
     */
    operator fun JobDsl.unaryPlus() = this@GitlabCiDsl.jobs.add(this)

    fun stages(block: StagesDsl.() -> Unit) = ensureStages().apply(block)
    fun stages(vararg elements: String) = stages(elements.toList())
    fun stages(elements: Iterable<String>) = ensureStages().apply { elements.forEach { stage(it) } }

    fun default(block: DefaultDsl.() -> Unit) = ensureDefault().apply(block)

    fun workflow(block: WorkflowDsl.() -> Unit) = ensureWorkflow().apply(block)

    fun include(block: IncludeDsl.() -> Unit) = ensureInclude().apply(block)
    fun include(vararg elements: String) = include(elements.toList())
    fun include(elements: Iterable<String>) = ensureInclude().apply { elements.forEach { local(it) } }

    fun pages(block: JobDsl.() -> Unit) = job("pages") {
        artifacts("public")
        only("master")
    }.apply(block)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, "", workflow, stages, default, include)
        addErrors(errors, "", jobs)
    }

    private fun ensureStages() = stages ?: StagesDsl().also { stages = it }
    private fun ensureDefault() = default ?: DefaultDsl().also { default = it }
    private fun ensureWorkflow() = workflow ?: WorkflowDsl().also { workflow = it }
    private fun ensureInclude() = include ?: IncludeDsl().also { include = it }

    private fun asMap(): Map<String, DslBase> {
        val map = mutableMapOf<String, DslBase>()

        workflow?.also { map["workflow"] = it }
        include?.also { map["include"] = it }
        stages?.also { map["stages"] = it }
        default?.also { map["default"] = it }
        jobs.forEach { map[it.name] = it }

        return map
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GitlabCiDsl

        if (jobs != other.jobs) return false
        if (stages != other.stages) return false
        if (default != other.default) return false
        if (workflow != other.workflow) return false
        if (include != other.include) return false

        return true
    }

    override fun hashCode(): Int {
        var result = jobs.hashCode()
        result = 31 * result + (stages?.hashCode() ?: 0)
        result = 31 * result + (default?.hashCode() ?: 0)
        result = 31 * result + (workflow?.hashCode() ?: 0)
        result = 31 * result + (include?.hashCode() ?: 0)
        return result
    }

    object GitlabCiDslSerializer : ValueSerializer<GitlabCiDsl, Map<String, DslBase>>(
        MapSerializer(String.serializer(), DslBase.serializer()),
        GitlabCiDsl::asMap
    )

    companion object {
        init {
            addSerializer(GitlabCiDsl::class, serializer())
        }
    }
}

fun gitlabCi(validate: Boolean = true, writer: Writer? = null, block: GitlabCiDsl.() -> Unit) {
    val dsl = GitlabCiDsl().apply(block)

    if (validate) {
        val errors = mutableListOf<String>()
        dsl.validate(errors)
        if (errors.isNotEmpty()) {
            throw IllegalArgumentException(
                errors.joinToString(
                    "\n",
                    "Configuration validation failed\n",
                    "\nValidation can be disabled by calling 'gitlabCi(validate = false) {}'"
                )
            )
        }
    }

    if (writer != null) {
        serializeToYaml(GitlabCiDsl.serializer(), dsl, writer)
    } else {
        FileWriter(".gitlab-ci.yml").use {
            serializeToYaml(GitlabCiDsl.serializer(), dsl, it)
        }
    }
}

fun gitlabCi(validate: Boolean = true, path: String, block: GitlabCiDsl.() -> Unit) {
    FileWriter(path).use {
        gitlabCi(validate, it, block)
    }
}

internal fun <T : DslBase> serializeToYaml(strategy: SerializationStrategy<T>, value: T, writer: Writer) {
    val config = YamlConfiguration(encodeDefaults = false)
    val yaml = Yaml(configuration = config)

    val yamlString = yaml.encodeToString(strategy, value)
    writer.write(yamlString)
}