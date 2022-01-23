package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@Serializable
class ServiceDsl(
        var name: String? = null
) : DslBase() {
    var alias: String? = null
    var command: MutableList<String>? = null
    var entrypoint: MutableList<String>? = null

    fun cmd(vararg elements: String) = cmd(elements.toList())
    fun cmd(elements: Iterable<String>) = ensureCmd().addAll(elements)

    fun entrypoint(vararg elements: String) = entrypoint(elements.toList())
    fun entrypoint(elements: Iterable<String>) = ensureEntrypoint().addAll(elements)

    override fun validate(errors: MutableList<String>) {
        addError(errors, isEmpty(name), "[service name='$name'] name '$name' is incorrect")
    }

    private fun ensureEntrypoint() = entrypoint ?: mutableListOf<String>().also { entrypoint = it }
    private fun ensureCmd() = command ?: mutableListOf<String>().also { command = it }

    companion object {
        init {
            addSerializer(ServiceDsl::class, serializer())
        }
    }
}

fun createService(name: String? = null, block: ServiceDsl.() -> Unit = {}) = ServiceDsl(name).apply(block)

@Serializable(with = ServiceListDsl.ServiceListDslSerializer::class)
class ServiceListDsl : DslBase() {
    private val services: MutableList<ServiceDsl> = mutableListOf()

    fun service(name: String? = null, block: ServiceDsl.() -> Unit = {}) = addAndReturn(services, ServiceDsl(name).apply(block))
    operator fun ServiceDsl.unaryPlus() = this@ServiceListDsl.services.add(this)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, "", services)
    }

    object ServiceListDslSerializer : ValueSerializer<ServiceListDsl, List<ServiceDsl>>(ListSerializer(ServiceDsl.serializer()), ServiceListDsl::services)
    companion object {
        init {
            addSerializer(ServiceListDsl::class, serializer())
        }
    }
}

fun createServices(block: ServiceListDsl.() -> Unit = {}) = ServiceListDsl().apply(block)
fun createServices(vararg elements: String, block: ServiceListDsl.() -> Unit = {}) = createServices(elements.toList(), block)
fun createServices(elements: Iterable<String>, block: ServiceListDsl.() -> Unit = {}) = ServiceListDsl().apply { elements.forEach { service(it) } }.apply(block)
