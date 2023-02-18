package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.StringRepresentation
import pcimcioch.gitlabci.dsl.serializer.StringRepresentationSerializer
import pcimcioch.gitlabci.dsl.serializer.TwoTypeSerializer

@Serializable
class CacheDsl : DslBase() {
    var paths: MutableSet<String>? = null
    var untracked: Boolean? = null
    var policy: CachePolicy? = null

    @SerialName("when")
    var whenCache: WhenCacheType? = null

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

    fun key(key: CacheKeyDsl) {
        keyString = null
        keyDsl = key
    }

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, "[cache]", keyDsl)
    }

    private fun ensureKeyDsl() = keyDsl ?: CacheKeyDsl().also { keyDsl = it }
    private fun ensurePaths() = paths ?: mutableSetOf<String>().also { paths = it }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CacheDsl

        if (paths != other.paths) return false
        if (untracked != other.untracked) return false
        if (policy != other.policy) return false
        if (whenCache != other.whenCache) return false
        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        var result = paths?.hashCode() ?: 0
        result = 31 * result + (untracked?.hashCode() ?: 0)
        result = 31 * result + (policy?.hashCode() ?: 0)
        result = 31 * result + (whenCache?.hashCode() ?: 0)
        result = 31 * result + (key?.hashCode() ?: 0)
        return result
    }


    object KeySerializer : TwoTypeSerializer<Any>(
        PrimitiveSerialDescriptor("Key", PrimitiveKind.STRING),
        String::class, String.serializer(),
        CacheKeyDsl::class, CacheKeyDsl.serializer()
    )

    companion object {
        init {
            addSerializer(CacheDsl::class, serializer())
        }
    }
}

fun createCache(block: CacheDsl.() -> Unit = {}) = CacheDsl().apply(block)
fun createCache(vararg elements: String, block: CacheDsl.() -> Unit = {}) = createCache(elements.toList(), block)
fun createCache(elements: Iterable<String>, block: CacheDsl.() -> Unit = {}) =
    CacheDsl().apply { paths(elements) }.apply(block)

@Serializable
class CacheKeyDsl : DslBase() {
    var prefix: String? = null
    var files: MutableSet<String>? = null

    fun files(vararg elements: String) = files(elements.toList())
    fun files(elements: Iterable<String>) = ensureFiles().addAll(elements)

    override fun validate(errors: MutableList<String>) {
        addError(errors, files?.isNotEmpty() != true, "[key] files list can't be empty")
        addError(
            errors,
            "." == prefix || "%2E" == prefix || "%2e" == prefix,
            "[key] prefix value '$prefix' can't be '.' nor '%2E'"
        )
        addError(
            errors,
            prefix?.contains("/") == true || prefix?.contains("%2F") == true || prefix?.contains("%2f") == true,
            "[key] prefix value '$prefix' can't contain '/' nor '%2F'"
        )
    }

    private fun ensureFiles() = files ?: mutableSetOf<String>().also { files = it }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CacheKeyDsl

        if (prefix != other.prefix) return false
        if (files != other.files) return false

        return true
    }

    override fun hashCode(): Int {
        var result = prefix?.hashCode() ?: 0
        result = 31 * result + (files?.hashCode() ?: 0)
        return result
    }


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
    PULL("pull"),
    PULL_PUSH("pull-push"),
    PUSH("push");

    object CachePolicySerializer : StringRepresentationSerializer<CachePolicy>("CachePolicy")
}

@Serializable(with = WhenCacheType.WhenCacheTypeSerializer::class)
enum class WhenCacheType(
    override val stringRepresentation: String
) : StringRepresentation {
    ON_SUCCESS("on_success"),
    ON_FAILURE("on_failure"),
    ALWAYS("always");

    object WhenCacheTypeSerializer : StringRepresentationSerializer<WhenCacheType>("WhenCacheType")
}