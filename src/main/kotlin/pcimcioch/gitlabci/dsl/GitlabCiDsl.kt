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
import pcimcioch.gitlabci.dsl.job.JobDsl
import pcimcioch.gitlabci.dsl.serializer.MultiTypeSerializer
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer
import pcimcioch.gitlabci.dsl.stage.StagesDsl
import java.io.FileWriter
import java.io.Writer
import kotlin.collections.set

@GitlabCiDslMarker
@Serializable(with = GitlabCiDsl.GitlabCiDslSerializer::class)
class GitlabCiDsl : DslBase {
    private val jobs: MutableList<JobDsl> = mutableListOf()
    private var stages: StagesDsl? = null

    fun job(name: String, block: JobDsl.() -> Unit) = addAndReturn(jobs, JobDsl(name)).apply(block)
    operator fun JobDsl.unaryPlus() = this@GitlabCiDsl.jobs.add(this)

    fun stages(block: StagesDsl.() -> Unit) = ensureStages().apply(block)
    fun stages(vararg elements: String) = stages(elements.toList())
    fun stages(elements: Iterable<String>) = ensureStages().apply { elements.forEach { stage(it) } }

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, stages, "")
        addErrors(errors, jobs, "")
    }

    private fun ensureStages() = stages ?: StagesDsl().also { stages = it }

    private fun asMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()

        stages?.also { map["stages"] = it }
        jobs.forEach { map[it.name] = it }

        return map
    }

    object ElementSerializer : MultiTypeSerializer(
            PrimitiveDescriptor("Elements", PrimitiveKind.STRING),
            mapOf(
                    JobDsl::class to JobDsl.serializer(),
                    StagesDsl::class to StagesDsl.serializer()))

    object GitlabCiDslSerializer : ValueSerializer<GitlabCiDsl, Map<String, Any>>(MapSerializer(String.serializer(), ElementSerializer), GitlabCiDsl::asMap)
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