package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.list
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.DslBase.Companion.addErrors
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.isEmpty
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@GitlabCiDslMarker
@Serializable
class ServiceDsl(
        var name: String? = null
) : DslBase {
    var alias: String? = null
    var cmd: MutableList<String>? = null
    var entrypoint: MutableList<String>? = null

    fun cmd(vararg elements: String) = cmd(elements.toList())
    fun cmd(elements: Iterable<String>) = ensureCmd().addAll(elements)

    fun entrypoint(vararg elements: String) = entrypoint(elements.toList())
    fun entrypoint(elements: Iterable<String>) = ensureEntrypoint().addAll(elements)

    override fun validate(errors: MutableList<String>) {
        addError(errors, isEmpty(name), "[service name='$name'] name '$name' is incorrect")
    }

    private fun ensureEntrypoint() = entrypoint ?: mutableListOf<String>().also { entrypoint = it }
    private fun ensureCmd() = cmd ?: mutableListOf<String>().also { cmd = it }
}

fun createService(block: ServiceDsl.() -> Unit) = ServiceDsl().apply(block)
fun createService(name: String) = ServiceDsl(name)
fun createService(name: String, block: ServiceDsl.() -> Unit) = ServiceDsl(name).apply(block)

@GitlabCiDslMarker
@Serializable(with = ServiceListDsl.ServiceListDslSerializer::class)
class ServiceListDsl : DslBase {
    private val services: MutableList<ServiceDsl> = mutableListOf()

    fun service(block: ServiceDsl.() -> Unit) = services.add(ServiceDsl().apply(block))
    fun service(name: String) = services.add(ServiceDsl(name))
    fun service(name: String, block: ServiceDsl.() -> Unit) = services.add(ServiceDsl(name).apply(block))
    operator fun ServiceDsl.unaryPlus() = this@ServiceListDsl.services.add(this)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, services, "")
    }

    object ServiceListDslSerializer : ValueSerializer<ServiceListDsl, List<ServiceDsl>>(ServiceDsl.serializer().list, ServiceListDsl::services)
}

fun createServices(block: ServiceListDsl.() -> Unit) = ServiceListDsl().apply(block)
fun createServices(vararg elements: String) = createServices(elements.toList())
fun createServices(elements: Iterable<String>) = ServiceListDsl().apply { elements.forEach { service(it) } }
