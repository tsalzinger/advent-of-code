package me.salzinger.common.extensions

import me.salzinger.common.Grid2D
import me.salzinger.common.NeighborProvider

fun <T> Sequence<T>.permutate(): Sequence<Pair<T, T>> {
    return toList().permutate()
}

fun Sequence<String>.toGrid2D(neighborProvider: NeighborProvider? = null): Grid2D<Char> {
    return map { row -> row.map { it } }.toList()
        .run {
            if (neighborProvider != null) {
                toGrid2D(neighborProvider = neighborProvider)
            } else {
                toGrid2D()
            }
        }
}