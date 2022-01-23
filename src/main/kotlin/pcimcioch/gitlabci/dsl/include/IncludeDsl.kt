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
}

fun createIncludeRemote(remote: String) = IncludeRemoteDsl(remote)