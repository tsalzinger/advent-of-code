package me.salzinger

typealias Opcode = Int
typealias Memory = List<Int>
typealias WriteableMemory = MutableList<Int>

typealias ParameterMode = Int

fun List<String>.convertIntcodeInput(): List<Memory> = map { it.split(",").map(String::toInt) }
fun String.convertIntcodeInput(): Memory = split(",").map(String::toInt)

fun resolveParameter(memory: Memory, instruction: Instruction, position: Int, memoryOffset: Int): Int {
    val parameter = memory[memoryOffset + position]
    return when (val parameterMode = instruction.getParameterMode(position)) {
        0 -> {
            if (parameter < 0) {
                throw RuntimeException("Cannot get value from negative memory address. Instruction: $instruction Position: $position Offset: $memoryOffset ParameterMode: $parameterMode")
            } else {
                memory[parameter]
            }
        }
        1 -> parameter
        else -> throw RuntimeException("Unsupported parameter mode $parameterMode")
    }
}

data class Instruction(
    val opcode: Opcode,
    val parameterModes: List<ParameterMode>
) {
    fun getParameterMode(position: Int): ParameterMode {
        return if (parameterModes.size >= position) {
            parameterModes[position - 1]
        } else {
            0
        }
    }

    companion object {
        fun init(value: Int): Instruction {
            val parameterModes = value.toString().dropLast(2)
            val opcode: Opcode = value.toString().drop(parameterModes.length).toInt()
            return Instruction(opcode, parameterModes.reversed().map { it.toString().toInt() })
        }
    }
}

data class ProgramOutput(
    val memory: Memory,
    val output: List<Int>
)

fun interpret(startMemory: Memory, overrides: Map<Int, Int> = emptyMap(), input: Int? = null): ProgramOutput {
    val memory: WriteableMemory = startMemory.toMutableList()

    val output = mutableListOf<Int>()

    overrides.forEach { index, override ->
        memory[index] = override
    }

    var index = 0
    var instruction = Instruction.init(memory[index])


    while (instruction.opcode != 99) {
        when (val opcode = instruction.opcode) {
            1 -> {
                memory[memory[index + 3]] = resolveParameter(memory, instruction, 1, index) + resolveParameter(memory, instruction, 2, index)
                index += 4
            }
            2 -> {
                memory[memory[index + 3]] = resolveParameter(memory, instruction, 1, index) * resolveParameter(memory, instruction, 2, index)
                index += 4
            }
            3 -> {
                memory[memory[index + 1]] = input ?: throw RuntimeException("No input available")
                index += 2
            }
            4 -> {
                output.add(resolveParameter(memory, instruction, 1, index))
                index += 2
            }
            5 -> {
                if (resolveParameter(memory, instruction, 1, index) != 0) {
                    index = resolveParameter(memory, instruction, 2, index)
                } else {
                    index += 3
                }
            }
            6 -> {
                if (resolveParameter(memory, instruction, 1, index) == 0) {
                    index = resolveParameter(memory, instruction, 2, index)
                } else {
                    index += 3
                }
            }
            7 -> {
                val first = resolveParameter(memory, instruction, 1, index)
                val second = resolveParameter(memory, instruction, 2, index)
                val outputAddress = memory[index + 3]

                if (first < second) {
                    memory[outputAddress] = 1
                } else {
                    memory[outputAddress] = 0
                }
                index += 4
            }
            8 -> {
                val first = resolveParameter(memory, instruction, 1, index)
                val second = resolveParameter(memory, instruction, 2, index)
                val outputAddress = memory[index + 3]

                if (first == second) {
                    memory[outputAddress] = 1
                } else {
                    memory[outputAddress] = 0
                }
                index += 4
            }
            else -> throw RuntimeException("Unknown opcode $opcode")
        }

        instruction = Instruction.init(memory[index])
    }

    return ProgramOutput(
        memory,
        output
    )
}

fun interpret(program: Memory, noun: Int, verb: Int): ProgramOutput {
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
                interpret(it, 12, 2).memory.first()
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
                val result = interpret(initialMemory, noun, verb).memory.first()
                if (result == 19690720) {
                    return@solve "${100 * noun + verb}"
                }
            }
        }

        throw RuntimeException("No solution found")
    }
}