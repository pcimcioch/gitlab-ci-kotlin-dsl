package pcimcioch.gitlabci.dsl

// TODO move to DslBase?
internal fun <T> addAndReturn(list: MutableList<T>, element: T): T {
    list.add(element)
    return element
}

// TODO move to DslBase?
internal fun isEmpty(tested: String?) = (tested == null || "" == tested)