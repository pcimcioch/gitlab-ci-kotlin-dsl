package pcimcioch.gitlabci.dsl

enum class WhenType {
    ON_SUCCESS,
    ON_FAILURE,
    ALWAYS,
    MANUAL,
    DELAYED;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}