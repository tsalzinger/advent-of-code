package me.salzinger.puzzles.puzzle14

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle14.`Restroom Redoubt`.findTheEasterEgg
import me.salzinger.puzzles.puzzle14.`Restroom Redoubt`.getSafetyFactorAfter100Seconds

fun main() {
    "puzzles/puzzle14/puzzle-14.in"
        .streamInput()
        .findTheEasterEgg()
        .also(::println)
}