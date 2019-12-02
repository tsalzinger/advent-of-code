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

fun interpret(program: IntcodeProgram, noun: Int, verb: Int): IntcodeResult {
    return interpret(program, mapOf(1 to noun, 2 to verb))
}

fun main() {
    solvePuzzle3()
    solvePuzzle4()
}

private fun solvePuzzle3() {
    3.solve {
        convertIntcodeInput()
            .map {
                interpret(it, 12, 2).first()
            }
            .first()
            .toString()
    }
}

private fun solvePuzzle4() {
    4.solve {
        val initialMemory = convertIntcodeInput().first()

        for (noun in 0..99) {
            for (verb in 0..99) {
                val result = interpret(initialMemory, noun, verb).first()
                if (result == 19690720) {
                    return@solve "${100 * noun + verb}"
                }
            }
        }

        throw RuntimeException("No solution found")
    }
}