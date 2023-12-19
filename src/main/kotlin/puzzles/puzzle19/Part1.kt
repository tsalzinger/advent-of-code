package me.salzinger.puzzles.puzzle19

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle19.Aplenty.getTotalSumOfPartRatings

fun main() {
    "puzzles/puzzle19/puzzle-19.in"
        .streamInput()
        .getTotalSumOfPartRatings()
        .also(::println)
}
