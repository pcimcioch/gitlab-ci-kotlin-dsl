package pcimcioch.gitlabci.dsl.default

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addErrors
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.job.AfterScriptDsl
import pcimcioch.gitlabci.dsl.job.BeforeScriptDsl
import pcimcioch.gitlabci.dsl.job.CacheDsl
import pcimcioch.gitlabci.dsl.job.ImageDsl
import pcimcioch.gitlabci.dsl.job.ServiceListDsl

@GitlabCiDslMarker
@Serializable
class DefaultDsl : DslBase {
    var image: ImageDsl? = null
    var services: ServiceListDsl? = null
    var cache: CacheDsl? = null

    @SerialName("before_script")
    var beforeScript: BeforeScriptDsl? = null

    @SerialName("after_script")
    var afterScript: AfterScriptDsl? = null

    fun beforeScript(block: BeforeScriptDsl.() -> Unit) = ensureBeforeScript().apply(block)
    fun beforeScript(vararg elements: String) = beforeScript(elements.toList())
    fun beforeScript(elements: Iterable<String>) = ensureBeforeScript().apply { elements.forEach { exec(it) } }

    fun afterScript(block: AfterScriptDsl.() -> Unit) = ensureAfterScript().apply(block)
    fun afterScript(vararg elements: String) = afterScript(elements.toList())
    fun afterScript(elements: Iterable<String>) = ensureAfterScript().apply { elements.forEach { exec(it) } }

    fun image(name: String) = ensureImage().apply { this.name = name }
    fun image(block: ImageDsl.() -> Unit) = ensureImage().apply(block)
    fun image(name: String, block: ImageDsl.() -> Unit) = ensureImage().apply { this.name = name }.apply(block)

    fun services(vararg elements: String) = services(elements.toList())
    fun services(elements: Iterable<String>) = ensureServices().apply { elements.forEach { service(it) } }
    fun services(block: ServiceListDsl.() -> Unit) = ensureServices().apply(block)

    fun cache(block: CacheDsl.() -> Unit) = ensureCache().apply(block)
    fun cache(vararg elements: String) = cache(elements.toList())
    fun cache(elements: Iterable<String>) = ensureCache().apply { paths(elements) }

    override fun validate(errors: MutableList<String>) {
        val prefix = "[default]"

        addErrors(errors, beforeScript, prefix)
        addErrors(errors, afterScript, prefix)
        addErrors(errors, image, prefix)
        addErrors(errors, services, prefix)
        addErrors(errors, cache, prefix)
    }

    private fun ensureImage() = image ?: ImageDsl().also { image = it }
    private fun ensureServices() = services ?: ServiceListDsl().also { services = it }
    private fun ensureCache() = cache ?: CacheDsl().also { cache = it }
    private fun ensureBeforeScript() = beforeScript ?: BeforeScriptDsl().also { beforeScript = it }
    private fun ensureAfterScript() = afterScript ?: AfterScriptDsl().also { afterScript = it }
}