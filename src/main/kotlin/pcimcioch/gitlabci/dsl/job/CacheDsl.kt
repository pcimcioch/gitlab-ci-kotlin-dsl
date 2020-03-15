package pcimcioch.gitlabci.dsl.job

import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.DslBase.Companion.addErrors
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker

@GitlabCiDslMarker
class CacheDsl : DslBase {
    var paths: MutableSet<String> = mutableSetOf()
    var untracked: Boolean? = null
    var policy: CachePolicy? = null

    // TODO maybe those should be somehow awailable to read. same for inherit
    private var keyString: String? = null
    private var keyDsl: CacheKeyDsl? = null

    fun paths(vararg elements: String) = paths(elements.toList())
    fun paths(elements: Iterable<String>) = paths.addAll(elements)

    fun key(key: String) {
        keyDsl = null
        keyString = key
    }

    fun key(block: CacheKeyDsl.() -> Unit) {
        keyString = null
        ensureKeyDsl().apply(block)
    }

    private fun ensureKeyDsl(): CacheKeyDsl = keyDsl ?: CacheKeyDsl().also { keyDsl = it }

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, keyDsl, "[cache]")
    }
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
        addError(errors,
                "." == prefix || "%2E" == prefix || "%2e" == prefix,
                "[key] prefix value '$prefix' can't be '.' nor '%2E'")
        addError(errors,
                prefix?.contains("/") == true || prefix?.contains("%2F") == true || prefix?.contains("%2f") == true,
                "[key] prefix value '$prefix' can't contain '/' nor '%2F'")
    }
}

fun key(block: CacheKeyDsl.() -> Unit) = CacheKeyDsl().apply(block)

enum class CachePolicy {
    // TODO maybe set names up explicitely?
    PULL,
    PULL_PUSH,
    PUSH;

    override fun toString(): String {
        return super.toString().toLowerCase().replace('_', '-')
    }
}