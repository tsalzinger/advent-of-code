package me.salzinger.puzzles.puzzle19

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puz.`Linen Layout`.countArrangementPossibilities

fun main() {
    "puzzles/puzzle19/puzzle-19.in"
        .streamInput()
        .countArrangementPossibilities()
        .also(::println)
}