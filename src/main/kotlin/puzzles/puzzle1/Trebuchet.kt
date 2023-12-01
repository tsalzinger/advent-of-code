package me.salzinger.puzzles.puzzle1

object Trebuchet {

    fun Pair<Int, Int>.toCalibrationValue(): Int {
        return first * 10 + second
    }

    object Part1 {
        fun String.getFirstAndLastDigit(): Pair<Int, Int> {
            return first { it.isDigit() }.digitToInt() to last { it.isDigit() }.digitToInt()
        }

        fun String.getCalibrationValue(): Int {
            return getFirstAndLastDigit()
                .toCalibrationValue()
        }

        fun Sequence<String>.getSumOfCalibartionValues(): Int {
            return sumOf {
                it.getCalibrationValue()
            }
        }

        fun Sequence<String>.solve(): Int {
            return getSumOfCalibartionValues()
        }
    }

    object Part2 {
        val searchValues = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9,
            "1" to 1,
            "2" to 2,
            "3" to 3,
            "4" to 4,
            "5" to 5,
            "6" to 6,
            "7" to 7,
            "8" to 8,
            "9" to 9,
        )

        class NoDigitFoundException(
            input: String,
        ) : RuntimeException("Failed to find a digit in input string $input")

        fun String.getFirstAndLastDigit(): Pair<Int, Int> {
            val first =
                findAnyOf(searchValues.keys)?.second?.run(searchValues::get) ?: throw NoDigitFoundException(this)
            val last =
                findLastAnyOf(searchValues.keys)?.second?.run(searchValues::get) ?: throw NoDigitFoundException(this)

            return first to last
        }


        fun Sequence<String>.solve(): Int {
            return map {
                it.getFirstAndLastDigit().toCalibrationValue()
            }.sum()
        }
    }
}
