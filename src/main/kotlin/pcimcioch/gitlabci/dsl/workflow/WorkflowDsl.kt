package pcimcioch.gitlabci.dsl.workflow

import kotlinx.serialization.Serializable
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addErrors
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.job.RuleListDsl

@GitlabCiDslMarker
@Serializable
class WorkflowDsl : DslBase {
    var rules: RuleListDsl? = null

    fun rules(block: RuleListDsl.() -> Unit) = ensureRules().apply(block)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, "[workflow]", rules)
    }

    private fun ensureRules() = rules ?: RuleListDsl().also { rules = it }
}