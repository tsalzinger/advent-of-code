package me.salzinger


fun Int.calculateRequiredFuel() = (this / 3) - 2

fun List<String>.convertInput() = map { it.toInt() }

fun main() {
    getPuzzleInput(1)
        .convertInput()
        .map(Int::calculateRequiredFuel)
        .reduce(Int::plus)
        .writePuzzleSolution(1)
}