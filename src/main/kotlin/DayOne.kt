package me.salzinger


fun Int.calculateRequiredFuel() = (this / 3) - 2


fun Int.calculateRequiredFuelRec(): Int {
    val fuel = calculateRequiredFuel()

    return if (fuel < 0) {
        0
    } else {
        fuel + fuel.calculateRequiredFuelRec()
    }
}


private fun List<String>.convertInput() = map { it.toInt() }

fun main() {
    solvePuzzle1()

    solvePuzzle2()
}

private fun solvePuzzle1() {
    1.solve {
        convertInput()
            .map(Int::calculateRequiredFuel)
            .reduce(Int::plus)
            .toString()
    }
}

private fun solvePuzzle2() {
    2.solve {
        convertInput()
            .map(Int::calculateRequiredFuelRec)
            .reduce(Int::plus)
            .toString()
    }
}