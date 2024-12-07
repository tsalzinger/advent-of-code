package me.salzinger.puzzles.puzzle7

import me.salzinger.common.extensions.toLongList

object `Bridge Repair` {
    fun Pair<Long, Long>.possibleCombinations(): List<Long> {
        return listOf(
            first + second,
            first * second,
        )
    }

    fun List<Long>.possibleCombinations(): List<Long> {
        return if (size == 1) {
            listOf(first())
        } else {
            (first() to get(1)).possibleCombinations()
                .flatMap { it ->
                    (listOf(it) + subList(2, size)).possibleCombinations()
                }
        }
    }

    data class CalibrationEquation(
        val testValue: Long,
        val measurements: List<Long>,
    ) {
        val canBeSolved: Boolean
            get() {
                return testValue in measurements.possibleCombinations()
            }
    }

    fun Sequence<String>.sumOfPossibleCalibrationValues(): Long {
        return map { calibrationEquation ->
            val (testValue, measurements) = calibrationEquation.split(": ")

            CalibrationEquation(
                testValue.toLong(),
                measurements.toLongList(" ")
            )
        }
            .filter { equation -> equation.canBeSolved }
            .sumOf { equation -> equation.testValue }
    }
}