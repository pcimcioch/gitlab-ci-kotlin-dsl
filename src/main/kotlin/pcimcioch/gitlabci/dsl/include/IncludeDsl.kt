package pcimcioch.gitlabci.dsl.include

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@Serializable(with = IncludeDsl.IncludeDslSerializer::class)
class IncludeDsl : DslBase() {
    private val includes = mutableListOf<IncludeDetailsDsl>()

    fun local(local: String, block: IncludeLocalDsl.() -> Unit = {}) = addAndReturn(includes, IncludeLocalDsl(local).apply(block))
    fun file(project: String, file: String, ref: String? = null, block: IncludeFileDsl.() -> Unit = {}) = addAndReturn(includes, IncludeFileDsl(project, file, ref).apply(block))

    fun template(template: String, block: IncludeTemplateDsl.() -> Unit = {}) = addAndReturn(includes, IncludeTemplateDsl(template).apply(block))
    fun remote(remote: String, block: IncludeRemoteDsl.() -> Unit = {}) = addAndReturn(includes, IncludeRemoteDsl(remote).apply(block))
    fun component(component: String, block: IncludeComponentDsl.() -> Unit = {}) = addAndReturn(includes, IncludeComponentDsl(component).apply(block))
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
sealed class IncludeDetailsDsl : DslBase() {
    abstract var inputs: InputsDsl?

    fun inputs(block: InputsDsl.() -> Unit = {}) = ensureInputs().apply(block)
    fun inputs(elements: Map<String, Any>, block: InputsDsl.() -> Unit = {}) =
        ensureInputs().apply { elements.forEach { add(it.key, it.value) } }.apply(block)

    @JvmName("inputsEnum")
    fun <T : Enum<T>> inputs(elements: Map<T, Any>, block: InputsDsl.() -> Unit = {}) =
        ensureInputs().apply { elements.forEach { add(it.key, it.value) } }.apply(block)

    private fun ensureInputs() = inputs ?: InputsDsl().also { inputs = it }

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, "", inputs)
    }

}

@Serializable
class IncludeLocalDsl(
    var local: String,
    override var inputs: InputsDsl? = null
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
        if (inputs != other.inputs) return false

        return true
    }

    override fun hashCode(): Int {
        var result = local.hashCode()
        result = 31 * result + (inputs?.hashCode() ?: 0)
        return result
    }
}

fun createIncludeLocal(local: String, block: IncludeLocalDsl.() -> Unit = {}) = IncludeLocalDsl(local).apply(block)

@Serializable
class IncludeFileDsl(
    var project: String,
    var file: String,
    var ref: String? = null,
    override var inputs: InputsDsl? = null
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
        if (inputs != other.inputs) return false

        return true
    }

    override fun hashCode(): Int {
        var result = project.hashCode()
        result = 31 * result + file.hashCode()
        result = 31 * result + (ref?.hashCode() ?: 0)
        result = 31 * result + (inputs?.hashCode() ?: 0)
        return result
    }
}

fun createIncludeFile(project: String, file: String, ref: String? = null, block: IncludeFileDsl.() -> Unit = {}) = IncludeFileDsl(project, file, ref).apply(block)

@Serializable
class IncludeTemplateDsl(
    var template: String,
    override var inputs: InputsDsl? = null
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
        if (inputs != other.inputs) return false

        return true
    }

    override fun hashCode(): Int {
        var result = template.hashCode()
        result = 31 * result + (inputs?.hashCode() ?: 0)
        return result
    }
}

fun createIncludeTemplate(template: String, block: IncludeTemplateDsl.() -> Unit = {}) = IncludeTemplateDsl(template).apply(block)

@Serializable
class IncludeRemoteDsl(
    var remote: String,
    override var inputs: InputsDsl? = null
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
        if (inputs != other.inputs) return false

        return true
    }

    override fun hashCode(): Int {
        var result = remote.hashCode()
        result = 31 * result + (inputs?.hashCode() ?: 0)
        return result
    }
}

fun createIncludeRemote(remote: String, block: IncludeRemoteDsl.() -> Unit = {}) = IncludeRemoteDsl(remote).apply(block)

@Serializable
class IncludeComponentDsl(
    var component: String,
    override var inputs: InputsDsl? = null
) : IncludeDetailsDsl() {
    companion object {
        init {
            addSerializer(IncludeComponentDsl::class, serializer())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IncludeComponentDsl

        if (component != other.component) return false
        if (inputs != other.inputs) return false

        return true
    }

    override fun hashCode(): Int {
        var result = component.hashCode()
        result = 31 * result + (inputs?.hashCode() ?: 0)
        return result
    }
}

fun createIncludeComponent(remote: String, block: IncludeComponentDsl.() -> Unit = {}) = IncludeComponentDsl(remote).apply(block)
