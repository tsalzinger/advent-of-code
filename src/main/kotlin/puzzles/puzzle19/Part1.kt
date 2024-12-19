package me.salzinger.puzzles.puzzle19

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puz.`Linen Layout`.countPossibleDesigns

fun main() {
    "puzzles/puzzle19/puzzle-19.in"
        .streamInput()
        .countPossibleDesigns()
        .also(::println)
}