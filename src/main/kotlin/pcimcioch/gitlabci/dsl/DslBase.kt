package pcimcioch.gitlabci.dsl

interface DslBase {
    fun validate(errors: MutableList<String>) {}

    companion object {
        internal fun addError(errors: MutableList<String>, condition: Boolean, message: String) {
            if (condition) {
                errors.add(message)
            }
        }

        internal fun addErrors(errors: MutableList<String>, objs: Collection<DslBase>, messagePrefix: String) {
            objs.forEach { addErrors(errors, it, messagePrefix) }
        }

        internal fun addErrors(errors: MutableList<String>, obj: DslBase?, messagePrefix: String) {
            if (obj == null) {
                return
            }
            val objErrors = mutableListOf<String>()
            obj.validate(objErrors)
            objErrors.forEach { errors.add("$messagePrefix$it") }
        }
    }
}