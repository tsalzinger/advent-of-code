package me.salzinger

fun main() {
    6.solve(1) {
        flatMap { it.toIntList() }
            .sumOf { 1 + Lanternfish.getNumberOfOffspringAfterDays(it, 80) }
            .toString()
    }
}
