package me.salzinger.puzzles.puzzle2

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle2.RedNosedReports.countSafeReportsWithProblemDampener

fun main() {
    "puzzles/puzzle2/puzzle-2.in"
        .streamInput()
        .countSafeReportsWithProblemDampener()
        .also(::println)
}