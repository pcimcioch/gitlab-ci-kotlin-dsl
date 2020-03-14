package pcimcioch.gitlabci.dsl.job

import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker

@GitlabCiDslMarker
class VariablesDsl : DslBase {
    private val variables: MutableMap<String, Any> = mutableMapOf()

    fun add(name: String, value: Any) = variables.put(name, value)
    fun <T : Enum<T>> add(name: T, value: Any) = variables.put(name.toString(), value)

    infix fun String.to(value: Any) = add(this, value)
    infix fun <T : Enum<T>> T.to(value: Any) = add(this, value)

    fun gitStrategy(strategy: GitStrategyType) = add(RunnerSettingsVariables.GIT_STRATEGY, strategy)
    fun gitSubmoduleStrategy(strategy: GItSubmoduleStrategyType) = add(RunnerSettingsVariables.GIT_SUBMODULE_STRATEGY, strategy)
    fun gitCheckout(checkout: Boolean) = add(RunnerSettingsVariables.GIT_CHECKOUT, checkout)
    fun gitClean(flags: String) = add(RunnerSettingsVariables.GIT_CLEAN_FLAGS, flags)
    fun disableGitClean() = gitClean("none")
    fun getResourcesAttempts(attempts: Int) = add(RunnerSettingsVariables.GET_SOURCES_ATTEMPTS, attempts)
    fun artifactDownloadAttempts(attempts: Int) = add(RunnerSettingsVariables.ARTIFACT_DOWNLOAD_ATTEMPTS, attempts)
    fun restoreCacheAttempts(attempts: Int) = add(RunnerSettingsVariables.RESTORE_CACHE_ATTEMPTS, attempts)
    fun gitDepth(attempts: Int) = add(RunnerSettingsVariables.GIT_DEPTH, attempts)
}
// TODO remember that during yaml conversion, we have to call toString() for all values, except Int

enum class RunnerSettingsVariables {
    GIT_STRATEGY,
    GIT_SUBMODULE_STRATEGY,
    GIT_CHECKOUT,
    GIT_CLEAN_FLAGS,
    GET_SOURCES_ATTEMPTS,
    ARTIFACT_DOWNLOAD_ATTEMPTS,
    RESTORE_CACHE_ATTEMPTS,
    GIT_DEPTH
}

enum class GitStrategyType {
    CLONE,
    FETCH,
    NONE;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}

enum class GItSubmoduleStrategyType {
    NONE,
    NORMAL,
    RECURSIVE;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}