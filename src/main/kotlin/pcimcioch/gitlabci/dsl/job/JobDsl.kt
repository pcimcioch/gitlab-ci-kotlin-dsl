package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.DslBase.Companion.addErrors
import pcimcioch.gitlabci.dsl.Duration
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.StringRepresentation
import pcimcioch.gitlabci.dsl.isEmpty
import pcimcioch.gitlabci.dsl.serializer.StringRepresentationSerializer

@GitlabCiDslMarker
@Serializable
class JobDsl(
        @Transient
        val name: String = ""
) : DslBase {
    var extends: MutableList<String>? = null
    var image: ImageDsl? = null
    var stage: String? = null
    var tags: MutableSet<String>? = null
    var inherit: InheritDsl? = null

    @SerialName("allow_failure")
    var allowFailure: Boolean? = null

    @SerialName("when")
    var whenRun: WhenRunType? = null

    @SerialName("start_in")
    var startIn: Duration? = null
    var timeout: Duration? = null
    var retry: RetryDsl? = null
    var interruptible: Boolean? = null
    var parallel: Int? = null

    @SerialName("resource_group")
    var resourceGroup: String? = null
    var services: ServiceListDsl? = null
    var needs: NeedsListDsl? = null
    var dependencies: MutableSet<String>? = null
    var cache: CacheDsl? = null
    var artifacts: ArtifactsDsl? = null
    var only: OnlyExceptDsl? = null
    var except: OnlyExceptDsl? = null

    @SerialName("before_script")
    var beforeScript: BeforeScriptDsl? = null
    var script: ScriptDsl? = null

    @SerialName("after_script")
    var afterScript: AfterScriptDsl? = null
    var coverage: String? = null
    var variables: VariablesDsl? = null

    fun script(block: ScriptDsl.() -> Unit) = ensureScript().apply(block)
    fun script(vararg elements: String) = script(elements.toList())
    fun script(elements: Iterable<String>) = ensureScript().apply { elements.forEach { exec(it) } }

    fun beforeScript(block: BeforeScriptDsl.() -> Unit) = ensureBeforeScript().apply(block)
    fun beforeScript(vararg elements: String) = beforeScript(elements.toList())
    fun beforeScript(elements: Iterable<String>) = ensureBeforeScript().apply { elements.forEach { exec(it) } }

    fun afterScript(block: AfterScriptDsl.() -> Unit) = ensureAfterScript().apply(block)
    fun afterScript(vararg elements: String) = afterScript(elements.toList())
    fun afterScript(elements: Iterable<String>) = ensureAfterScript().apply { elements.forEach { exec(it) } }

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

    fun tags(vararg elements: String) = tags(elements.toList())
    fun tags(elements: Iterable<String>) = ensureTags().addAll(elements)

    fun retry(max: Int) = ensureRetry().apply { this.max = max }
    fun retry(block: RetryDsl.() -> Unit) = ensureRetry().apply(block)
    fun retry(max: Int, block: RetryDsl.() -> Unit) = ensureRetry().apply { this.max = max }.apply(block)

    fun extends(vararg elements: String) = extends(elements.toList())
    fun extends(elements: Iterable<String>) = ensureExtends().addAll(elements)
    fun extends(vararg elements: JobDsl) = extends(elements.toList())
    fun extends(elements: Iterable<JobDsl>) = ensureExtends().apply { elements.forEach { add(it.name) } }

    fun dependencies(vararg elements: String) = dependencies(elements.toList())
    fun dependencies(elements: Iterable<String>) = ensureDependencies().addAll(elements)
    fun dependencies(vararg elements: JobDsl) = dependencies(elements.toList())
    fun dependencies(elements: Iterable<JobDsl>) = ensureDependencies().apply { elements.forEach { add(it.name) } }

    fun variables(block: VariablesDsl.() -> Unit) = ensureVariables().apply(block)
    fun variables(elements: Map<String, Any>) = ensureVariables().apply { elements.forEach { add(it.key, it.value) } }
    fun variables(elements: Map<String, Any>, block: VariablesDsl.() -> Unit) = ensureVariables().apply { elements.forEach { add(it.key, it.value) } }.apply(block)

    @JvmName("variablesEnum")
    fun <T : Enum<T>> variables(elements: Map<T, Any>) = ensureVariables().apply { elements.forEach { add(it.key, it.value) } }

    @JvmName("variablesEnum")
    fun <T : Enum<T>> variables(elements: Map<T, Any>, block: VariablesDsl.() -> Unit) = ensureVariables().apply { elements.forEach { add(it.key, it.value) } }.apply(block)

    fun cache(block: CacheDsl.() -> Unit) = ensureCache().apply(block)
    fun cache(vararg elements: String) = cache(elements.toList())
    fun cache(elements: Iterable<String>) = ensureCache().apply { paths(elements) }

    fun artifacts(block: ArtifactsDsl.() -> Unit) = ensureArtifacts().apply(block)
    fun artifacts(vararg elements: String) = artifacts(elements.toList())
    fun artifacts(elements: Iterable<String>) = ensureArtifacts().apply { paths(elements) }

    fun only(block: OnlyExceptDsl.() -> Unit) = ensureOnly().apply(block)
    fun only(vararg elements: String) = only(elements.toList())
    fun only(elements: Iterable<String>) = ensureOnly().apply { refs(elements) }

    fun except(block: OnlyExceptDsl.() -> Unit) = ensureExcept().apply(block)
    fun except(vararg elements: String) = except(elements.toList())
    fun except(elements: Iterable<String>) = ensureExcept().apply { refs(elements) }

    override fun validate(errors: MutableList<String>) {
        val prefix = "[job name='$name']"

        addError(errors, isEmpty(name) || Validation.RESTRICTED_NAMES.contains(name), "$prefix name '$name' is incorrect")
        addError(errors, startIn != null && whenRun != WhenRunType.DELAYED, "$prefix startIn can be used only with when=delayed jobs")
        addError(errors, script == null, "$prefix at least one script command must be configured")
        addError(errors, parallel != null && (parallel!! < 2 || parallel!! > 50), "$prefix parallel must be in range [2, 50]")

        addErrors(errors, beforeScript, prefix)
        addErrors(errors, afterScript, prefix)
        addErrors(errors, inherit, prefix)
        addErrors(errors, retry, prefix)
        addErrors(errors, image, prefix)
        addErrors(errors, script, prefix)
        addErrors(errors, services, prefix)
        addErrors(errors, needs, prefix)
        addErrors(errors, variables, prefix)
        addErrors(errors, cache, prefix)
        addErrors(errors, artifacts, prefix)
        addErrors(errors, only, prefix)
        addErrors(errors, except, prefix)
    }

    private fun ensureInherit() = inherit ?: InheritDsl().also { inherit = it }
    private fun ensureImage() = image ?: ImageDsl().also { image = it }
    private fun ensureScript() = script ?: ScriptDsl().also { script = it }
    private fun ensureServices() = services ?: ServiceListDsl().also { services = it }
    private fun ensureNeeds() = needs ?: NeedsListDsl().also { needs = it }
    private fun ensureRetry() = retry ?: RetryDsl().also { retry = it }
    private fun ensureVariables() = variables ?: VariablesDsl().also { variables = it }
    private fun ensureCache() = cache ?: CacheDsl().also { cache = it }
    private fun ensureArtifacts() = artifacts ?: ArtifactsDsl().also { artifacts = it }
    private fun ensureOnly() = only ?: OnlyExceptDsl().also { only = it }
    private fun ensureExcept() = except ?: OnlyExceptDsl().also { except = it }
    private fun ensureBeforeScript() = beforeScript ?: BeforeScriptDsl().also { beforeScript = it }
    private fun ensureAfterScript() = afterScript ?: AfterScriptDsl().also { afterScript = it }
    private fun ensureTags() = tags ?: mutableSetOf<String>().also { tags = it }
    private fun ensureExtends() = extends ?: mutableListOf<String>().also { extends = it }
    private fun ensureDependencies() = dependencies ?: mutableSetOf<String>().also { dependencies = it }

    private object Validation {
        val RESTRICTED_NAMES = listOf("image", "services", "stages", "types", "before_script", "after_script", "variables", "cache", "include")
    }
}

fun createJob(name: String, block: JobDsl.() -> Unit) = JobDsl(name).apply(block)

@Serializable(with = WhenRunType.WhenRuntTypeSerializer::class)
enum class WhenRunType(
        override val stringRepresentation: String
) : StringRepresentation {
    ON_SUCCESS("on_success"),
    ON_FAILURE("on_failure"),
    ALWAYS("always"),
    MANUAL("manual"),
    DELAYED("delayed");

    object WhenRuntTypeSerializer : StringRepresentationSerializer<WhenRunType>("WhenRunType")
}