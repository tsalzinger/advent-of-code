package me.salzinger.puzzles.puzzle24

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle24.NeverTellMeTheOdds.getNumberOfFutureXYIntersectionsWithin

private const val BOUNDARY_MIN = 200_000_000_000_000L
private const val BOUNDARY_MAX = 400_000_000_000_000L

fun main() {
    "puzzles/puzzle24/puzzle-24.in"
        .streamInput()
        .getNumberOfFutureXYIntersectionsWithin(
            BOUNDARY_MIN,
            BOUNDARY_MAX,
        )
        .also(::println)
}
