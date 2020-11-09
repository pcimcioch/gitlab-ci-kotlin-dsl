package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.serializer.MultiTypeSerializer
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@Serializable(with = SecretsDsl.SecretsDslSerializer::class)
class SecretsDsl : DslBase() {
    var secrets: MutableMap<String, SecretDsl> = mutableMapOf()

    fun add(name: String, value: SecretDsl) = secrets.put(name, value)
    fun add(name: String, vault: String) = secrets.put(name, SecretDsl().apply { this.vault(vault) })
    fun add(name: String, block: VaultDsl.() -> Unit) = secrets.put(name, SecretDsl().apply { vault(block) })

    infix fun String.to(value: SecretDsl) = add(this, value)
    infix fun String.to(vault: String) = add(this, vault)
    infix fun String.to(block: VaultDsl.() -> Unit) = add(this, block)

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
    private var vaultDsl: VaultDsl? = null

    @Serializable(with = VaultSerializer::class)
    var vault: Any? = null
        get() = vaultString ?: vaultDsl
        private set

    fun vault(vault: String) {
        vaultDsl = null
        vaultString = vault
    }

    fun vault(block: VaultDsl.() -> Unit) {
        vaultString = null
        ensureVaultDsl().apply(block)
    }

    fun vault(vault: VaultDsl) {
        vaultString = null
        vaultDsl = vault
    }

    private fun ensureVaultDsl() = vaultDsl ?: VaultDsl().also { vaultDsl = it }

    object VaultSerializer : MultiTypeSerializer<Any>(
            PrimitiveSerialDescriptor("Vault", PrimitiveKind.STRING),
            mapOf(
                    String::class to String.serializer(),
                    VaultDsl::class to VaultDsl.serializer()))

    companion object {
        init {
            addSerializer(SecretDsl::class, serializer())
        }
    }
}

fun createSecret(vault: String) = SecretDsl().apply { vault(vault) }
fun createSecret(block: VaultDsl.() -> Unit = {}) = SecretDsl().apply { vault(block) }
fun createSecret(vault: VaultDsl) = SecretDsl().apply { vault(vault) }

@Serializable
class VaultDsl : DslBase() {
    var engine: VaultEngineDsl? = null
    var path: String? = null
    var field: String? = null

    fun engine(block: VaultEngineDsl.() -> Unit = {}) = ensureEngine().apply(block)

    private fun ensureEngine() = engine ?: VaultEngineDsl().also { engine = it }

    companion object {
        init {
            addSerializer(VaultDsl::class, serializer())
        }
    }
}

fun createVault(block: VaultDsl.() -> Unit = {}) = VaultDsl().apply(block)

@Serializable
class VaultEngineDsl: DslBase() {
    var name: String? = null
    var path: String? = null

    companion object {
        init {
            addSerializer(VaultEngineDsl::class, serializer())
        }
    }
}

fun createVaultEngine(block: VaultEngineDsl.() -> Unit = {}) = VaultEngineDsl().apply(block)