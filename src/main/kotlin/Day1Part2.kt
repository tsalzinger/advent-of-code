package me.salzinger

fun main() {
    1.solve(2) {
        val measurements = map { it.toInt() }

        var count = 0
        for (i in 0..(measurements.size - 4)) {
            if (measurements[i] < measurements[i + 3]) {
                count++
            }
        }

        "$count"
    }
}
