package me.salzinger.puzzles.puzzle11

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle11.`Plutonian Pebbles`.countStonesAfterBlinking75Times

fun main() {
    "puzzles/puzzle11/puzzle-11.in"
        .streamInput()
        .countStonesAfterBlinking75Times()
        .also(::println)
}