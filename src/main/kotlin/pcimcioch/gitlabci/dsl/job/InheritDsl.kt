package pcimcioch.gitlabci.dsl.job

import pcimcioch.gitlabci.dsl.DefaultType
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
    fun default(elements: Iterable<DefaultType>) = defaultList.addAll(elements)

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