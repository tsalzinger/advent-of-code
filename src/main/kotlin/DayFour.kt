package me.salzinger

import kotlin.math.max

fun List<String>.convertToRange() = first().split('-').let { (a, b) -> (a.toInt())..(b.toInt()) }

fun Int.meetsPasswordCriteria(): Boolean {
    val password = toString()
    var maxNum: Int = Int.MIN_VALUE
    var valid = password.length == 6
    var hasDouble = false
    var index = 0
    while (valid && index < password.length) {
        val current = password[index].toInt()
        valid = valid && current >= maxNum
        hasDouble = hasDouble || current == maxNum
        maxNum = max(maxNum, current)
        index++
    }
    return valid && hasDouble
}

fun main() {
    solvePuzzle7()
}

private fun solvePuzzle7() {
    7.solve {
        convertToRange()
            .filter(Int::meetsPasswordCriteria)
            .count()
            .toString()
    }
}