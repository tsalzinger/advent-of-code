package me.salzinger.puzzles.puzzle24

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle24.`Crossed Wires`.getNumberOutputOnZWires

fun main() {
    "puzzles/puzzle24/puzzle-24.in"
        .streamInput()
        .getNumberOutputOnZWires()
        .also(::println)
}