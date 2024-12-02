package me.salzinger.puzzles.puzzle2

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle2.RedNosedReports.countSafeReports

fun main() {
    "puzzles/puzzle2/puzzle-2.in"
        .streamInput()
        .countSafeReports()
        .also(::println)
}