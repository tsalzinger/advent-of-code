package me.salzinger.common.extensions

fun String.toIntList(vararg delimiters: String = arrayOf(",")): List<Int> {
    return split(delimiters = delimiters).map { it.toInt() }
}

fun String.toIntList(regex: Regex): List<Int> {
    return split(regex = regex).map { it.toInt() }
}

fun String.toLongList(vararg delimiters: String = arrayOf(",")): List<Long> {
    return split(delimiters = delimiters).map { it.toLong() }
}

fun String.toLongList(regex: Regex): List<Long> {
    return split(regex = regex).map { it.toLong() }
}
