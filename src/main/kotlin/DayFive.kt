package me.salzinger


fun main() {
    solvePuzzle9()
}

private fun solvePuzzle9() {
    9.solve {
        convertIntcodeInput()
            .map {
                interpret(it).output
            }
            .first()
            .joinToString("\n")
    }
}