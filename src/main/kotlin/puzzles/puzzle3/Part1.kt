package me.salzinger.puzzles.puzzle3

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle3.MullItOver.sumOfMultiplyInstructions

fun main() {
    "puzzles/puzzle3/puzzle-3.in"
        .streamInput()
        .sumOfMultiplyInstructions()
        .also(::println)
}
