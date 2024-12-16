package me.salzinger.puzzles.puzzle15

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle15.`Warehouse Woes`.sumOfGpsCoordinates

fun main() {
    "puzzles/puzzle15/puzzle-15.in"
        .streamInput()
        .sumOfGpsCoordinates()
        .also(::println)
}