package me.salzinger.puzzles.puzzle19

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle19.Aplenty.getNumberOfPossibleAcceptedCombinations

fun main() {
    "puzzles/puzzle19/puzzle-19.in"
        .streamInput()
        .getNumberOfPossibleAcceptedCombinations()
        .also(::println)
}
