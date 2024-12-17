package me.salzinger.puzzles.puzzle17

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle17.`Chronospatial Computer`.getProgramOutput

fun main() {
    "puzzles/puzzle17/puzzle-17.in"
        .streamInput()
        .getProgramOutput()
        .also(::println)
}