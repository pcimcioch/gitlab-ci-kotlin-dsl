package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.DslBase.Companion.addErrors
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.StringRepresentation
import pcimcioch.gitlabci.dsl.serializer.StringRepresentationSerializer

// TODO tests
@GitlabCiDslMarker
class CacheDsl : DslBase {
    var paths: MutableSet<String>? = null
    var untracked: Boolean? = null
    var policy: CachePolicy? = null

    // TODO maybe those should be somehow available to read? same for inherit
    private var keyString: String? = null
    private var keyDsl: CacheKeyDsl? = null

    fun paths(vararg elements: String) = paths(elements.toList())
    fun paths(elements: Iterable<String>) = ensurePaths().addAll(elements)

    fun key(key: String) {
        keyDsl = null
        keyString = key
    }

    fun key(block: CacheKeyDsl.() -> Unit) {
        keyString = null
        ensureKeyDsl().apply(block)
    }

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, keyDsl, "[cache]")
    }

    private fun ensureKeyDsl(): CacheKeyDsl = keyDsl ?: CacheKeyDsl().also { keyDsl = it }
    private fun ensurePaths() = paths ?: mutableSetOf<String>().also { paths = it }
}

fun cache(block: CacheDsl.() -> Unit) = CacheDsl().apply(block)
fun cache(vararg elements: String) = cache(elements.toList())
fun cache(elements: Iterable<String>) = CacheDsl().apply { paths(elements) }

@GitlabCiDslMarker
class CacheKeyDsl : DslBase {
    var prefix: String? = null
    var files: MutableSet<String> = mutableSetOf()

    fun files(vararg elements: String) = files(elements.toList())
    fun files(elements: Iterable<String>) = files.addAll(elements)

    override fun validate(errors: MutableList<String>) {
        addError(errors, files.isEmpty(), "[key] files list can't be empty")
        addError(errors,
                "." == prefix || "%2E" == prefix || "%2e" == prefix,
                "[key] prefix value '$prefix' can't be '.' nor '%2E'")
        addError(errors,
                prefix?.contains("/") == true || prefix?.contains("%2F") == true || prefix?.contains("%2f") == true,
                "[key] prefix value '$prefix' can't contain '/' nor '%2F'")
    }
}

fun key(block: CacheKeyDsl.() -> Unit) = CacheKeyDsl().apply(block)

@Serializable(with = CachePolicy.CachePolicySerializer::class)
enum class CachePolicy(
        override val stringRepresentation: String
) : StringRepresentation {
    PULL("push"),
    PULL_PUSH("pull-push"),
    PUSH("push");

    object CachePolicySerializer : StringRepresentationSerializer<CachePolicy>("CachePolicy")
}