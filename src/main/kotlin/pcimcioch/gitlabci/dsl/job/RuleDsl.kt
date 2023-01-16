package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.Duration
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@Serializable
class RuleDsl : DslBase() {
    @SerialName("if")
    var ifCondition: String? = null
    var changes: MutableSet<String>? = null
    var exists: MutableSet<String>? = null

    @SerialName("allow_failure")
    var allowFailure: Boolean? = null

    @SerialName("when")
    var whenRun: WhenRunType? = null

    @SerialName("start_in")
    var startIn: Duration? = null

    fun changes(vararg elements: String) = changes(elements.toList())
    fun changes(elements: Iterable<String>) = ensureChanges().addAll(elements)

    fun exists(vararg elements: String) = exists(elements.toList())
    fun exists(elements: Iterable<String>) = ensureExists().addAll(elements)

    override fun validate(errors: MutableList<String>) {
        addError(
            errors,
            startIn != null && whenRun != WhenRunType.DELAYED,
            "[rule] startIn can be used only with when=delayed jobs"
        )
    }

    private fun ensureChanges() = changes ?: mutableSetOf<String>().also { changes = it }
    private fun ensureExists() = exists ?: mutableSetOf<String>().also { exists = it }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RuleDsl

        if (ifCondition != other.ifCondition) return false
        if (changes != other.changes) return false
        if (exists != other.exists) return false
        if (allowFailure != other.allowFailure) return false
        if (whenRun != other.whenRun) return false
        if (startIn != other.startIn) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ifCondition?.hashCode() ?: 0
        result = 31 * result + (changes?.hashCode() ?: 0)
        result = 31 * result + (exists?.hashCode() ?: 0)
        result = 31 * result + (allowFailure?.hashCode() ?: 0)
        result = 31 * result + (whenRun?.hashCode() ?: 0)
        result = 31 * result + (startIn?.hashCode() ?: 0)
        return result
    }


    companion object {
        init {
            addSerializer(RuleDsl::class, serializer())
        }
    }
}

fun createRule(block: RuleDsl.() -> Unit = {}) = RuleDsl().apply(block)

@Serializable(with = RuleListDsl.RuleListDslSerializer::class)
class RuleListDsl : DslBase() {
    private val rules: MutableList<RuleDsl> = mutableListOf()

    fun rule(block: RuleDsl.() -> Unit) = addAndReturn(rules, RuleDsl().apply(block))
    operator fun RuleDsl.unaryPlus() = this@RuleListDsl.rules.add(this)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, "", rules)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RuleListDsl

        if (rules != other.rules) return false

        return true
    }

    override fun hashCode(): Int {
        return rules.hashCode()
    }


    object RuleListDslSerializer :
        ValueSerializer<RuleListDsl, List<RuleDsl>>(ListSerializer(RuleDsl.serializer()), RuleListDsl::rules)

    companion object {
        init {
            addSerializer(RuleListDsl::class, serializer())
        }
    }
}

fun createRules(block: RuleListDsl.() -> Unit) = RuleListDsl().apply(block)