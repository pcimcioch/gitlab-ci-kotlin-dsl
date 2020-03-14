package pcimcioch.gitlabci.dsl

enum class DefaultType {
    IMAGE,
    SERVICES,
    BEFORE_SCRIPT,
    AFTER_SCRIPT,
    TAGS,
    CACHE,
    ARTIFACTS,
    RETRY,
    TIMEOUT,
    INTERRUPTIBLE;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}