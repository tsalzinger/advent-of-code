package me.salzinger.puzzles.puzzle7

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle7.`Bridge Repair`.sumOfPossibleCalibrationValues
import me.salzinger.puzzles.puzzle7.`Bridge Repair`.sumOfPossibleCalibrationValuesWithConcatenateOperator

fun main() {
    "puzzles/puzzle7/puzzle-7.in"
        .streamInput()
        .sumOfPossibleCalibrationValuesWithConcatenateOperator()
        .also(::println)
}