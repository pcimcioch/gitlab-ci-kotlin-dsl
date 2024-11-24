package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.Duration
import pcimcioch.gitlabci.dsl.StringRepresentation
import pcimcioch.gitlabci.dsl.include.IncludeDetailsDsl
import pcimcioch.gitlabci.dsl.serializer.StringRepresentationSerializer

@Serializable
class JobDsl(
    @Transient
    val name: String = ""
) : DslBase() {
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
    var cache: MutableList<CacheDsl>? = null
    var artifacts: ArtifactsDsl? = null
    var only: OnlyExceptDsl? = null
    var except: OnlyExceptDsl? = null
    var rules: RuleListDsl? = null

    @SerialName("before_script")
    var beforeScript: BeforeScriptDsl? = null
    var script: ScriptDsl? = null

    @SerialName("after_script")
    var afterScript: AfterScriptDsl? = null
    var release: ReleaseDsl? = null
    var trigger: TriggerDsl? = null
    var coverage: String? = null
    var environment: EnvironmentDsl? = null
    var variables: VariablesDsl? = null
    var secrets: SecretsDsl? = null

    fun script(block: ScriptDsl.() -> Unit = {}) = ensureScript().apply(block)
    fun script(vararg elements: String, block: ScriptDsl.() -> Unit = {}) = script(elements.toList(), block)
    fun script(elements: Iterable<String>, block: ScriptDsl.() -> Unit = {}) =
        ensureScript().apply { elements.forEach { exec(it) } }.apply(block)

    fun beforeScript(block: BeforeScriptDsl.() -> Unit = {}) = ensureBeforeScript().apply(block)
    fun beforeScript(vararg elements: String, block: BeforeScriptDsl.() -> Unit = {}) =
        beforeScript(elements.toList(), block)

    fun beforeScript(elements: Iterable<String>, block: BeforeScriptDsl.() -> Unit = {}) =
        ensureBeforeScript().apply { elements.forEach { exec(it) } }.apply(block)

    fun afterScript(block: AfterScriptDsl.() -> Unit = {}) = ensureAfterScript().apply(block)
    fun afterScript(vararg elements: String, block: AfterScriptDsl.() -> Unit = {}) =
        afterScript(elements.toList(), block)

    fun afterScript(elements: Iterable<String>, block: AfterScriptDsl.() -> Unit = {}) =
        ensureAfterScript().apply { elements.forEach { exec(it) } }.apply(block)

    fun inherit(block: InheritDsl.() -> Unit = {}) = ensureInherit().apply(block)

    fun image(name: String? = null, block: ImageDsl.() -> Unit = {}) =
        ensureImage().apply { this.name = name }.apply(block)

    fun releaseImage() {
        image = ImageDsl("registry.gitlab.com/gitlab-org/release-cli:latest")
    }

    fun release(tagName: String? = null, block: ReleaseDsl.() -> Unit = {}) =
        ensureRelease().apply { this.tagName = tagName }.apply(block)

    fun services(block: ServiceListDsl.() -> Unit = {}) = ensureServices().apply(block)
    fun services(vararg elements: String, block: ServiceListDsl.() -> Unit = {}) = services(elements.toList(), block)
    fun services(elements: Iterable<String>, block: ServiceListDsl.() -> Unit = {}) =
        ensureServices().apply { elements.forEach { service(it) } }.apply(block)

    fun needs(block: NeedsListDsl.() -> Unit = {}) = ensureNeeds().apply(block)
    fun needs(vararg elements: String, block: NeedsListDsl.() -> Unit = {}) = needs(elements.toList(), block)
    fun needs(elements: Iterable<String>, block: NeedsListDsl.() -> Unit = {}) =
        ensureNeeds().apply { elements.forEach { needJob(it) } }.apply(block)

    fun needs(vararg elements: JobDsl, block: NeedsListDsl.() -> Unit = {}) = needs(elements.toList(), block)

    @JvmName("needsJob")
    fun needs(elements: Iterable<JobDsl>, block: NeedsListDsl.() -> Unit = {}) =
        ensureNeeds().apply { elements.forEach { needJob(it) } }.apply(block)

    fun tags(vararg elements: String) = tags(elements.toList())
    fun tags(elements: Iterable<String>) = ensureTags().addAll(elements)

    fun retry(max: Int? = null, block: RetryDsl.() -> Unit = {}) = ensureRetry().apply { this.max = max }.apply(block)

    fun extends(vararg elements: String) = extends(elements.toList())
    fun extends(elements: Iterable<String>) = ensureExtends().addAll(elements)
    fun extends(vararg elements: JobDsl) = extends(elements.toList())
    fun extends(elements: Iterable<JobDsl>) = ensureExtends().apply { elements.forEach { add(it.name) } }

    fun dependencies(vararg elements: String) = dependencies(elements.toList())
    fun dependencies(elements: Iterable<String>) = ensureDependencies().addAll(elements)
    fun dependencies(vararg elements: JobDsl) = dependencies(elements.toList())
    fun dependencies(elements: Iterable<JobDsl>) = ensureDependencies().apply { elements.forEach { add(it.name) } }

    fun variables(block: VariablesDsl.() -> Unit = {}) = ensureVariables().apply(block)
    fun variables(elements: Map<String, Any>, block: VariablesDsl.() -> Unit = {}) =
        ensureVariables().apply { elements.forEach { add(it.key, it.value) } }.apply(block)

    @JvmName("variablesEnum")
    fun <T : Enum<T>> variables(elements: Map<T, Any>, block: VariablesDsl.() -> Unit = {}) =
        ensureVariables().apply { elements.forEach { add(it.key, it.value) } }.apply(block)

    fun secrets(block: SecretsDsl.() -> Unit = {}) = ensureSecrets().apply(block)
    fun secrets(elements: Map<String, String>, block: SecretsDsl.() -> Unit = {}) =
        ensureSecrets().apply { elements.forEach { add(it.key, it.value) } }.apply(block)

    @JvmName("secretsFromDsl")
    fun secrets(elements: Map<String, SecretDsl>, block: SecretsDsl.() -> Unit = {}) =
        ensureSecrets().apply { elements.forEach { add(it.key, it.value) } }.apply(block)

    fun cache(block: CacheDsl.() -> Unit = {}) = ensureCache().add(CacheDsl().apply(block))
    fun cache(vararg elements: String, block: CacheDsl.() -> Unit = {}) = cache(elements.toList(), block)
    fun cache(elements: Iterable<String>, block: CacheDsl.() -> Unit = {}) =
        ensureCache().add(CacheDsl().apply { paths(elements) }.apply(block))
    operator fun CacheDsl.unaryPlus() = this@JobDsl.ensureCache().add(this)

    fun artifacts(block: ArtifactsDsl.() -> Unit = {}) = ensureArtifacts().apply(block)
    fun artifacts(vararg elements: String, block: ArtifactsDsl.() -> Unit = {}) = artifacts(elements.toList(), block)
    fun artifacts(elements: Iterable<String>, block: ArtifactsDsl.() -> Unit = {}) =
        ensureArtifacts().apply { paths(elements) }.apply(block)

    fun only(block: OnlyExceptDsl.() -> Unit = {}) = ensureOnly().apply(block)
    fun only(vararg elements: String, block: OnlyExceptDsl.() -> Unit = {}) = only(elements.toList(), block)
    fun only(elements: Iterable<String>, block: OnlyExceptDsl.() -> Unit = {}) =
        ensureOnly().apply { refs(elements) }.apply(block)

    fun except(block: OnlyExceptDsl.() -> Unit = {}) = ensureExcept().apply(block)
    fun except(vararg elements: String, block: OnlyExceptDsl.() -> Unit = {}) = except(elements.toList(), block)
    fun except(elements: Iterable<String>, block: OnlyExceptDsl.() -> Unit = {}) =
        ensureExcept().apply { refs(elements) }.apply(block)

    fun rules(block: RuleListDsl.() -> Unit = {}) = ensureRules().apply(block)

    fun environment(name: String? = null, block: EnvironmentDsl.() -> Unit = {}) =
        ensureEnvironment().apply { this.name = name }.apply(block)

    fun trigger(
        project: String? = null,
        branch: String? = null,
        strategy: TriggerStrategy? = null,
        block: TriggerDsl.() -> Unit = {}
    ) = ensureTrigger().apply {
        this.project = project
        this.branch = branch
        this.strategy = strategy
    }.apply(block)

    override fun validate(errors: MutableList<String>) {
        val prefix = "[job name='$name']"

        addError(
            errors,
            isEmpty(name) || Validation.RESTRICTED_NAMES.contains(name),
            "$prefix name '$name' is incorrect"
        )
        addError(
            errors,
            startIn != null && whenRun != WhenRunType.DELAYED,
            "$prefix startIn can be used only with when=delayed jobs"
        )
        addError(
            errors,
            parallel != null && (parallel!! < 2 || parallel!! > 50),
            "$prefix parallel must be in range [2, 50]"
        )

        addErrors(
            errors, prefix, beforeScript, afterScript, inherit, retry, image, script, services, needs, variables,
            artifacts, only, except, rules, environment, trigger, release, secrets
        )

        cache?.forEach {
            addErrors(errors, prefix, it)
        }
    }

    private fun ensureInherit() = inherit ?: InheritDsl().also { inherit = it }
    private fun ensureImage() = image ?: ImageDsl().also { image = it }
    private fun ensureRelease() = release ?: ReleaseDsl().also { release = it }
    private fun ensureScript() = script ?: ScriptDsl().also { script = it }
    private fun ensureServices() = services ?: ServiceListDsl().also { services = it }
    private fun ensureNeeds() = needs ?: NeedsListDsl().also { needs = it }
    private fun ensureRetry() = retry ?: RetryDsl().also { retry = it }
    private fun ensureVariables() = variables ?: VariablesDsl().also { variables = it }
    private fun ensureSecrets() = secrets ?: SecretsDsl().also { secrets = it }
    private fun ensureCache() = cache ?: mutableListOf<CacheDsl>().also { cache = it }
    private fun ensureArtifacts() = artifacts ?: ArtifactsDsl().also { artifacts = it }
    private fun ensureOnly() = only ?: OnlyExceptDsl().also { only = it }
    private fun ensureExcept() = except ?: OnlyExceptDsl().also { except = it }
    private fun ensureRules() = rules ?: RuleListDsl().also { rules = it }
    private fun ensureBeforeScript() = beforeScript ?: BeforeScriptDsl().also { beforeScript = it }
    private fun ensureAfterScript() = afterScript ?: AfterScriptDsl().also { afterScript = it }
    private fun ensureTags() = tags ?: mutableSetOf<String>().also { tags = it }
    private fun ensureExtends() = extends ?: mutableListOf<String>().also { extends = it }
    private fun ensureDependencies() = dependencies ?: mutableSetOf<String>().also { dependencies = it }
    private fun ensureEnvironment() = environment ?: EnvironmentDsl().also { environment = it }
    private fun ensureTrigger() = trigger ?: TriggerDsl().also { trigger = it }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JobDsl

        if (name != other.name) return false
        if (extends != other.extends) return false
        if (image != other.image) return false
        if (stage != other.stage) return false
        if (tags != other.tags) return false
        if (inherit != other.inherit) return false
        if (allowFailure != other.allowFailure) return false
        if (whenRun != other.whenRun) return false
        if (startIn != other.startIn) return false
        if (timeout != other.timeout) return false
        if (retry != other.retry) return false
        if (interruptible != other.interruptible) return false
        if (parallel != other.parallel) return false
        if (resourceGroup != other.resourceGroup) return false
        if (services != other.services) return false
        if (needs != other.needs) return false
        if (dependencies != other.dependencies) return false
        if (cache != other.cache) return false
        if (artifacts != other.artifacts) return false
        if (only != other.only) return false
        if (except != other.except) return false
        if (rules != other.rules) return false
        if (beforeScript != other.beforeScript) return false
        if (script != other.script) return false
        if (afterScript != other.afterScript) return false
        if (release != other.release) return false
        if (trigger != other.trigger) return false
        if (coverage != other.coverage) return false
        if (environment != other.environment) return false
        if (variables != other.variables) return false
        if (secrets != other.secrets) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (extends?.hashCode() ?: 0)
        result = 31 * result + (image?.hashCode() ?: 0)
        result = 31 * result + (stage?.hashCode() ?: 0)
        result = 31 * result + (tags?.hashCode() ?: 0)
        result = 31 * result + (inherit?.hashCode() ?: 0)
        result = 31 * result + (allowFailure?.hashCode() ?: 0)
        result = 31 * result + (whenRun?.hashCode() ?: 0)
        result = 31 * result + (startIn?.hashCode() ?: 0)
        result = 31 * result + (timeout?.hashCode() ?: 0)
        result = 31 * result + (retry?.hashCode() ?: 0)
        result = 31 * result + (interruptible?.hashCode() ?: 0)
        result = 31 * result + (parallel ?: 0)
        result = 31 * result + (resourceGroup?.hashCode() ?: 0)
        result = 31 * result + (services?.hashCode() ?: 0)
        result = 31 * result + (needs?.hashCode() ?: 0)
        result = 31 * result + (dependencies?.hashCode() ?: 0)
        result = 31 * result + (cache?.hashCode() ?: 0)
        result = 31 * result + (artifacts?.hashCode() ?: 0)
        result = 31 * result + (only?.hashCode() ?: 0)
        result = 31 * result + (except?.hashCode() ?: 0)
        result = 31 * result + (rules?.hashCode() ?: 0)
        result = 31 * result + (beforeScript?.hashCode() ?: 0)
        result = 31 * result + (script?.hashCode() ?: 0)
        result = 31 * result + (afterScript?.hashCode() ?: 0)
        result = 31 * result + (release?.hashCode() ?: 0)
        result = 31 * result + (trigger?.hashCode() ?: 0)
        result = 31 * result + (coverage?.hashCode() ?: 0)
        result = 31 * result + (environment?.hashCode() ?: 0)
        result = 31 * result + (variables?.hashCode() ?: 0)
        result = 31 * result + (secrets?.hashCode() ?: 0)
        return result
    }

    private object Validation {
        val RESTRICTED_NAMES = listOf(
            "image",
            "services",
            "stages",
            "types",
            "before_script",
            "after_script",
            "variables",
            "cache",
            "include"
        )
    }

    companion object {
        init {
            addSerializer(JobDsl::class, serializer())
        }
    }
}

fun createJob(name: String, block: JobDsl.() -> Unit) = JobDsl(name).apply(block)

@Serializable(with = WhenRunType.WhenRunTypeSerializer::class)
enum class WhenRunType(
    override val stringRepresentation: String
) : StringRepresentation {
    ON_SUCCESS("on_success"),
    ON_FAILURE("on_failure"),
    ALWAYS("always"),
    MANUAL("manual"),
    DELAYED("delayed"),
    NEVER("never");

    object WhenRunTypeSerializer : StringRepresentationSerializer<WhenRunType>("WhenRunType")
}