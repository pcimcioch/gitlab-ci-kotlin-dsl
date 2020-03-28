package pcimcioch.gitlabci.dsl.include

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.list
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@Serializable(with = IncludeDsl.IncludeDslSerializer::class)
class IncludeDsl : DslBase() {
    private val includes = mutableListOf<IncludeDetailsDsl>()

    fun local(file: String) = includes.add(IncludeLocalDsl(file))
    fun file(project: String, file: String, ref: String? = null) = includes.add(IncludeFileDsl(project, file, ref))
    fun template(template: String) = includes.add(IncludeTemplateDsl(template))
    fun remote(remote: String) = includes.add(IncludeRemoteDsl(remote))

    override fun validate(errors: MutableList<String>) {
        addErrors(errors,"[include]", includes)
    }

    object IncludeDslSerializer : ValueSerializer<IncludeDsl, List<IncludeDetailsDsl>>(DslBase.serializer().list, IncludeDsl::includes)
    companion object {
        init {
            addSerializer(IncludeDsl::class, serializer())
        }
    }
}

abstract class IncludeDetailsDsl : DslBase()

@Serializable
class IncludeLocalDsl(
        var local: String? = null
) : IncludeDetailsDsl() {
    companion object {
        init {
            addSerializer(IncludeLocalDsl::class, serializer())
        }
    }
}

@Serializable
class IncludeFileDsl(
        var project: String? = null,
        var file: String? = null,
        var ref: String? = null
) : IncludeDetailsDsl() {
    companion object {
        init {
            addSerializer(IncludeFileDsl::class, serializer())
        }
    }
}

@Serializable
class IncludeTemplateDsl(
        var template: String? = null
) : IncludeDetailsDsl() {
    companion object {
        init {
            addSerializer(IncludeTemplateDsl::class, serializer())
        }
    }
}

@Serializable
class IncludeRemoteDsl(
        var remote: String? = null
) : IncludeDetailsDsl() {
    companion object {
        init {
            addSerializer(IncludeRemoteDsl::class, serializer())
        }
    }
}