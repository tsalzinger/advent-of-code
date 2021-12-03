package me.salzinger

fun main() {
    1.solve(1) {
        var previous = Int.MAX_VALUE
        map { it.toInt() }
            .count {
                val comparison = it > previous

                previous = it

                comparison
            }
            .toString()
    }
}
