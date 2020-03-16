package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.DslBase.Companion.addErrors
import pcimcioch.gitlabci.dsl.Duration
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.StringRepresentation
import pcimcioch.gitlabci.dsl.isEmpty
import pcimcioch.gitlabci.dsl.serializer.StringRepresentationSerializer
import pcimcioch.gitlabci.dsl.stage.StageDsl

// TODO tests
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
    var needs: NeedsListDsl? = null
    var retry: RetryDsl? = null
    var timeout: Duration? = null
    var parallel: Int? = null
    var interruptible: Boolean? = null
    var resourceGroup: String? = null
    var variables: VariablesDsl? = null
    var cache: CacheDsl? = null
    var artifacts: ArtifactsDsl? = null
    var afterScript: AfterScriptDsl? = null
    var beforeScript: BeforeScriptDsl? = null
    var tags: MutableSet<String>? = null
    var extends: MutableList<String>? = null
    var dependencies: MutableSet<String>? = null

    fun script(block: ScriptDsl.() -> Unit) = ensureScript().apply(block)

    fun beforeScript(block: BeforeScriptDsl.() -> Unit) = ensureBeforeScript().apply(block)

    fun afterScript(block: AfterScriptDsl.() -> Unit) = ensureAfterScript().apply(block)

    fun inherit(block: InheritDsl.() -> Unit) = ensureInherit().apply(block)

    fun image(name: String) = ensureImage().apply { this.name = name }
    fun image(block: ImageDsl.() -> Unit) = ensureImage().apply(block)
    fun image(name: String, block: ImageDsl.() -> Unit) = ensureImage().apply { this.name = name }.apply(block)

    fun services(vararg elements: String) = services(elements.toList())
    fun services(elements: Iterable<String>) = ensureServices().apply { elements.forEach { service(it) } }
    fun services(block: ServiceListDsl.() -> Unit) = ensureServices().apply(block)

    fun needs(vararg elements: String) = needs(elements.toList())
    fun needs(elements: Iterable<String>) = ensureNeeds().apply { elements.forEach { needJob(it) } }
    fun needs(vararg elements: JobDsl) = needs(elements.toList())

    @JvmName("needsJob")
    fun needs(elements: Iterable<JobDsl>) = ensureNeeds().apply { elements.forEach { needJob(it) } }
    fun needs(block: NeedsListDsl.() -> Unit) = ensureNeeds().apply(block)

    fun stage(value: StageDsl) {
        stage = value.name
    }

    fun tags(vararg elements: String) = tags(elements.toList())
    fun tags(elements: Iterable<String>) = ensureTags().addAll(elements)

    fun retry(max: Int) = ensureRetry().apply { this.max = max }
    fun retry(block: RetryDsl.() -> Unit) = ensureRetry().apply(block)
    fun retry(max: Int, block: RetryDsl.() -> Unit) = ensureRetry().apply { this.max = max }.apply(block)

    fun extends(vararg elements: String) = extends(elements.toList())
    fun extends(elements: Iterable<String>) = ensureExtends().addAll(elements)
    fun extends(vararg elements: JobDsl) = extends(elements.toList())
    fun extends(elements: Iterable<JobDsl>) = ensureExtends().apply { elements.forEach { add(it.getName()) } }

    fun dependencies(vararg elements: String) = dependencies(elements.toList())
    fun dependencies(elements: Iterable<String>) = ensureDependencies().addAll(elements)
    fun dependencies(vararg elements: JobDsl) = dependencies(elements.toList())
    fun dependencies(elements: Iterable<JobDsl>) = ensureDependencies().apply { elements.forEach { add(it.getName()) } }

    fun emptyDependencies() = ensureDependencies().clear()

    fun variables(block: VariablesDsl.() -> Unit) = ensureVariables().apply(block)
    fun variables(elements: Map<String, String>) = ensureVariables().apply { elements.forEach { add(it.key, it.value) } }
    fun variables(elements: Map<String, String>, block: VariablesDsl.() -> Unit) = ensureVariables().apply { elements.forEach { add(it.key, it.value) } }.apply(block)

    @JvmName("variablesEnum")
    fun <T : Enum<T>> variables(elements: Map<T, String>) = ensureVariables().apply { elements.forEach { add(it.key, it.value) } }

    @JvmName("variablesEnum")
    fun <T : Enum<T>> variables(elements: Map<T, String>, block: VariablesDsl.() -> Unit) = ensureVariables().apply { elements.forEach { add(it.key, it.value) } }.apply(block)

    fun cache(block: CacheDsl.() -> Unit) = ensureCache().apply(block)
    fun cache(vararg elements: String) = cache(elements.toList())
    fun cache(elements: Iterable<String>) = ensureCache().apply { paths(elements) }

    fun artifacts(block: ArtifactsDsl.() -> Unit) = ensureArtifacts().apply(block)
    fun artifacts(vararg elements: String) = artifacts(elements.toList())
    fun artifacts(elements: Iterable<String>) = ensureArtifacts().apply { paths(elements) }

    override fun validate(errors: MutableList<String>) {
        val prefix = "[job name='$name']"

        addError(errors, isEmpty(name) || RESTRICTED_NAMES.contains(name), "$prefix name '$name' is incorrect")
        addError(errors, startIn != null && whenRun != WhenType.DELAYED, "$prefix startIn can be used only with when=delayed jobs")
        addError(errors, script == null, "$prefix at least one script command must be configured")
        addError(errors, parallel != null && (parallel!! < 2 || parallel!! > 50), "$prefix parallel must be in range [2, 50]")

        addErrors(errors, beforeScript, prefix)
        addErrors(errors, afterScript, prefix)
        addErrors(errors, inherit, prefix)
        addErrors(errors, inherit, prefix)
        addErrors(errors, image, prefix)
        addErrors(errors, script, prefix)
        addErrors(errors, services, prefix)
        addErrors(errors, needs, prefix)
        addErrors(errors, variables, prefix)
        addErrors(errors, cache, prefix)
        addErrors(errors, artifacts, prefix)
    }

    internal fun getName() = name ?: throw IllegalStateException("Job without name")

    private fun ensureInherit() = inherit ?: InheritDsl().also { inherit = it }
    private fun ensureImage() = image ?: ImageDsl().also { image = it }
    private fun ensureScript() = script ?: ScriptDsl().also { script = it }
    private fun ensureServices() = services ?: ServiceListDsl().also { services = it }
    private fun ensureNeeds() = needs ?: NeedsListDsl().also { needs = it }
    private fun ensureRetry() = retry ?: RetryDsl().also { retry = it }
    private fun ensureVariables() = variables ?: VariablesDsl().also { variables = it }
    private fun ensureCache() = cache ?: CacheDsl().also { cache = it }
    private fun ensureArtifacts() = artifacts ?: ArtifactsDsl().also { artifacts = it }
    private fun ensureBeforeScript() = beforeScript ?: BeforeScriptDsl().also { beforeScript = it }
    private fun ensureAfterScript() = afterScript ?: AfterScriptDsl().also { afterScript = it }
    private fun ensureTags() = tags ?: mutableSetOf<String>().also { tags = it }
    private fun ensureExtends() = extends ?: mutableListOf<String>().also { extends = it }
    private fun ensureDependencies() = dependencies ?: mutableSetOf<String>().also { dependencies = it }

    private companion object {
        val RESTRICTED_NAMES = listOf("image", "services", "stages", "types", "before_script", "after_script", "variables", "cache", "include")
    }
}

fun job(block: JobDsl.() -> Unit) = JobDsl().apply(block)
fun job(name: String, block: JobDsl.() -> Unit) = JobDsl(name).apply(block)

@Serializable(with = WhenType.WhenTypeSerializer::class)
enum class WhenType(override val stringRepresentation: String) : StringRepresentation {
    ON_SUCCESS("on_success"),
    ON_FAILURE("on_failure"),
    ALWAYS("always"),
    MANUAL("manual"),
    DELAYED("delayed");

    object WhenTypeSerializer : StringRepresentationSerializer<WhenType>("WhenType")
}