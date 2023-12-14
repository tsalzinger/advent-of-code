package me.salzinger.puzzles.puzzle13

import me.salzinger.common.extensions.ChunkEvaluation
import me.salzinger.common.extensions.chunkedBy

object PointOfIncidence {

    fun List<String>.getMirrorIndexOrNull(): Int? {
        return windowed(2)
            .mapIndexed { index, (first, second) ->
                (index + 1) to (first to second)
            }
            .filter { (_, pair) ->
                pair.first == pair.second
            }
            .singleOrNull { (index, _) ->
                if (index <= count() / 2) {
                    slice(index..<2 * index) == slice(0..<index).reversed()
                } else {
                    val sliceLength = count() - index
                    slice((index - sliceLength)..<index) == slice(index..<count()).reversed()
                }
            }
            ?.first
    }

    fun List<String>.getMirrorIndex(): Int {
        return getMirrorIndexOrNull() ?: throw RuntimeException("Failed to find mirror index in $this")
    }

    fun List<String>.getMirrorSummaryOrNull(multiplier: Int): Int? {
        return getMirrorIndexOrNull()?.times(multiplier)
    }

    fun List<String>.transpose(): List<String> {
        val original = this

        return List(first().count()) { index ->
            original.joinToString("") { it[index].toString() }
        }
    }

    fun Sequence<String>.getSummaryOfNotes(): Int {
        return toList()
            .chunkedBy {
                when {
                    it.isEmpty() -> ChunkEvaluation.END_CHUNK_AND_DISCARD
                    else -> ChunkEvaluation.APPEND_TO_CHUNK
                }
            }
            .sumOf {
                it.getMirrorIndexOrNull()?.times(100) ?: it.transpose().getMirrorIndex()
            }
    }
}
