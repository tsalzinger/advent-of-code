package me.salzinger.puzzles.puzzle7

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle7.`Bridge Repair`.sumOfPossibleCalibrationValues

fun main() {
    "puzzles/puzzle7/puzzle-7.in"
        .streamInput()
        .sumOfPossibleCalibrationValues()
        .also(::println)
}