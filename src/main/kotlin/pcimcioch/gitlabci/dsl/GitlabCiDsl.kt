package pcimcioch.gitlabci.dsl

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import pcimcioch.gitlabci.dsl.DslBase.Companion.addErrors
import pcimcioch.gitlabci.dsl.default.DefaultDsl
import pcimcioch.gitlabci.dsl.job.JobDsl
import pcimcioch.gitlabci.dsl.serializer.MultiTypeSerializer
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer
import pcimcioch.gitlabci.dsl.stage.StagesDsl
import pcimcioch.gitlabci.dsl.workflow.WorkflowDsl
import java.io.FileWriter
import java.io.Writer
import kotlin.collections.set

@GitlabCiDslMarker
@Serializable(with = GitlabCiDsl.GitlabCiDslSerializer::class)
class GitlabCiDsl : DslBase {
    private val jobs: MutableList<JobDsl> = mutableListOf()
    private var stages: StagesDsl? = null
    private var default: DefaultDsl? = null
    private var workflow: WorkflowDsl? = null

    fun job(name: String, block: JobDsl.() -> Unit) = addAndReturn(jobs, JobDsl(name)).apply(block)
    operator fun JobDsl.unaryPlus() = this@GitlabCiDsl.jobs.add(this)

    fun stages(block: StagesDsl.() -> Unit) = ensureStages().apply(block)
    fun stages(vararg elements: String) = stages(elements.toList())
    fun stages(elements: Iterable<String>) = ensureStages().apply { elements.forEach { stage(it) } }

    fun default(block: DefaultDsl.() -> Unit) = ensureDefault().apply(block)

    fun workflow(block: WorkflowDsl.() -> Unit) = ensureWorkflow().apply(block)

    fun pages(block: JobDsl.() -> Unit) = job("pages") {
        artifacts("public")
        only("master")
    }.apply(block)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, workflow, "")
        addErrors(errors, stages, "")
        addErrors(errors, default, "")
        addErrors(errors, jobs, "")
    }

    private fun ensureStages() = stages ?: StagesDsl().also { stages = it }
    private fun ensureDefault() = default ?: DefaultDsl().also { default = it }
    private fun ensureWorkflow() = workflow ?: WorkflowDsl().also { workflow = it }

    private fun asMap(): Map<String, DslBase> {
        val map = mutableMapOf<String, DslBase>()

        workflow?.also { map["workflow"] = it }
        stages?.also { map["stages"] = it }
        default?.also { map["default"] = it }
        jobs.forEach { map[it.name] = it }

        return map
    }

    object ElementSerializer : MultiTypeSerializer<DslBase>(
            PrimitiveDescriptor("Elements", PrimitiveKind.STRING),
            mapOf(
                    JobDsl::class to JobDsl.serializer(),
                    StagesDsl::class to StagesDsl.serializer(),
                    DefaultDsl::class to DefaultDsl.serializer(),
                    WorkflowDsl::class to WorkflowDsl.serializer()))

    object GitlabCiDslSerializer : ValueSerializer<GitlabCiDsl, Map<String, DslBase>>(MapSerializer(String.serializer(), ElementSerializer), GitlabCiDsl::asMap)
}

fun gitlabCi(validate: Boolean = true, writer: Writer? = null, block: GitlabCiDsl.() -> Unit) {
    val dsl = GitlabCiDsl().apply(block)

    if (validate) {
        val errors = mutableListOf<String>()
        dsl.validate(errors)
        if (errors.isNotEmpty()) {
            throw IllegalArgumentException(errors.joinToString(
                    "\n",
                    "Configuration validation failed\n",
                    "\nValidation can be disabled by calling 'gitlabCi(validate = false) {}'"))
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

internal fun <T : DslBase> serializeToYaml(strategy: SerializationStrategy<T>, value: T, writer: Writer) {
    val config = YamlConfiguration(encodeDefaults = false)
    val yaml = Yaml(configuration = config)

    val yamlString = yaml.stringify(strategy, value)
    writer.write(yamlString)
}