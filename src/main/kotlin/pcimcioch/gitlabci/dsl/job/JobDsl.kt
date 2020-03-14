package pcimcioch.gitlabci.dsl.job

import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.DslBase.Companion.addErrors
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.WhenType
import pcimcioch.gitlabci.dsl.addAndReturn
import pcimcioch.gitlabci.dsl.isEmpty
import pcimcioch.gitlabci.dsl.stage.StageDsl
import java.time.Duration

@GitlabCiDslMarker
class JobDsl : DslBase {
    var name: String? = null
    var inherit: InheritDsl? = null
    var image: ImageDsl? = null
    var stage: String? = null
    var allowFailure: Boolean? = null
    var whenRun: WhenType? = null
    var startIn: Duration? = null
    private var script: ScriptDsl? = null
    private val services: MutableList<ServiceDsl> = mutableListOf()

    constructor()
    constructor(name: String) {
        this.name = name
    }

    fun script(block: ScriptDsl.() -> Unit): ScriptDsl {
        script = script ?: ScriptDsl()
        return script?.apply(block)!!
    }

    fun inherit(block: InheritDsl.() -> Unit): InheritDsl {
        inherit = inherit ?: InheritDsl()
        return inherit?.apply(block)!!
    }

    fun image(name: String): ImageDsl {
        image = image ?: ImageDsl()
        image?.name = name
        return image!!
    }

    fun image(block: ImageDsl.() -> Unit): ImageDsl {
        image = image ?: ImageDsl()
        return image?.apply(block)!!
    }

    fun image(name: String, block: ImageDsl.() -> Unit): ImageDsl {
        image = image ?: ImageDsl()
        image?.name = name
        return image?.apply(block)!!
    }

    fun service(block: ServiceDsl.() -> Unit) = addAndReturn(services, ServiceDsl()).apply(block)
    fun service(name: String) = addAndReturn(services, ServiceDsl(name))
    fun service(name: String, block: ServiceDsl.() -> Unit) = addAndReturn(services, ServiceDsl(name)).apply(block)
    operator fun ServiceDsl.unaryPlus() = this@JobDsl.services.add(this)

    fun services(vararg elements: String) = services(elements.toList())
    fun services(elements: Iterable<String>) = elements.forEach { services.add(ServiceDsl(it)) }

    fun stage(value: StageDsl) {
        stage = value.name
    }

    override fun validate(errors: MutableList<String>) {
        val prefix = "[job name='$name']"

        addError(errors, isEmpty(name) || RESTRICTED_NAMES.contains(name), "$prefix name '$name' is incorrect")
        addError(errors, startIn != null && whenRun != WhenType.DELAYED, "$prefix startIn can be used only with when=delayed jobs")
        addError(errors, script?.commands?.isEmpty() != false, "$prefix at least one script command must be configured")

        addErrors(errors, inherit, prefix)
        addErrors(errors, image, prefix)
        addErrors(errors, script, prefix)
        addErrors(errors, services, prefix)
    }

    private companion object {
        val RESTRICTED_NAMES = listOf("image", "services", "stages", "types", "before_script", "after_script", "variables", "cache", "include")
    }
}

fun job(block: JobDsl.() -> Unit) = JobDsl().apply(block)
fun job(name: String, block: JobDsl.() -> Unit) = JobDsl(name).apply(block)
