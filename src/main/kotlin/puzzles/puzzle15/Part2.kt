package me.salzinger.puzzles.puzzle15

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle15.LensLibrary.getSumOfFocusPowerPerLens

fun main() {
    "puzzles/puzzle15/puzzle-15.in"
        .streamInput()
        .getSumOfFocusPowerPerLens()
        .also(::println)
}
