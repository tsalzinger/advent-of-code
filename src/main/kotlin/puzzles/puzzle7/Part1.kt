package me.salzinger.puzzles.puzzle6

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle6.CamelCards.toCamelCardGame

fun main() {
    "puzzles/puzzle7/puzzle-7.in"
        .streamInput()
        .toCamelCardGame()
        .getTotalWinnings()
        .also(::println)
}
