package me.salzinger.puzzles.puzzle22

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle22.SandSlabs.getNumberOfBricksWhichCanSafelyBeDisintegrated

fun main() {
    "puzzles/puzzle22/puzzle-22.in"
        .streamInput()
        .getNumberOfBricksWhichCanSafelyBeDisintegrated()
        .also(::println)
}
