package pcimcioch.gitlabci.dsl.default

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.Duration
import pcimcioch.gitlabci.dsl.job.*

@Serializable
class DefaultDsl : DslBase() {
    var image: ImageDsl? = null
    var services: ServiceListDsl? = null
    var cache: CacheDsl? = null
    var tags: MutableSet<String>? = null
    var artifacts: ArtifactsDsl? = null
    var retry: RetryDsl? = null
    var timeout: Duration? = null
    var interruptible: Boolean? = null

    @SerialName("before_script")
    var beforeScript: BeforeScriptDsl? = null

    @SerialName("after_script")
    var afterScript: AfterScriptDsl? = null

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

    fun image(name: String? = null, block: ImageDsl.() -> Unit = {}) =
        ensureImage().apply { this.name = name }.apply(block)

    fun services(block: ServiceListDsl.() -> Unit = {}) = ensureServices().apply(block)
    fun services(vararg elements: String, block: ServiceListDsl.() -> Unit = {}) = services(elements.toList(), block)
    fun services(elements: Iterable<String>, block: ServiceListDsl.() -> Unit = {}) =
        ensureServices().apply { elements.forEach { service(it) } }.apply(block)

    fun cache(block: CacheDsl.() -> Unit = {}) = ensureCache().apply(block)
    fun cache(vararg elements: String, block: CacheDsl.() -> Unit = {}) = cache(elements.toList(), block)
    fun cache(elements: Iterable<String>, block: CacheDsl.() -> Unit = {}) =
        ensureCache().apply { paths(elements) }.apply(block)

    fun tags(vararg elements: String) = tags(elements.toList())
    fun tags(elements: Iterable<String>) = ensureTags().addAll(elements)

    fun artifacts(block: ArtifactsDsl.() -> Unit = {}) = ensureArtifacts().apply(block)
    fun artifacts(vararg elements: String, block: ArtifactsDsl.() -> Unit = {}) = artifacts(elements.toList(), block)
    fun artifacts(elements: Iterable<String>, block: ArtifactsDsl.() -> Unit = {}) =
        ensureArtifacts().apply { paths(elements) }.apply(block)

    fun retry(max: Int? = null, block: RetryDsl.() -> Unit = {}) = ensureRetry().apply { this.max = max }.apply(block)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, "[default]", beforeScript, afterScript, image, services, cache, artifacts, retry)
    }

    private fun ensureImage() = image ?: ImageDsl().also { image = it }
    private fun ensureServices() = services ?: ServiceListDsl().also { services = it }
    private fun ensureTags() = tags ?: mutableSetOf<String>().also { tags = it }
    private fun ensureArtifacts() = artifacts ?: ArtifactsDsl().also { artifacts = it }
    private fun ensureRetry() = retry ?: RetryDsl().also { retry = it }
    private fun ensureCache() = cache ?: CacheDsl().also { cache = it }
    private fun ensureBeforeScript() = beforeScript ?: BeforeScriptDsl().also { beforeScript = it }
    private fun ensureAfterScript() = afterScript ?: AfterScriptDsl().also { afterScript = it }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DefaultDsl

        if (image != other.image) return false
        if (services != other.services) return false
        if (cache != other.cache) return false
        if (tags != other.tags) return false
        if (artifacts != other.artifacts) return false
        if (retry != other.retry) return false
        if (timeout != other.timeout) return false
        if (interruptible != other.interruptible) return false
        if (beforeScript != other.beforeScript) return false
        if (afterScript != other.afterScript) return false

        return true
    }

    override fun hashCode(): Int {
        var result = image?.hashCode() ?: 0
        result = 31 * result + (services?.hashCode() ?: 0)
        result = 31 * result + (cache?.hashCode() ?: 0)
        result = 31 * result + (tags?.hashCode() ?: 0)
        result = 31 * result + (artifacts?.hashCode() ?: 0)
        result = 31 * result + (retry?.hashCode() ?: 0)
        result = 31 * result + (timeout?.hashCode() ?: 0)
        result = 31 * result + (interruptible?.hashCode() ?: 0)
        result = 31 * result + (beforeScript?.hashCode() ?: 0)
        result = 31 * result + (afterScript?.hashCode() ?: 0)
        return result
    }

    companion object {
        init {
            addSerializer(DefaultDsl::class, serializer())
        }
    }
}