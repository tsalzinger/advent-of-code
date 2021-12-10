package me.salzinger

import java.util.*

fun main() {
    val pairs = mapOf(
        ')' to '(',
        ']' to '[',
        '}' to '{',
        '>' to '<',
    )

    val scores = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137,
    )

    10.solve(1) {

        mapNotNull { line ->
            val stack = Stack<Char>()
            line.firstOrNull { item ->
                if (item in "([{<") {
                    stack.push(item)
                    false
                } else if (stack.peek() == pairs.getValue(item)) {
                    stack.pop()
                    false
                } else {
                    true
                }
            }
        }.map { scores.getValue(it) }.sum().toString()
    }
}

