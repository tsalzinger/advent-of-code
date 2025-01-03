package me.salzinger.common.extensions

import me.salzinger.common.Grid2D
import me.salzinger.common.NeighborProvider

fun <T> List<T>.countOccurrences(value: T) = this.count { it == value }
fun <T> List<T>.toPairs(): List<Pair<T, T>> = chunked(2).map { Pair(it[0], it[1]) }

enum class ChunkEvaluation {
    APPEND_TO_CHUNK,
    APPEND_TO_NEW_CHUNK,
    END_CHUNK_AND_DISCARD
}

typealias ChunkCondition<T> = (T) -> ChunkEvaluation

fun <T> List<T>.chunkedBy(chunkCondition: (T) -> ChunkEvaluation): List<List<T>> {
    return if (isEmpty()) {
        emptyList()
    } else {
        foldIndexed(mutableListOf(mutableListOf())) { index: Int, acc: MutableList<MutableList<T>>, item: T ->
            when (chunkCondition(item)) {
                ChunkEvaluation.APPEND_TO_CHUNK -> {
                    if (acc.isEmpty()) {
                        acc.add(mutableListOf(item))
                    } else {
                        acc.last().add(item)
                    }
                }

                ChunkEvaluation.APPEND_TO_NEW_CHUNK -> {
                    acc.add(mutableListOf(item))
                }

                ChunkEvaluation.END_CHUNK_AND_DISCARD -> {
                    if (index != lastIndex) {
                        acc.add(mutableListOf())
                    }
                }
            }

            acc
        }.map { it.toList() }
    }
}


object ChunkConditions {
    val ON_EMPTY_STRING: ChunkCondition<String> = {
        when {
            it.isEmpty() -> ChunkEvaluation.END_CHUNK_AND_DISCARD
            else -> ChunkEvaluation.APPEND_TO_CHUNK
        }
    }
    val ON_EMPTY_COLLECTION: ChunkCondition<Collection<Any>> = {
        when {
            it.isEmpty() -> ChunkEvaluation.END_CHUNK_AND_DISCARD
            else -> ChunkEvaluation.APPEND_TO_CHUNK
        }
    }
}


/**
 * @param neighborProvider The neighbor provider to use, if `null` the default of [Grid2D] will be used
 */
fun <T> List<List<T>>.toGrid2D(neighborProvider: NeighborProvider? = null): Grid2D<T> {
    return if (neighborProvider != null) {
        Grid2D(this, neighborProvider)
    } else {
        Grid2D(this)
    }
}

fun <T> List<T>.permutate(): Sequence<Pair<T, T>> {
    return asSequence().flatMapIndexed { index, element ->
        drop(index + 1).map {
            element to it
        }
    }
}