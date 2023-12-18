package me.salzinger.puzzles.puzzle18

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle18.LavaductLagoon.getLagoonSize

fun main() {
    "puzzles/puzzle18/puzzle-18.in"
        .streamInput()
        .getLagoonSize()
        .also(::println)
}
