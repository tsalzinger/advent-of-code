package me.salzinger.puzzles.puzzle1

object Trebuchet {
    fun String.getFirstAndLastDigit(): Pair<Int, Int> {
        return first { it.isDigit() }.digitToInt() to last { it.isDigit() }.digitToInt()
    }

    fun String.getCalibrationValue(): Int {
        return getFirstAndLastDigit()
            .run {
                first * 10 + second
            }
    }

    fun Sequence<String>.getSumOfCalibartionValues(): Int {
        return sumOf {
            it.getCalibrationValue()
        }
    }

    object Part1 {
        fun Sequence<String>.solve(): Int {
            return getSumOfCalibartionValues()
        }
    }
}
