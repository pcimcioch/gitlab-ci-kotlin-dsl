package pcimcioch.gitlabci.dsl.job

import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker

@GitlabCiDslMarker
class InheritDsl : DslBase {
    private var defaultBoolean: Boolean? = null
    private var variablesBoolean: Boolean? = null
    private val defaultList: MutableList<String> = mutableListOf() // TODO this should be list of enums
    private val variablesList: MutableList<String> = mutableListOf()

    fun default(vararg elements: String) = default(elements.toList())
    fun default(elements: Iterable<String>) = defaultList.addAll(elements)

    fun default(value: Boolean) {
        defaultBoolean = value
    }

    fun variables(vararg elements: String) = variables(elements.toList())
    fun variables(elements: Iterable<String>) = variablesList.addAll(elements)

    fun variables(value: Boolean) {
        variablesBoolean = value
    }

    override fun validate(errors: MutableList<String>) {
        addError(errors, defaultBoolean != null && defaultList.isNotEmpty(), "[inherit] both boolean and list form of 'default' configured")
        addError(errors, variablesBoolean != null && variablesList.isNotEmpty(), "[inherit] both boolean and list form of 'variables' configured")
    }
}

fun inherit(block: InheritDsl.() -> Unit) = InheritDsl().apply(block)