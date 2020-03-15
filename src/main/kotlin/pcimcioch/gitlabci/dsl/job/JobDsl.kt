package pcimcioch.gitlabci.dsl.job

import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.DslBase.Companion.addErrors
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.isEmpty
import pcimcioch.gitlabci.dsl.stage.StageDsl
import java.time.Duration

@GitlabCiDslMarker
class JobDsl(var name: String? = null) : DslBase {
    var inherit: InheritDsl? = null
    var image: ImageDsl? = null
    var stage: String? = null
    var allowFailure: Boolean? = null
    var whenRun: WhenType? = null
    var startIn: Duration? = null
    var script: ScriptDsl? = null
    var services: ServiceListDsl? = null
    var retry: RetryDsl? = null
    var timeout: Duration? = null
    var parallel: Int? = null
    var interruptible: Boolean? = null
    var resourceGroup: String? = null
    var variables: VariablesDsl? = null
    var cache: CacheDsl? = null
    var tags: MutableSet<String> = mutableSetOf()
    var extends: MutableList<String> = mutableListOf()

    fun script(block: ScriptDsl.() -> Unit) = ensureScript().apply(block)

    fun inherit(block: InheritDsl.() -> Unit) = ensureInherit().apply(block)

    fun image(name: String) = ensureImage().apply { this.name = name }
    fun image(block: ImageDsl.() -> Unit) = ensureImage().apply(block)
    fun image(name: String, block: ImageDsl.() -> Unit) = ensureImage().apply { this.name = name }.apply(block)

    fun services(vararg elements: String) = services(elements.toList())
    fun services(elements: Iterable<String>) = ensureServices().apply { elements.forEach { service(it) } }
    fun services(block: ServiceListDsl.() -> Unit) = ensureServices().apply(block)

    fun stage(value: StageDsl) {
        stage = value.name
    }

    fun tags(vararg elements: String) = tags(elements.toList())
    fun tags(elements: Iterable<String>) = tags.addAll(elements)

    fun retry(max: Int) = ensureRetry().apply { this.max = max }
    fun retry(block: RetryDsl.() -> Unit) = ensureRetry().apply(block)
    fun retry(max: Int, block: RetryDsl.() -> Unit) = ensureRetry().apply { this.max = max }.apply(block)

    fun extends(vararg elements: String) = extends(elements.toList())
    fun extends(elements: Iterable<String>) = extends.addAll(elements)
    fun extends(vararg elements: JobDsl) = extends(elements.toList())
    fun extends(elements: Iterable<JobDsl>) = elements.forEach { extends.add(it.name ?: throw IllegalStateException("Passed job without name to extends")) }

    fun variables(block: VariablesDsl.() -> Unit) = ensureVariables().apply(block)

    fun cache(block: CacheDsl.() -> Unit) = ensureCache().apply(block)
    fun cache(vararg elements: String) = cache(elements.toList())
    fun cache(elements: Iterable<String>) = ensureCache().apply { paths(elements) }

    override fun validate(errors: MutableList<String>) {
        val prefix = "[job name='$name']"

        addError(errors, isEmpty(name) || RESTRICTED_NAMES.contains(name), "$prefix name '$name' is incorrect")
        addError(errors, startIn != null && whenRun != WhenType.DELAYED, "$prefix startIn can be used only with when=delayed jobs")
        addError(errors, script?.commands?.isEmpty() != false, "$prefix at least one script command must be configured")
        addError(errors, parallel != null && (parallel!! < 2 || parallel!! > 50), "$prefix parallel must be in range [2, 50]")

        addErrors(errors, inherit, prefix)
        addErrors(errors, image, prefix)
        addErrors(errors, script, prefix)
        addErrors(errors, services, prefix)
        addErrors(errors, variables, prefix)
        addErrors(errors, cache, prefix)
    }

    private fun ensureInherit() = inherit ?: InheritDsl().also { inherit = it }
    private fun ensureImage() = image ?: ImageDsl().also { image = it }
    private fun ensureScript() = script ?: ScriptDsl().also { script = it }
    private fun ensureServices() = services ?: ServiceListDsl().also { services = it }
    private fun ensureRetry() = retry ?: RetryDsl().also { retry = it }
    private fun ensureVariables() = variables ?: VariablesDsl().also { variables = it }
    private fun ensureCache() = cache ?: CacheDsl().also { cache = it }

    private companion object {
        val RESTRICTED_NAMES = listOf("image", "services", "stages", "types", "before_script", "after_script", "variables", "cache", "include")
    }
}

fun job(block: JobDsl.() -> Unit) = JobDsl().apply(block)
fun job(name: String, block: JobDsl.() -> Unit) = JobDsl(name).apply(block)

enum class WhenType {
    ON_SUCCESS,
    ON_FAILURE,
    ALWAYS,
    MANUAL,
    DELAYED;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}