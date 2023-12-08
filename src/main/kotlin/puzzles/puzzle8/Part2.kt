package me.salzinger.puzzles.puzzle8

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle8.HauntedWasteland.getParallelStepCountWithLeftRightInstructions

fun main() {
    "puzzles/puzzle8/puzzle-8.in"
        .streamInput()
        .getParallelStepCountWithLeftRightInstructions()
        .also(::println)
}
