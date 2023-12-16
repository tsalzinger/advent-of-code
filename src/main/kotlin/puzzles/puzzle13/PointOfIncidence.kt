package me.salzinger.puzzles.puzzle13

import me.salzinger.common.extensions.ChunkEvaluation
import me.salzinger.common.extensions.chunkedBy


typealias IndexedLines = Pair<Int, String>

object PointOfIncidence {

    fun String.countDifferences(other: String): Int {
        return this.zip(other)
            .count { (first, second) ->
                first != second
            }
    }

    fun List<String>.pairsOfLinesWithOneDifference(): List<Pair<IndexedLines, IndexedLines>> {
        return foldIndexed(emptyList()) { index, pairs, line ->
            val firstPair = index to line
            pairs + drop(index + 1)
                .mapIndexedNotNull { otherIndex, otherLine ->
                    if (line.countDifferences(otherLine) == 1) {
                        (index + otherIndex + 1) to otherLine
                    } else {
                        null
                    }
                }
                .map {
                    firstPair to it
                }
        }
    }

    fun List<String>.getMirrorIndexOrNull(): Int? {
        return getMirrorIndexes()
            .singleOrNull()
    }

    fun List<String>.getMirrorIndexes(): List<Int> {
        return windowed(2)
            .mapIndexed { index, (first, second) ->
                (index + 1) to (first to second)
            }
            .filter { (_, pair) ->
                pair.first == pair.second
            }
            .filter { (index, _) ->
                if (index <= count() / 2) {
                    slice(index..<2 * index) == slice(0..<index).reversed()
                } else {
                    val sliceLength = count() - index
                    slice((index - sliceLength)..<index) == slice(index..<count()).reversed()
                }
            }
            .map {
                it.first
            }
    }

    fun List<String>.getMirrorIndex(): Int {
        return getMirrorIndexOrNull() ?: throw RuntimeException("Failed to find mirror index in $this")
    }

    fun List<String>.transpose(): List<String> {
        val original = this

        return List(first().count()) { index ->
            original.joinToString("") { it[index].toString() }
        }
    }

    fun Sequence<String>.separatePatterns(): List<List<String>> {
        return toList()
            .chunkedBy {
                when {
                    it.isEmpty() -> ChunkEvaluation.END_CHUNK_AND_DISCARD
                    else -> ChunkEvaluation.APPEND_TO_CHUNK
                }
            }
    }

    fun Sequence<String>.getSummaryOfNotes(): Int {
        return separatePatterns()
            .sumOf {
                it.getMirrorIndexOrNull()?.times(100) ?: it.transpose().getMirrorIndex()
            }
    }

    fun Sequence<String>.getSummaryOfNotesAfterCorrectingTheSmudge(): Int {
        return separatePatterns()
            .map { pattern ->
                val rowIndexWithoutCorrection = pattern.getMirrorIndexOrNull()
                val rowWise = pattern
                    .pairsOfLinesWithOneDifference()
                    .filter { (it.second.first - it.first.first) % 2 == 1 }
                    .map { (line1, line2) ->
                        List(pattern.count()) { index ->
                            if (index == line2.first) {
                                line1.second
                            } else {
                                pattern[index]
                            }
                        }
                    }
                    .flatMap {
                        it.getMirrorIndexes()
                    }
                    .toSet()
                    .filter {
                        it != rowIndexWithoutCorrection
                    }

                val transposed = pattern.transpose()
                val columnIndexWithoutCorrection = transposed.getMirrorIndexOrNull()
                val columnWise = transposed
                    .pairsOfLinesWithOneDifference()
                    .filter { (it.second.first - it.first.first) % 2 == 1 }
                    .map { (line1, line2) ->
                        List(transposed.count()) { index ->
                            if (index == line2.first) {
                                line1.second
                            } else {
                                transposed[index]
                            }
                        }
                    }
                    .flatMap {
                        it.getMirrorIndexes()
                    }
                    .toSet()
                    .filter {
                        it != columnIndexWithoutCorrection
                    }

                require(rowWise.count() + columnWise.count() == 1)

                rowWise.singleOrNull()?.times(100) ?: columnWise.single()
            }
            .sum()
    }
}
