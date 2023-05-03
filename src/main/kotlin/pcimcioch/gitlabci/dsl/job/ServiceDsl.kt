package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.SerialName
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

    @SerialName("pull_policy")
    var pullPolicy: MutableList<PullPolicy>? = null

    fun cmd(vararg elements: String) = cmd(elements.toList())
    fun cmd(elements: Iterable<String>) = ensureCmd().addAll(elements)

    fun entrypoint(vararg elements: String) = entrypoint(elements.toList())
    fun entrypoint(elements: Iterable<String>) = ensureEntrypoint().addAll(elements)

    fun pullPolicy(vararg elements: PullPolicy) = pullPolicy(elements.toList())
    fun pullPolicy(elements: Iterable<PullPolicy>) = ensurePullPolicy().addAll(elements)

    override fun validate(errors: MutableList<String>) {
        addError(errors, isEmpty(name), "[service name='$name'] name '$name' is incorrect")
    }

    private fun ensureEntrypoint() = entrypoint ?: mutableListOf<String>().also { entrypoint = it }
    private fun ensureCmd() = command ?: mutableListOf<String>().also { command = it }
    private fun ensurePullPolicy() = pullPolicy ?: mutableListOf<PullPolicy>().also { pullPolicy = it }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ServiceDsl

        if (name != other.name) return false
        if (alias != other.alias) return false
        if (command != other.command) return false
        if (entrypoint != other.entrypoint) return false
        if (pullPolicy != other.pullPolicy) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (alias?.hashCode() ?: 0)
        result = 31 * result + (command?.hashCode() ?: 0)
        result = 31 * result + (entrypoint?.hashCode() ?: 0)
        result = 31 * result + (pullPolicy?.hashCode() ?: 0)
        return result
    }


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

    fun service(name: String? = null, block: ServiceDsl.() -> Unit = {}) =
        addAndReturn(services, ServiceDsl(name).apply(block))

    operator fun ServiceDsl.unaryPlus() = this@ServiceListDsl.services.add(this)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, "", services)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ServiceListDsl

        if (services != other.services) return false

        return true
    }

    override fun hashCode(): Int {
        return services.hashCode()
    }


    object ServiceListDslSerializer : ValueSerializer<ServiceListDsl, List<ServiceDsl>>(
        ListSerializer(ServiceDsl.serializer()),
        ServiceListDsl::services
    )

    companion object {
        init {
            addSerializer(ServiceListDsl::class, serializer())
        }
    }
}

fun createServices(block: ServiceListDsl.() -> Unit = {}) = ServiceListDsl().apply(block)
fun createServices(vararg elements: String, block: ServiceListDsl.() -> Unit = {}) =
    createServices(elements.toList(), block)

fun createServices(elements: Iterable<String>, block: ServiceListDsl.() -> Unit = {}) =
    ServiceListDsl().apply { elements.forEach { service(it) } }.apply(block)
