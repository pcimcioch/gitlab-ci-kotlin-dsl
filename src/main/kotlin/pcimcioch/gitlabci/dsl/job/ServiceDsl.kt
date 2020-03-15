package pcimcioch.gitlabci.dsl.job

import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.DslBase.Companion.addErrors
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.addAndReturn
import pcimcioch.gitlabci.dsl.isEmpty

@GitlabCiDslMarker
class ServiceDsl(var name: String? = null) : DslBase {
    var alias: String? = null
    var entrypoint: List<String>? = null
    var cmd: List<String>? = null

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

fun service(block: ServiceDsl.() -> Unit) = ServiceDsl().apply(block)
fun service(name: String) = ServiceDsl(name)
fun service(name: String, block: ServiceDsl.() -> Unit) = ServiceDsl(name).apply(block)

@GitlabCiDslMarker
class ServiceListDsl : DslBase {
    private val services: MutableList<ServiceDsl> = mutableListOf()

    fun service(block: ServiceDsl.() -> Unit) = addAndReturn(services, ServiceDsl()).apply(block)
    fun service(name: String) = addAndReturn(services, ServiceDsl(name))
    fun service(name: String, block: ServiceDsl.() -> Unit) = addAndReturn(services, ServiceDsl(name)).apply(block)
    operator fun ServiceDsl.unaryPlus() = this@ServiceListDsl.services.add(this)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, services, "")
    }
}

fun services(block: ServiceListDsl.() -> Unit) = ServiceListDsl().apply(block)
fun services(vararg elements: String) = services(elements.toList())
fun services(elements: Iterable<String>) = ServiceListDsl().apply { elements.forEach { service(it) } }
