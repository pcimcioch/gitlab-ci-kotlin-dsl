package pcimcioch.gitlabci.dsl.job

import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker

@GitlabCiDslMarker
class InheritDsl : DslBase {
    private var defaultBoolean: Boolean? = null
    private var variablesBoolean: Boolean? = null
    private val defaultList: MutableSet<DefaultType> = mutableSetOf()
    private val variablesList: MutableSet<String> = mutableSetOf()

    fun default(vararg elements: DefaultType) = default(elements.toList())
    fun default(elements: Iterable<DefaultType>) {
        defaultBoolean = null
        defaultList.addAll(elements)
    }

    fun default(value: Boolean) {
        defaultList.clear()
        defaultBoolean = value
    }

    fun variables(vararg elements: String) = variables(elements.toList())
    fun variables(elements: Iterable<String>) {
        variablesBoolean = null
        variablesList.addAll(elements)
    }

    fun variables(value: Boolean) {
        variablesList.clear()
        variablesBoolean = value
    }
}

fun inherit(block: InheritDsl.() -> Unit) = InheritDsl().apply(block)

enum class DefaultType {
    IMAGE,
    SERVICES,
    BEFORE_SCRIPT,
    AFTER_SCRIPT,
    TAGS,
    CACHE,
    ARTIFACTS,
    RETRY,
    TIMEOUT,
    INTERRUPTIBLE;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}