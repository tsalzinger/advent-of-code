package me.salzinger

import kotlin.math.absoluteValue

fun main() {
    7.solve(1) {
        flatMap { it.toIntList() }
            .sorted()
            .run {
                val median = get(size / 2)
                sumOf {
                    (it - median).absoluteValue
                }
            }
            .toString()
    }
}
