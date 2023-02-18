package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
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

    fun include(block: TriggerIncludeDsl.() -> Unit = {}) = ensureInclude().apply(block)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, "[trigger]", include)
    }

    private fun ensureInclude() = include ?: TriggerIncludeDsl().also { include = it }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TriggerDsl

        if (project != other.project) return false
        if (branch != other.branch) return false
        if (strategy != other.strategy) return false
        if (include != other.include) return false

        return true
    }

    override fun hashCode(): Int {
        var result = project?.hashCode() ?: 0
        result = 31 * result + (branch?.hashCode() ?: 0)
        result = 31 * result + (strategy?.hashCode() ?: 0)
        result = 31 * result + (include?.hashCode() ?: 0)
        return result
    }


    companion object {
        init {
            addSerializer(TriggerDsl::class, serializer())
        }
    }
}

fun createTrigger(
    project: String? = null,
    branch: String? = null,
    strategy: TriggerStrategy? = null,
    block: TriggerDsl.() -> Unit = {}
) = TriggerDsl(project, branch, strategy).apply(block)

@Serializable(with = TriggerIncludeDsl.TriggerIncludeDslSerializer::class)
class TriggerIncludeDsl : DslBase() {
    private val includes = mutableListOf<TriggerIncludeDetailsDsl>()

    fun local(local: String) = addAndReturn(includes, TriggerIncludeLocalDsl(local))
    fun file(project: String, file: String, ref: String? = null) =
        addAndReturn(includes, TriggerIncludeFileDsl(project, file, ref))

    fun artifact(artifact: String, job: String) = addAndReturn(includes, TriggerIncludeArtifactDsl(artifact, job))
    fun artifact(artifact: String, job: JobDsl) = addAndReturn(includes, TriggerIncludeArtifactDsl(artifact, job))
    operator fun TriggerIncludeDetailsDsl.unaryPlus() = this@TriggerIncludeDsl.includes.add(this)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, "[include]", includes)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TriggerIncludeDsl

        if (includes != other.includes) return false

        return true
    }

    override fun hashCode(): Int {
        return includes.hashCode()
    }


    object TriggerIncludeDslSerializer : ValueSerializer<TriggerIncludeDsl, List<TriggerIncludeDetailsDsl>>(
        ListSerializer(TriggerIncludeDetailsDsl.serializer()),
        TriggerIncludeDsl::includes
    )

    companion object {
        init {
            addSerializer(TriggerIncludeDsl::class, serializer())
        }
    }
}

fun createTriggerInclude(block: TriggerIncludeDsl.() -> Unit = {}) = TriggerIncludeDsl().apply(block)

@Suppress("SERIALIZER_TYPE_INCOMPATIBLE")
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TriggerIncludeLocalDsl

        if (local != other.local) return false

        return true
    }

    override fun hashCode(): Int {
        return local.hashCode()
    }
}

fun createTriggerIncludeLocal(local: String) = TriggerIncludeLocalDsl(local)

@Serializable
class TriggerIncludeFileDsl(
    var project: String,
    var file: String,
    var ref: String? = null
) : TriggerIncludeDetailsDsl() {


    companion object {
        init {
            addSerializer(TriggerIncludeFileDsl::class, serializer())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TriggerIncludeFileDsl

        if (project != other.project) return false
        if (file != other.file) return false
        if (ref != other.ref) return false

        return true
    }

    override fun hashCode(): Int {
        var result = project.hashCode()
        result = 31 * result + file.hashCode()
        result = 31 * result + (ref?.hashCode() ?: 0)
        return result
    }
}

fun createTriggerIncludeFile(project: String, file: String, ref: String? = null) =
    TriggerIncludeFileDsl(project, file, ref)

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TriggerIncludeArtifactDsl

        if (artifact != other.artifact) return false
        if (job != other.job) return false

        return true
    }

    override fun hashCode(): Int {
        var result = artifact.hashCode()
        result = 31 * result + job.hashCode()
        return result
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