package me.salzinger.puzzles.puzzle25

import me.salzinger.common.Grid2D
import me.salzinger.common.extensions.ChunkConditions
import me.salzinger.common.extensions.chunkedBy
import me.salzinger.common.extensions.toGrid2D

object `Code Chronicle` {
    fun Sequence<String>.countUniqueFittingLockKeyPairs(): Int {
        val locks = mutableListOf<Grid2D<Boolean>>()
        val keys = mutableListOf<Grid2D<Boolean>>()

        map {
            it.map { it == '#' }
        }
            .toList()
            .chunkedBy(ChunkConditions.ON_EMPTY_COLLECTION)
            .forEach {
                if (it.first().first()) {
                    locks += it.drop(1).dropLast(1).toGrid2D()
                } else {
                    keys += it.drop(1).dropLast(1).toGrid2D()
                }
            }

        return locks.flatMap { lock ->
            keys.filterNot { key ->
                lock.any { it.value && key[it.coordinate].value }
            }.map { lock to it }
        }.count()
    }
}