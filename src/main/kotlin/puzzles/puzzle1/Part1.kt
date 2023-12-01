package me.salzinger.puzzles.puzzle1

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle1.Trebuchet.Part1.solve

fun main() {
    "puzzles/puzzle1/puzzle-1.in"
        .streamInput()
        .solve()
        .also(::println)
}
