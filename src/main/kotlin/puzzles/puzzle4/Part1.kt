package me.salzinger.puzzles.puzzle4

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle4.CeresSearch.countOccurrencesOfXmas

fun main() {
    "puzzles/puzzle4/puzzle-4.in"
        .streamInput()
        .countOccurrencesOfXmas()
        .also(::println)
}
