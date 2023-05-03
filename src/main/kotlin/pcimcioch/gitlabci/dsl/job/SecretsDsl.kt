package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.serializer.TwoTypeSerializer
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@Serializable(with = SecretsDsl.SecretsDslSerializer::class)
class SecretsDsl : DslBase() {
    var secrets: MutableMap<String, SecretDsl> = mutableMapOf()

    fun add(name: String, value: SecretDsl) = secrets.put(name, value)
    fun add(name: String, vault: String) = secrets.put(name, SecretDsl().apply { this.vault(vault) })
    fun add(name: String, block: SecretVaultDsl.() -> Unit) = secrets.put(name, SecretDsl().apply { vault(block) })
    fun addSecret(name: String, block: SecretDsl.() -> Unit) = secrets.put(name, SecretDsl().apply(block))

    infix fun String.to(value: SecretDsl) = add(this, value)
    infix fun String.to(vault: String) = add(this, vault)
    infix fun String.to(block: SecretVaultDsl.() -> Unit) = add(this, block)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SecretsDsl

        if (secrets != other.secrets) return false

        return true
    }

    override fun hashCode(): Int {
        return secrets.hashCode()
    }


    object SecretsDslSerializer : ValueSerializer<SecretsDsl, Map<String, SecretDsl>>(
        MapSerializer(String.serializer(), SecretDsl.serializer()),
        SecretsDsl::secrets
    )

    companion object {
        init {
            addSerializer(SecretsDsl::class, serializer())
        }
    }
}

fun createSecrets(block: SecretsDsl.() -> Unit = {}) = SecretsDsl().apply(block)
fun createSecrets(elements: Map<String, String>, block: SecretsDsl.() -> Unit = {}) =
    SecretsDsl().apply { elements.forEach { add(it.key, it.value) } }.apply(block)

@JvmName("createSecretsFromDsl")
fun createSecrets(elements: Map<String, SecretDsl>, block: SecretsDsl.() -> Unit = {}) =
    SecretsDsl().apply { elements.forEach { add(it.key, it.value) } }.apply(block)

@Serializable
class SecretDsl : DslBase() {
    @Transient
    private var vaultString: String? = null

    @Transient
    private var vaultDsl: SecretVaultDsl? = null

    var file: Boolean? = null
    var token: String? = null

    @Serializable(with = VaultSerializer::class)
    var vault: Any? = null
        get() = vaultString ?: vaultDsl
        private set

    fun vault(vault: String) {
        vaultDsl = null
        vaultString = vault
    }

    fun vault(block: SecretVaultDsl.() -> Unit) {
        vaultString = null
        ensureVaultDsl().apply(block)
    }

    fun vault(vault: SecretVaultDsl) {
        vaultString = null
        vaultDsl = vault
    }

    private fun ensureVaultDsl() = vaultDsl ?: SecretVaultDsl().also { vaultDsl = it }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SecretDsl

        if (vault != other.vault) return false
        if (file != other.file) return false
        if (token != other.token) return false

        return true
    }

    override fun hashCode(): Int {
        var result = vault?.hashCode() ?: 0
        result = 31 * result + (file?.hashCode() ?: 0)
        result = 31 * result + (token?.hashCode() ?: 0)
        return result
    }


    object VaultSerializer : TwoTypeSerializer<Any>(
        PrimitiveSerialDescriptor("Vault", PrimitiveKind.STRING),
        String::class, String.serializer(),
        SecretVaultDsl::class, SecretVaultDsl.serializer()
    )

    companion object {
        init {
            addSerializer(SecretDsl::class, serializer())
        }
    }
}

fun createSecret(vault: String) = SecretDsl().apply { vault(vault) }
fun createSecret(block: SecretVaultDsl.() -> Unit = {}) = SecretDsl().apply { vault(block) }
fun createSecret(vault: SecretVaultDsl) = SecretDsl().apply { vault(vault) }

@Serializable
class SecretVaultDsl : DslBase() {
    var engine: SecretVaultEngineDsl? = null
    var path: String? = null
    var field: String? = null

    fun engine(block: SecretVaultEngineDsl.() -> Unit = {}) = ensureEngine().apply(block)

    private fun ensureEngine() = engine ?: SecretVaultEngineDsl().also { engine = it }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SecretVaultDsl

        if (engine != other.engine) return false
        if (path != other.path) return false
        if (field != other.field) return false

        return true
    }

    override fun hashCode(): Int {
        var result = engine?.hashCode() ?: 0
        result = 31 * result + (path?.hashCode() ?: 0)
        result = 31 * result + (field?.hashCode() ?: 0)
        return result
    }


    companion object {
        init {
            addSerializer(SecretVaultDsl::class, serializer())
        }
    }
}

fun createSecretVault(block: SecretVaultDsl.() -> Unit = {}) = SecretVaultDsl().apply(block)

@Serializable
class SecretVaultEngineDsl : DslBase() {
    var name: String? = null
    var path: String? = null


    companion object {
        init {
            addSerializer(SecretVaultEngineDsl::class, serializer())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SecretVaultEngineDsl

        if (name != other.name) return false
        if (path != other.path) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (path?.hashCode() ?: 0)
        return result
    }
}

fun createSecretVaultEngine(block: SecretVaultEngineDsl.() -> Unit = {}) = SecretVaultEngineDsl().apply(block)