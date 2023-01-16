package pcimcioch.gitlabci.dsl.include

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@Serializable(with = IncludeDsl.IncludeDslSerializer::class)
class IncludeDsl : DslBase() {
    private val includes = mutableListOf<IncludeDetailsDsl>()

    fun local(local: String) = addAndReturn(includes, IncludeLocalDsl(local))
    fun file(project: String, file: String, ref: String? = null) = addAndReturn(includes, IncludeFileDsl(project, file, ref))

    fun template(template: String) = addAndReturn(includes, IncludeTemplateDsl(template))
    fun remote(remote: String) = addAndReturn(includes, IncludeRemoteDsl(remote))
    operator fun IncludeDetailsDsl.unaryPlus() = this@IncludeDsl.includes.add(this)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, "[include]", includes)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IncludeDsl

        if (includes != other.includes) return false

        return true
    }

    override fun hashCode(): Int {
        return includes.hashCode()
    }

    object IncludeDslSerializer : ValueSerializer<IncludeDsl, List<IncludeDetailsDsl>>(
        ListSerializer(IncludeDetailsDsl.serializer()),
        IncludeDsl::includes
    )

    companion object {
        init {
            addSerializer(IncludeDsl::class, serializer())
        }
    }
}

@Suppress("SERIALIZER_TYPE_INCOMPATIBLE")
@Serializable(with = DslBase.DslBaseSerializer::class)
sealed class IncludeDetailsDsl : DslBase()

@Serializable
class IncludeLocalDsl(
    var local: String
) : IncludeDetailsDsl() {
    companion object {
        init {
            addSerializer(IncludeLocalDsl::class, serializer())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IncludeLocalDsl

        if (local != other.local) return false

        return true
    }

    override fun hashCode(): Int {
        return local.hashCode()
    }
}

fun createIncludeLocal(local: String) = IncludeLocalDsl(local)

@Serializable
class IncludeFileDsl(
    var project: String,
    var file: String,
    var ref: String? = null
) : IncludeDetailsDsl() {
    companion object {
        init {
            addSerializer(IncludeFileDsl::class, serializer())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IncludeFileDsl

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

fun createIncludeFile(project: String, file: String, ref: String? = null) = IncludeFileDsl(project, file, ref)

@Serializable
class IncludeTemplateDsl(
    var template: String
) : IncludeDetailsDsl() {
    companion object {
        init {
            addSerializer(IncludeTemplateDsl::class, serializer())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IncludeTemplateDsl

        if (template != other.template) return false

        return true
    }

    override fun hashCode(): Int {
        return template.hashCode()
    }
}

fun createIncludeTemplate(template: String) = IncludeTemplateDsl(template)

@Serializable
class IncludeRemoteDsl(
    var remote: String
) : IncludeDetailsDsl() {
    companion object {
        init {
            addSerializer(IncludeRemoteDsl::class, serializer())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IncludeRemoteDsl

        if (remote != other.remote) return false

        return true
    }

    override fun hashCode(): Int {
        return remote.hashCode()
    }
}

fun createIncludeRemote(remote: String) = IncludeRemoteDsl(remote)