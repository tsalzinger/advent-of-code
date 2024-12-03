package me.salzinger.puzzles.puzzle3

object MullItOver {
    val MULTIPLY_INSTRUCTION_REGEX = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")
    val MULTIPLY_OR_CONDITIONAL_INSTRUCTION_REGEX = Regex("do\\(\\)|don't\\(\\)|mul\\((\\d{1,3}),(\\d{1,3})\\)")

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

    fun Sequence<String>.sumOfMultiplyInstructionsWithConditionals(): Int {
        var evaluate = true
        return flatMap {
            MULTIPLY_OR_CONDITIONAL_INSTRUCTION_REGEX.findAll(it)
        }
            .map { matchResult ->
                when (matchResult.value) {
                    "do()" -> {
                        evaluate = true
                        0
                    }
                    "don't()" -> {
                        evaluate = false
                        0
                    }
                    else -> {
                        if (evaluate) {
                            val (first, second) = matchResult.destructured.toList().map { it.toInt() }
                            first * second
                        } else {
                            0
                        }
                    }
                }
            }
            .sum()
    }
}