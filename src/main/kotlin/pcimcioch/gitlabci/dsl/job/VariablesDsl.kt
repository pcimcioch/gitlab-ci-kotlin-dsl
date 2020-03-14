package pcimcioch.gitlabci.dsl.job

import pcimcioch.gitlabci.dsl.GitlabCiDslMarker

@GitlabCiDslMarker
class VariablesDsl {
    private val variables: MutableMap<String, Any> = mutableMapOf()

    fun add(name: String, value: String) = variables.put(name, value)
    fun add(name: String, value: Int) = variables.put(name, value)
    fun add(name: String, value: Any) = variables.put(name, value.toString())

    fun gitStrategy(strategy: GitStrategyType) = add(RunnerSettingsVariables.GIT_STRATEGY.toString(), strategy.toString().toLowerCase())
    fun gitSubmoduleStrategy(strategy: GItSubmoduleStrategyType) = add(RunnerSettingsVariables.GIT_SUBMODULE_STRATEGY.toString(), strategy.toString().toLowerCase())
    fun gitCheckout(checkout: Boolean) = add(RunnerSettingsVariables.GIT_CHECKOUT.toString(), checkout)
    fun gitClean(flags: String) = add(RunnerSettingsVariables.GIT_CLEAN_FLAGS.toString(), flags)
    fun disableGitClean() = gitClean("none")
    fun getResourcesAttempts(attempts: Int) = add(RunnerSettingsVariables.GET_SOURCES_ATTEMPTS.toString(), attempts)
    fun artifactDownloadAttempts(attempts: Int) = add(RunnerSettingsVariables.ARTIFACT_DOWNLOAD_ATTEMPTS.toString(), attempts)
    fun restoreCacheAttempts(attempts: Int) = add(RunnerSettingsVariables.RESTORE_CACHE_ATTEMPTS.toString(), attempts)
    fun gitDepth(attempts: Int) = add(RunnerSettingsVariables.GIT_DEPTH.toString(), attempts)

    infix fun String.to(value: String) = add(this, value)
    infix fun String.to(value: Int) = add(this, value)
    infix fun String.to(value: Any) = add(this, value)
}

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
    NONE
}

enum class GItSubmoduleStrategyType {
    NONE,
    NORMAL,
    RECURSIVE
}