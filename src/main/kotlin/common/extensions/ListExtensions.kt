package me.salzinger.common.extensions

fun <T> List<T>.countOccurrences(value: T) = this.count { it == value }
fun <T> List<T>.toPairs(): List<Pair<T, T>> = chunked(2).map { Pair(it[0], it[1]) }
