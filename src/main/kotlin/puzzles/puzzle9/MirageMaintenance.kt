package me.salzinger.puzzles.puzzle9

import me.salzinger.common.extensions.toIntList
import java.util.*

object MirageMaintenance {

    fun List<Int>.getStepDifferences(): List<Int> {
        return windowed(2)
            .map { (first, second) ->
                second - first
            }
    }

    fun List<Int>.getStackOfStepDifferences(): Stack<List<Int>> {
        val stack = Stack<List<Int>>()

        var current = this
        stack.push(current)

        while (current.any { it != 0 }) {
            current = current.getStepDifferences()
            stack.push(current)
        }

        return stack
    }

    fun Stack<List<Int>>.predictNextValue(): Int {
        return map {
            it.last()
        }.reduceRight { value, difference ->
            value + difference
        }
    }

    fun List<Int>.predictNextValue(): Int {
        return getStackOfStepDifferences()
            .predictNextValue()
    }

    fun String.predictNextValue(): Int {
        return toIntList(" ").predictNextValue()
    }

    fun Sequence<String>.getSumOfExtrapolatedValues(): Long {
        return map {
            it.predictNextValue().toLong()
        }.sum()
    }

    fun Sequence<String>.getSumOfPreviousExtrapolatedValues(): Long {
        return map {
            it.toIntList(" ").reversed().predictNextValue().toLong()
        }.sum()
    }
}
