package me.salzinger.common.extensions

fun String.toIntList(vararg delimiters: String = arrayOf(",")): List<Int> {
    return split(delimiters = delimiters).map { it.toInt() }
}

fun String.toIntList(regex: Regex): List<Int> {
    return split(regex = regex).map { it.toInt() }
}
