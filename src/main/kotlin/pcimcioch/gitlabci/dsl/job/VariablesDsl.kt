package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addError
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.StringRepresentation
import pcimcioch.gitlabci.dsl.serializer.StringRepresentationSerializer
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@GitlabCiDslMarker
@Serializable(with = VariablesDsl.VariablesDslSerializer::class)
class VariablesDsl : DslBase {
    var variables: MutableMap<String, String> = mutableMapOf()

    fun add(name: String, value: Any) = variables.put(name, value.toString())
    fun <T : Enum<T>> add(name: T, value: Any) = variables.put(name.toString(), value.toString())

    infix fun String.to(value: Any) = add(this, value)
    infix fun <T : Enum<T>> T.to(value: Any) = add(this, value)

    fun gitStrategy(strategy: GitStrategyType) = add(RunnerSettingsVariables.GIT_STRATEGY, strategy.stringRepresentation)
    fun gitSubmoduleStrategy(strategy: GitSubmoduleStrategyType) = add(RunnerSettingsVariables.GIT_SUBMODULE_STRATEGY, strategy.stringRepresentation)
    fun gitCheckout(checkout: Boolean) = add(RunnerSettingsVariables.GIT_CHECKOUT, checkout)
    fun gitClean(flags: String) = add(RunnerSettingsVariables.GIT_CLEAN_FLAGS, flags)
    fun disableGitClean() = gitClean("none")
    fun getResourcesAttempts(attempts: Int) = add(RunnerSettingsVariables.GET_SOURCES_ATTEMPTS, attempts)
    fun artifactDownloadAttempts(attempts: Int) = add(RunnerSettingsVariables.ARTIFACT_DOWNLOAD_ATTEMPTS, attempts)
    fun restoreCacheAttempts(attempts: Int) = add(RunnerSettingsVariables.RESTORE_CACHE_ATTEMPTS, attempts)
    fun gitDepth(attempts: Int) = add(RunnerSettingsVariables.GIT_DEPTH, attempts)
    fun gitClonePath(path: String) = add(RunnerSettingsVariables.GIT_CLONE_PATH, path)

    override fun validate(errors: MutableList<String>) {
        addError(errors, variables.isEmpty(), "[variables] variables map cannot be empty")
    }

    object VariablesDslSerializer : ValueSerializer<VariablesDsl, Map<String, String>>(MapSerializer(String.serializer(), String.serializer()), VariablesDsl::variables)
}

fun variables(block: VariablesDsl.() -> Unit) = VariablesDsl().apply(block)
fun variables(elements: Map<String, String>) = VariablesDsl().apply { elements.forEach { add(it.key, it.value) } }
fun variables(elements: Map<String, String>, block: VariablesDsl.() -> Unit) = VariablesDsl().apply { elements.forEach { add(it.key, it.value) } }.apply(block)

@JvmName("variablesEnum")
fun <T : Enum<T>> variables(elements: Map<T, String>) = VariablesDsl().apply { elements.forEach { add(it.key, it.value) } }

@JvmName("variablesEnum")
fun <T : Enum<T>> variables(elements: Map<T, String>, block: VariablesDsl.() -> Unit) = VariablesDsl().apply { elements.forEach { add(it.key, it.value) } }.apply(block)

enum class RunnerSettingsVariables {
    GIT_STRATEGY,
    GIT_SUBMODULE_STRATEGY,
    GIT_CHECKOUT,
    GIT_CLEAN_FLAGS,
    GET_SOURCES_ATTEMPTS,
    ARTIFACT_DOWNLOAD_ATTEMPTS,
    RESTORE_CACHE_ATTEMPTS,
    GIT_DEPTH,
    GIT_CLONE_PATH
}

@Serializable(with = GitStrategyType.GitStrategyTypeSerializer::class)
enum class GitStrategyType(override val stringRepresentation: String) : StringRepresentation {
    CLONE("clone"),
    FETCH("fetch"),
    NONE("none");

    object GitStrategyTypeSerializer : StringRepresentationSerializer<GitStrategyType>("GitStrategyType")
}

@Serializable(with = GitSubmoduleStrategyType.GitSubmoduleStrategyTypeSerializer::class)
enum class GitSubmoduleStrategyType(override val stringRepresentation: String) : StringRepresentation {
    NONE("none"),
    NORMAL("normal"),
    RECURSIVE("recursive");

    object GitSubmoduleStrategyTypeSerializer : StringRepresentationSerializer<GitSubmoduleStrategyType>("GitSubmoduleStrategyType")
}