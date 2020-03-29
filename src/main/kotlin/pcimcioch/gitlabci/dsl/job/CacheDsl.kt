package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.serializer
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.StringRepresentation
import pcimcioch.gitlabci.dsl.serializer.MultiTypeSerializer
import pcimcioch.gitlabci.dsl.serializer.StringRepresentationSerializer

@Serializable
class CacheDsl : DslBase() {
    var paths: MutableSet<String>? = null
    var untracked: Boolean? = null
    var policy: CachePolicy? = null

    @Transient
    private var keyString: String? = null

    @Transient
    private var keyDsl: CacheKeyDsl? = null

    @Serializable(with = KeySerializer::class)
    var key: Any? = null
        get() = keyString ?: keyDsl
        private set

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
        addErrors(errors, "[cache]", keyDsl)
    }

    private fun ensureKeyDsl(): CacheKeyDsl = keyDsl ?: CacheKeyDsl().also { keyDsl = it }
    private fun ensurePaths() = paths ?: mutableSetOf<String>().also { paths = it }

    object KeySerializer : MultiTypeSerializer<Any>(
            PrimitiveDescriptor("Key", PrimitiveKind.STRING),
            mapOf(
                    String::class to String.serializer(),
                    CacheKeyDsl::class to CacheKeyDsl.serializer()))

    companion object {
        init {
            addSerializer(CacheDsl::class, serializer())
        }
    }
}

fun createCache(block: CacheDsl.() -> Unit = {}) = CacheDsl().apply(block)
fun createCache(vararg elements: String, block: CacheDsl.() -> Unit = {}) = createCache(elements.toList(), block)
fun createCache(elements: Iterable<String>, block: CacheDsl.() -> Unit = {}) = CacheDsl().apply { paths(elements) }.apply(block)

@Serializable
class CacheKeyDsl : DslBase() {
    var prefix: String? = null
    var files: MutableSet<String>? = null

    fun files(vararg elements: String) = files(elements.toList())
    fun files(elements: Iterable<String>) = ensureFiles().addAll(elements)

    override fun validate(errors: MutableList<String>) {
        addError(errors, files?.isNotEmpty() != true, "[key] files list can't be empty")
        addError(errors,
                "." == prefix || "%2E" == prefix || "%2e" == prefix,
                "[key] prefix value '$prefix' can't be '.' nor '%2E'")
        addError(errors,
                prefix?.contains("/") == true || prefix?.contains("%2F") == true || prefix?.contains("%2f") == true,
                "[key] prefix value '$prefix' can't contain '/' nor '%2F'")
    }

    private fun ensureFiles() = files ?: mutableSetOf<String>().also { files = it }

    companion object {
        init {
            addSerializer(CacheKeyDsl::class, serializer())
        }
    }
}

fun createCacheKey(block: CacheKeyDsl.() -> Unit = {}) = CacheKeyDsl().apply(block)

@Serializable(with = CachePolicy.CachePolicySerializer::class)
enum class CachePolicy(
        override val stringRepresentation: String
) : StringRepresentation {
    PULL("push"),
    PULL_PUSH("pull-push"),
    PUSH("push");

    object CachePolicySerializer : StringRepresentationSerializer<CachePolicy>("CachePolicy")
}