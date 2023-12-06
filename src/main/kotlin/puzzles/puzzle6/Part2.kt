package me.salzinger.puzzles.puzzle6

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle6.WaitForIt.getNumberOfWaysToBeatRecordDistance

fun main() {
    "puzzles/puzzle6/puzzle-6-part-2.in"
        .streamInput()
        .getNumberOfWaysToBeatRecordDistance()
        .also(::println)
}
