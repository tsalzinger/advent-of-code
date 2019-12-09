package me.salzinger


fun main() {
    solvePuzzle9()
    solvePuzzle10()
}

private fun interpret(memory: Memory, input: Int) = IntcodeProgramInterpreter(
    memory,
    inputs = listOf(input).toBigIntegerList().asInputProvider()
).evaluate()

private fun solvePuzzle9() {
    9.solve {
        convertIntcodeInput()
            .map {
                interpret(it, 1).executionContext.output.getOutput()
            }
            .first()
            .joinToString("\n")
    }
}

private fun solvePuzzle10() {
    10.solve {
        convertIntcodeInput()
            .map {
                interpret(it, 5).executionContext.output.getOutput()
            }
            .first()
            .joinToString("\n")
    }
}