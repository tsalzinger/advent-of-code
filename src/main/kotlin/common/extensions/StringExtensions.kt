package me.salzinger.common.extensions

fun String.toIntList(vararg delimiters: String = arrayOf(",")): List<Int> {
    return trim().split(delimiters = delimiters).map { it.trim().toInt() }
}

fun String.toIntList(regex: Regex): List<Int> {
    return trim().split(regex = regex).map { it.trim().toInt() }
}

fun String.toLongList(vararg delimiters: String = arrayOf(",")): List<Long> {
    return trim().split(delimiters = delimiters).map { it.trim().toLong() }
}

fun String.toLongList(regex: Regex): List<Long> {
    return trim().split(regex = regex).map { it.trim().toLong() }
}
