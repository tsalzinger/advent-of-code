package me.salzinger.puzzles.puzzle25

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle25.`Code Chronicle`.countUniqueFittingLockKeyPairs

fun main() {
    "puzzles/puzzle25/puzzle-25.in"
        .streamInput()
        .countUniqueFittingLockKeyPairs()
        .also(::println)
}