package me.salzinger.puzzles.puzzle7

import me.salzinger.common.extensions.toLongList

object `Bridge Repair` {
    sealed interface Operator {
        fun evaluate(a: Long, b: Long): Long

        object Add : Operator {
            override fun evaluate(a: Long, b: Long) = a + b
        }

        object Multiply : Operator {
            override fun evaluate(a: Long, b: Long) = a * b
        }

        object Concatenate : Operator {
            override fun evaluate(a: Long, b: Long) = "$a$b".toLong()
        }
    }

    fun Pair<Long, Long>.possibleCombinations(
        operators: List<Operator>,
    ): List<Long> {
        return operators
            .map { operator -> operator.evaluate(first, second) }
    }

    fun List<Long>.possibleCombinations(
        operators: List<Operator>,
    ): List<Long> {
        return if (size == 1) {
            listOf(first())
        } else {
            (first() to get(1)).possibleCombinations(operators)
                .flatMap { it ->
                    (listOf(it) + subList(2, size)).possibleCombinations(operators)
                }
        }
    }

    data class CalibrationEquation(
        val testValue: Long,
        val measurements: List<Long>,
        val operators: List<Operator> = listOf(
            Operator.Add,
            Operator.Multiply,
        ),
    ) {
        val canBeSolved: Boolean
            get() {
                return testValue in measurements.possibleCombinations(operators)
            }
    }

    fun Sequence<String>.sumOfPossibleCalibrationValues(): Long {
        return map { calibrationEquation ->
            val (testValue, measurements) = calibrationEquation.split(": ")

            CalibrationEquation(
                testValue.toLong(),
                measurements.toLongList(" "),
            )
        }
            .filter { equation -> equation.canBeSolved }
            .sumOf { equation -> equation.testValue }
    }

    fun Sequence<String>.sumOfPossibleCalibrationValuesWithConcatenateOperator(): Long {
        return map { calibrationEquation ->
            val (testValue, measurements) = calibrationEquation.split(": ")

            CalibrationEquation(
                testValue.toLong(),
                measurements.toLongList(" "),
                operators = listOf(
                    Operator.Add,
                    Operator.Multiply,
                    Operator.Concatenate,
                ),
            )
        }
            .filter { equation -> equation.canBeSolved }
            .sumOf { equation -> equation.testValue }
    }
}