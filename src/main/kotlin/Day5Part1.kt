package me.salzinger

import me.salzinger.Line.Companion.toLine

fun main() {
    5.solve(1) {
        map { it.toLine() }
            .flatMap { it.mapToPoints() }
            .groupBy { it }
            .count { it.value.size >= 2 }
            .toString()
    }
}
