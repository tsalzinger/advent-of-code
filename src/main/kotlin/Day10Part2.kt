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
        '(' to 1,
        '[' to 2,
        '{' to 3,
        '<' to 4,
    )

    10.solve(2) {

        mapNotNull { line ->
            val stack = Stack<Char>()
            val valid = line.all { item ->
                if (item in "([{<") {
                    stack.push(item)
                    true
                } else if (stack.peek() == pairs.getValue(item)) {
                    stack.pop()
                    true
                } else {
                    false
                }
            }

            if (valid) {
                stack
            } else {
                null
            }
        }.map { stack ->
            stack
                .reversed()
                .fold(0L) { sum, item ->
                    (sum * 5) + scores.getValue(item)
                }
        }.sorted()
            .run {
                get(count() / 2)
            }
            .toString()
    }
}

