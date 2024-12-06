package me.salzinger.puzzles.puzzle6

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle6.GuardGallivant.countPositionsVisitedByGuard
import me.salzinger.puzzles.puzzle6.GuardGallivant.countPossibleObstacleLocationsToLoop

fun main() {
    "puzzles/puzzle6/puzzle-6.in"
        .streamInput()
        .countPossibleObstacleLocationsToLoop()
        .also(::println)
}