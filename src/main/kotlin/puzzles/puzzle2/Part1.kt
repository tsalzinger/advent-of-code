package me.salzinger.puzzles.puzzle2

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle2.CubeConundrum.getSumOfPossibleGameIds

fun main() {
    "puzzles/puzzle2/puzzle-2.in"
        .streamInput()
        .getSumOfPossibleGameIds(
            mapOf(
                CubeConundrum.Color.RED to 12,
                CubeConundrum.Color.GREEN to 13,
                CubeConundrum.Color.BLUE to 14,
            )
        )
        .also(::println)
}
