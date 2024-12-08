package me.salzinger.puzzles.puzzle8

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle8.`Resonant Collinearity`.countUniqueAntinodeLocations

fun main() {
    "puzzles/puzzle8/puzzle-8.in"
        .streamInput()
        .countUniqueAntinodeLocations()
        .also(::println)
}