package me.salzinger


fun main() {
    solvePuzzle9()
    solvePuzzle10()
}

private fun solvePuzzle9() {
    9.solve {
        convertIntcodeInput()
            .map {
                interpret(it, emptyMap(), 1).output
            }
            .first()
            .joinToString("\n")
    }
}

private fun solvePuzzle10() {
    10.solve {
        convertIntcodeInput()
            .map {
                interpret(it, emptyMap(), 5).output
            }
            .first()
            .joinToString("\n")
    }
}