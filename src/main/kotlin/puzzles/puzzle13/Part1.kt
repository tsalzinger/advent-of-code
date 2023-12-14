package me.salzinger.puzzles.puzzle13

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle13.PointOfIncidence.getSummaryOfNotes

fun main() {
    "puzzles/puzzle13/puzzle-13.in"
        .streamInput()
        .getSummaryOfNotes()
        .also(::println)
}
