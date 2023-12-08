package me.salzinger.puzzles.puzzle8

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle8.HauntedWasteland.getStepCountWithLeftRightInstructions

fun main() {
    "puzzles/puzzle8/puzzle-8.in"
        .streamInput()
        .getStepCountWithLeftRightInstructions()
        .also(::println)
}
