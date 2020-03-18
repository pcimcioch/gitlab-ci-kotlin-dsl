package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.list
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.DslBase.Companion.addErrors
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.addAndReturn
import pcimcioch.gitlabci.dsl.isEmpty
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@GitlabCiDslMarker
@Serializable
class ServiceDsl(
        var name: String? = null
) : DslBase {
    var alias: String? = null
    var cmd: List<String>? = null
    var entrypoint: List<String>? = null

    fun cmd(vararg elements: String) = cmd(elements.toList())
    fun cmd(elements: Iterable<String>) {
        cmd = elements.toList()
    }

    fun entrypoint(vararg elements: String) = entrypoint(elements.toList())
    fun entrypoint(elements: Iterable<String>) {
        entrypoint = elements.toList()
    }

    override fun validate(errors: MutableList<String>) {
        addError(errors, isEmpty(name), "[service name='$name'] name '$name' is incorrect")
    }
}

fun createService(block: ServiceDsl.() -> Unit) = ServiceDsl().apply(block)
fun createService(name: String) = ServiceDsl(name)
fun createService(name: String, block: ServiceDsl.() -> Unit) = ServiceDsl(name).apply(block)

@GitlabCiDslMarker
@Serializable(with = ServiceListDsl.ServiceListDslSerializer::class)
class ServiceListDsl : DslBase {
    private val services: MutableList<ServiceDsl> = mutableListOf()

    fun service(block: ServiceDsl.() -> Unit) = addAndReturn(services, ServiceDsl()).apply(block)
    fun service(name: String) = addAndReturn(services, ServiceDsl(name))
    fun service(name: String, block: ServiceDsl.() -> Unit) = addAndReturn(services, ServiceDsl(name)).apply(block)
    operator fun ServiceDsl.unaryPlus() = this@ServiceListDsl.services.add(this)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, services, "")
    }

    object ServiceListDslSerializer : ValueSerializer<ServiceListDsl, List<ServiceDsl>>(ServiceDsl.serializer().list, ServiceListDsl::services)
}

fun createServices(block: ServiceListDsl.() -> Unit) = ServiceListDsl().apply(block)
fun createServices(vararg elements: String) = createServices(elements.toList())
fun createServices(elements: Iterable<String>) = ServiceListDsl().apply { elements.forEach { service(it) } }
