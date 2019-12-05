package me.salzinger

typealias Opcode = Int
typealias Memory = List<Int>
typealias WriteableMemory = MutableList<Int>

fun List<String>.convertIntcodeInput(): List<Memory> = map { it.split(",").map(String::toInt) }
fun String.convertIntcodeInput(): Memory = split(",").map(String::toInt)

fun interpret(memory: Memory, overrides: Map<Int, Int>): IntcodeProgramResult {
    return IntcodeProgramInterpreter(memory, overrides).evaluate()
}

fun interpret(memory: Memory, noun: Int, verb: Int) = interpret(memory, mapOf(1 to noun, 2 to verb))

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