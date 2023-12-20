package me.salzinger.puzzles.puzzle20

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle20.PulsePropagation.getNumberOfPulsesAfter1000Iterations

fun main() {
    "puzzles/puzzle20/puzzle-20.in"
        .streamInput()
        .getNumberOfPulsesAfter1000Iterations()
        .also(::println)
}
