package me.salzinger.puzzles.puzzle21

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle21.StepCounter.getNumberOfReachableTilesAfterSteps

fun main() {
    "puzzles/puzzle21/puzzle-21.in"
        .streamInput()
        .getNumberOfReachableTilesAfterSteps(stepCount = 64)
        .also(::println)
}
