package me.salzinger.puzzles.puzzle6

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle6.GuardGallivant.countPositionsVisitedByGuard

fun main() {
    "puzzles/puzzle6/puzzle-6.in"
        .streamInput()
        .countPositionsVisitedByGuard()
        .also(::println)
}