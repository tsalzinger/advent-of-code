package me.salzinger

fun main() {
    8.solve(1) {
        map {
            it.split("|").last().trim()
        }.flatMap {
            it.split(" ")
        }.count { it.length in listOf(2, 3, 4, 7) }.toString()
    }
}
