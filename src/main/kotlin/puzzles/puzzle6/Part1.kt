package me.salzinger.puzzles.puzzle6

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle6.WaitForIt.getProductOfNumberOfWaysToBeatRecordDistance

fun main() {
    "puzzles/puzzle6/puzzle-6.in"
        .streamInput()
        .getProductOfNumberOfWaysToBeatRecordDistance()
        .also(::println)
}
