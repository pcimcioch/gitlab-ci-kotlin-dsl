package pcimcioch.gitlabci.dsl.workflow

import kotlinx.serialization.Serializable
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.job.ArtifactsDsl
import pcimcioch.gitlabci.dsl.job.RuleListDsl

@Serializable
class WorkflowDsl : DslBase() {
    var name: String? = null
    var rules: RuleListDsl? = null

    fun rules(block: RuleListDsl.() -> Unit = {}) = ensureRules().apply(block)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, "[workflow]", rules)
    }

    private fun ensureRules() = rules ?: RuleListDsl().also { rules = it }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WorkflowDsl

        if (rules != other.rules) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (rules?.hashCode() ?: 0)
        return result
    }

    companion object {
        init {
            addSerializer(WorkflowDsl::class, serializer())
        }
    }
}

fun createWorkflow(block: WorkflowDsl.() -> Unit = {}) = WorkflowDsl().apply(block)