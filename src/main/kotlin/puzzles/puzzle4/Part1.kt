package me.salzinger.puzzles.puzzle4

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle4.Scratchcards.sumOfPoints
import me.salzinger.puzzles.puzzle4.Scratchcards.toScratchcards

fun main() {
    "puzzles/puzzle4/puzzle-4.in"
        .streamInput()
        .toScratchcards()
        .sumOfPoints()
        .also(::println)
}
