package me.salzinger.puzzles.puzzle3

object MullItOver {
    val MULTIPLY_INSTRUCTION_REGEX = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")

    fun Sequence<String>.sumOfMultiplyInstructions(): Int {
        return flatMap {
            MULTIPLY_INSTRUCTION_REGEX.findAll(it)
        }
            .map { matchResult ->
                val (first, second) = matchResult.destructured.toList().map { it.toInt() }
                first * second
            }
            .sum()
    }
}