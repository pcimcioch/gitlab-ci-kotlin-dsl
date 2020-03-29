package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.list
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.StringRepresentation
import pcimcioch.gitlabci.dsl.serializer.StringRepresentationSerializer
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@Serializable
class TriggerDsl(
        var project: String? = null,
        var branch: String? = null,
        var strategy: TriggerStrategy? = null
) : DslBase() {
    var include: TriggerIncludeDsl? = null

    fun include(block: TriggerIncludeDsl.() -> Unit) = ensureInclude().apply(block)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, "[trigger]", include)
    }

    private fun ensureInclude() = include ?: TriggerIncludeDsl().also { include = it }

    companion object {
        init {
            addSerializer(TriggerDsl::class, serializer())
        }
    }
}

fun createTrigger(project: String? = null, branch: String? = null, strategy: TriggerStrategy? = null, block: TriggerDsl.() -> Unit = {}) = TriggerDsl(project, branch, strategy).apply(block)

@Serializable(with = TriggerIncludeDsl.TriggerIncludeDslSerializer::class)
class TriggerIncludeDsl : DslBase() {
    private val includes = mutableListOf<TriggerIncludeDetailsDsl>()

    fun local(local: String) = addAndReturn(includes, TriggerIncludeLocalDsl(local))
    fun artifact(artifact: String, job: String) = addAndReturn(includes, TriggerIncludeArtifactDsl(artifact, job))
    fun artifact(artifact: String, job: JobDsl) = addAndReturn(includes, TriggerIncludeArtifactDsl(artifact, job))
    operator fun TriggerIncludeDetailsDsl.unaryPlus() = this@TriggerIncludeDsl.includes.add(this)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, "[include]", includes)
    }

    object TriggerIncludeDslSerializer : ValueSerializer<TriggerIncludeDsl, List<TriggerIncludeDetailsDsl>>(TriggerIncludeDetailsDsl.serializer().list, TriggerIncludeDsl::includes)
    companion object {
        init {
            addSerializer(TriggerIncludeDsl::class, serializer())
        }
    }
}

fun createTriggerInclude(block: TriggerIncludeDsl.() -> Unit) = TriggerIncludeDsl().apply(block)

@Serializable(with = DslBase.DslBaseSerializer::class)
sealed class TriggerIncludeDetailsDsl : DslBase()

@Serializable
class TriggerIncludeLocalDsl(
        var local: String
) : TriggerIncludeDetailsDsl() {
    companion object {
        init {
            addSerializer(TriggerIncludeLocalDsl::class, serializer())
        }
    }
}

fun createTriggerIncludeLocal(local: String) = TriggerIncludeLocalDsl(local)

@Serializable
class TriggerIncludeArtifactDsl(
        var artifact: String,
        var job: String
) : TriggerIncludeDetailsDsl() {
    constructor(artifact: String, job: JobDsl) : this(artifact, job.name)

    companion object {
        init {
            addSerializer(TriggerIncludeArtifactDsl::class, serializer())
        }
    }
}

fun createTriggerIncludeArtifact(artifact: String, job: String) = TriggerIncludeArtifactDsl(artifact, job)
fun createTriggerIncludeArtifact(artifact: String, job: JobDsl) = TriggerIncludeArtifactDsl(artifact, job)

@Serializable(with = TriggerStrategy.TriggerStrategySerializer::class)
enum class TriggerStrategy(
        override val stringRepresentation: String
) : StringRepresentation {
    DEPEND("depend");

    object TriggerStrategySerializer : StringRepresentationSerializer<TriggerStrategy>("TriggerStrategy")
}