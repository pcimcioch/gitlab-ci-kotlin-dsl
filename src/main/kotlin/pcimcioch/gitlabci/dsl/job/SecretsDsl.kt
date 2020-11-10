package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.PrimitiveDescriptor
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.serializer.TwoTypeSerializer
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@Serializable(with = SecretsDsl.SecretsDslSerializer::class)
class SecretsDsl : DslBase() {
    var secrets: MutableMap<String, SecretDsl> = mutableMapOf()

    fun add(name: String, value: SecretDsl) = secrets.put(name, value)
    fun add(name: String, vault: String) = secrets.put(name, SecretDsl().apply { this.vault(vault) })
    fun add(name: String, block: SecretVaultDsl.() -> Unit) = secrets.put(name, SecretDsl().apply { vault(block) })

    infix fun String.to(value: SecretDsl) = add(this, value)
    infix fun String.to(vault: String) = add(this, vault)
    infix fun String.to(block: SecretVaultDsl.() -> Unit) = add(this, block)

    object SecretsDslSerializer : ValueSerializer<SecretsDsl, Map<String, SecretDsl>>(MapSerializer(String.serializer(), SecretDsl.serializer()), SecretsDsl::secrets)
    companion object {
        init {
            addSerializer(SecretsDsl::class, serializer())
        }
    }
}

fun createSecrets(block: SecretsDsl.() -> Unit = {}) = SecretsDsl().apply(block)
fun createSecrets(elements: Map<String, String>, block: SecretsDsl.() -> Unit = {}) = SecretsDsl().apply { elements.forEach { add(it.key, it.value) } }.apply(block)
@JvmName("createSecretsFromDsl")
fun createSecrets(elements: Map<String, SecretDsl>, block: SecretsDsl.() -> Unit = {}) = SecretsDsl().apply { elements.forEach { add(it.key, it.value) } }.apply(block)

@Serializable
class SecretDsl : DslBase() {
    @Transient
    private var vaultString: String? = null

    @Transient
    private var vaultDsl: SecretVaultDsl? = null

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

    object VaultSerializer : TwoTypeSerializer<Any>(
            PrimitiveDescriptor("Vault", PrimitiveKind.STRING),
            String::class, String.serializer(),
            SecretVaultDsl::class, SecretVaultDsl.serializer())

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

    companion object {
        init {
            addSerializer(SecretVaultDsl::class, serializer())
        }
    }
}

fun createSecretVault(block: SecretVaultDsl.() -> Unit = {}) = SecretVaultDsl().apply(block)

@Serializable
class SecretVaultEngineDsl: DslBase() {
    var name: String? = null
    var path: String? = null

    companion object {
        init {
            addSerializer(SecretVaultEngineDsl::class, serializer())
        }
    }
}

fun createSecretVaultEngine(block: SecretVaultEngineDsl.() -> Unit = {}) = SecretVaultEngineDsl().apply(block)