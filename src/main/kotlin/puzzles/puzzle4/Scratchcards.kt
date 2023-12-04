package me.salzinger.puzzles.puzzle4

import me.salzinger.common.extensions.toIntList
import me.salzinger.common.math.pow

object Scratchcards {
    data class Scratchcard(
        val winningNumbers: Set<Int>,
        val playerNumbers: Set<Int>,
    ) {
        val countOfMatchingNumbers: Int by lazy {
            playerNumbers.count {
                it in winningNumbers
            }
        }

        val points: Long by lazy {
            if (countOfMatchingNumbers == 0) {
                0
            } else {
                2.pow(countOfMatchingNumbers - 1)
            }
        }
    }

    fun String.toScratchcard(): Scratchcard {
        return split(":")
            .last()
            .split("|")
            .let { (first, second) ->
                Scratchcard(
                    winningNumbers = first.trim().toIntList(Regex("\\s+")).toSet(),
                    playerNumbers = second.trim().toIntList(Regex("\\s+")).toSet(),
                )
            }
    }

    fun Sequence<String>.toScratchcards(): Sequence<Scratchcard> {
        return map {
            it.toScratchcard()
        }
    }

    fun Sequence<Scratchcard>.sumOfPoints(): Long {
        return sumOf {
            it.points
        }
    }

    fun Sequence<Scratchcard>.sumOfScratchcards(): Long {
        val cardCounts = mutableMapOf<Int, Long>()

        forEachIndexed { index, scratchcard ->
            cardCounts.compute(index) { _, count ->
                (count ?: 0) + 1
            }

            for (i in 0 until scratchcard.countOfMatchingNumbers) {
                cardCounts.compute(index + i + 1) { _, count ->
                    (count ?: 0) + cardCounts.getValue(index)
                }
            }
        }

        return cardCounts.values.sum()
    }
}
