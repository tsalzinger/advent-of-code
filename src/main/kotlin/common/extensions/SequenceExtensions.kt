package me.salzinger.common.extensions

import me.salzinger.common.Grid2D

fun <T> Sequence<T>.permutate(): Sequence<Pair<T, T>> {
    return toList().permutate()
}

fun Sequence<String>.toGrid2D(): Grid2D<Char> {
    return map { row -> row.map { it } }.toList().toGrid2D()
}