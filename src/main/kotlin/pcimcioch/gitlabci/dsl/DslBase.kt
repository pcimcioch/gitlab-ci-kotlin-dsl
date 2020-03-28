package pcimcioch.gitlabci.dsl

// TODO maybe serializable could be on this level?
interface DslBase {

    fun validate(errors: MutableList<String>) {}

    companion object {
        internal fun addError(errors: MutableList<String>, condition: Boolean?, message: String) {
            if (condition == true) {
                errors.add(message)
            }
        }

        internal fun addErrors(errors: MutableList<String>, messagePrefix: String, objs: Collection<DslBase>) {
            objs.forEach { addErrors(errors, messagePrefix, it) }
        }

        internal fun addErrors(errors: MutableList<String>, messagePrefix: String, vararg obj: DslBase?) {
            val objErrors = mutableListOf<String>()
            obj.forEach { it?.validate(objErrors) }
            objErrors.forEach { errors.add("$messagePrefix$it") }
        }

        internal fun <T> addAndReturn(list: MutableList<T>, element: T): T {
            list.add(element)
            return element
        }

        internal fun isEmpty(tested: String?) = (tested == null || "" == tested)
    }
}