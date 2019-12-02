package me.salzinger

typealias Opcode = Int
typealias IntcodeProgram = List<Opcode>
typealias IntcodeResult = List<Int>

private fun List<String>.convertIntcodeInput(): List<IntcodeProgram> = map { it.split(",").map(String::toInt) }

fun interpret(program: IntcodeProgram, overrides: Map<Int, Int>): IntcodeResult {
    val output = program.toMutableList()

    overrides.forEach { index, override ->
        output[index] = override
    }

    var index = 0
    var opcode = output[index]

    while (opcode != 99) {
        when (opcode) {
            1 -> output[output[index + 3]] = output[output[index + 1]] + output[output[index + 2]]
            2 -> output[output[index + 3]] = output[output[index + 1]] * output[output[index + 2]]
            else -> throw RuntimeException("Unknown opcode $opcode")
        }

        index += 4
        opcode = output[index]
    }

    return output
}

fun main() {
    solvePuzzle3()
}

private fun solvePuzzle3() {
    3.solve {
        convertIntcodeInput()
            .map {
                interpret(it, mapOf(1 to 12, 2 to 2)).first()
            }
            .first()
            .toString()
    }
}
