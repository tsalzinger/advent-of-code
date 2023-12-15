package me.salzinger.puzzles.puzzle15

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle15.LensLibrary.getSumOfHashes

fun main() {
    "puzzles/puzzle15/puzzle-15.in"
        .streamInput()
        .getSumOfHashes()
        .also(::println)
}
