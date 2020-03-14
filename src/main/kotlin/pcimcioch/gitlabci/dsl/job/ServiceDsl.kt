package pcimcioch.gitlabci.dsl.job

import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.isEmpty

@GitlabCiDslMarker
class ServiceDsl : DslBase {
    var name: String? = null
    var alias: String? = null
    private var entrypoint: List<String> = listOf()
    private var cmd: List<String> = listOf()

    constructor()
    constructor(name: String) {
        this.name = name
    }

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
