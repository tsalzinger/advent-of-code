package me.salzinger.puzzles.puzzle4

import me.salzinger.common.extensions.toIntList
import me.salzinger.common.math.pow

object Scratchcards {
    data class Scratchcard(
        val winningNumbers: Set<Int>,
        val playerNumbers: Set<Int>,
    ) {
        val points: Long by lazy {
            val countOfMatchingNumbers = playerNumbers.count {
                it in winningNumbers
            }

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
}
