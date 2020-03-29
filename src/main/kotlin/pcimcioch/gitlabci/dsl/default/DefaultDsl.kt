package pcimcioch.gitlabci.dsl.default

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.job.AfterScriptDsl
import pcimcioch.gitlabci.dsl.job.BeforeScriptDsl
import pcimcioch.gitlabci.dsl.job.CacheDsl
import pcimcioch.gitlabci.dsl.job.ImageDsl
import pcimcioch.gitlabci.dsl.job.ServiceListDsl

@Serializable
class DefaultDsl : DslBase() {
    var image: ImageDsl? = null
    var services: ServiceListDsl? = null
    var cache: CacheDsl? = null

    @SerialName("before_script")
    var beforeScript: BeforeScriptDsl? = null

    @SerialName("after_script")
    var afterScript: AfterScriptDsl? = null

    fun beforeScript(block: BeforeScriptDsl.() -> Unit = {}) = ensureBeforeScript().apply(block)
    fun beforeScript(vararg elements: String, block: BeforeScriptDsl.() -> Unit = {}) = beforeScript(elements.toList(), block)
    fun beforeScript(elements: Iterable<String>, block: BeforeScriptDsl.() -> Unit = {}) = ensureBeforeScript().apply { elements.forEach { exec(it) } }.apply(block)

    fun afterScript(block: AfterScriptDsl.() -> Unit = {}) = ensureAfterScript().apply(block)
    fun afterScript(vararg elements: String, block: AfterScriptDsl.() -> Unit = {}) = afterScript(elements.toList(), block)
    fun afterScript(elements: Iterable<String>, block: AfterScriptDsl.() -> Unit = {}) = ensureAfterScript().apply { elements.forEach { exec(it) } }.apply(block)

    fun image(name: String? = null, block: ImageDsl.() -> Unit = {}) = ensureImage().apply { this.name = name }.apply(block)

    fun services(block: ServiceListDsl.() -> Unit = {}) = ensureServices().apply(block)
    fun services(vararg elements: String, block: ServiceListDsl.() -> Unit = {}) = services(elements.toList(), block)
    fun services(elements: Iterable<String>, block: ServiceListDsl.() -> Unit = {}) = ensureServices().apply { elements.forEach { service(it) } }.apply(block)

    fun cache(block: CacheDsl.() -> Unit = {}) = ensureCache().apply(block)
    fun cache(vararg elements: String, block: CacheDsl.() -> Unit = {}) = cache(elements.toList(), block)
    fun cache(elements: Iterable<String>, block: CacheDsl.() -> Unit = {}) = ensureCache().apply { paths(elements) }.apply(block)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, "[default]", beforeScript, afterScript, image, services, cache)
    }

    private fun ensureImage() = image ?: ImageDsl().also { image = it }
    private fun ensureServices() = services ?: ServiceListDsl().also { services = it }
    private fun ensureCache() = cache ?: CacheDsl().also { cache = it }
    private fun ensureBeforeScript() = beforeScript ?: BeforeScriptDsl().also { beforeScript = it }
    private fun ensureAfterScript() = afterScript ?: AfterScriptDsl().also { afterScript = it }

    companion object {
        init {
            addSerializer(DefaultDsl::class, serializer())
        }
    }
}