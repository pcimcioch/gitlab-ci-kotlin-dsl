package pcimcioch.gitlabci.dsl

internal fun <T> addAndReturn(list: MutableList<T>, element: T): T {
    list.add(element)
    return element
}

internal fun isEmpty(tested: String?) = (tested == null || "" == tested)