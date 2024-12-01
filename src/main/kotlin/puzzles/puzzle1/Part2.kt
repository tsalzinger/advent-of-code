package me.salzinger.puzzles.puzzle1

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle1.HistorianHysteria.getSimilarityScore
import me.salzinger.puzzles.puzzle1.HistorianHysteria.sumOfDifferences

fun main() {
    "puzzles/puzzle1/puzzle-1.in"
        .streamInput()
        .getSimilarityScore()
        .also(::println)
}