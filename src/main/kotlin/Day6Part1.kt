package me.salzinger

import java.math.BigDecimal

fun main() {
    6.solve(1) {
        flatMap { it.toIntList() }
            .sumOf { BigDecimal.ONE + Lanternfish.getNumberOfOffspringAfterDays(it, 80) }
            .toString()
    }
}
