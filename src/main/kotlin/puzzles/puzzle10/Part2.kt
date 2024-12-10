package me.salzinger.puzzles.puzzle10

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle10.`Hoof It`.sumOfAllTrailheadRatings

fun main() {
    "puzzles/puzzle10/puzzle-10.in"
        .streamInput()
        .sumOfAllTrailheadRatings()
        .also(::println)
}