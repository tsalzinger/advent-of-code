package me.salzinger

import java.math.BigInteger

typealias Opcode = Int
typealias Memory = Map<Int, BigInteger>

fun List<String>.convertIntcodeInput(): List<Memory> = map(String::convertIntcodeInput)
fun String.convertIntcodeInput(): Memory =
    split(",")
        .map(String::toBigInteger)
        .toMemory()

fun List<BigInteger>.toMemory() =
    this.mapIndexed { index, value -> index to value }.toMap().withDefault { BigInteger.ZERO }

fun interpret(memory: Memory, overrides: Map<Int, Int>): ExecutionStatus {
    return IntcodeProgramInterpreter(memory, overrides.mapValues { (_, value) -> value.toBigInteger() }).evaluate()
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
                interpret(it, 12, 2).executionContext.memory.getValue(0)
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
                val result = interpret(initialMemory, noun, verb).executionContext.memory.getValue(0)
                if (result == 19690720.toBigInteger()) {
                    return@solve "${100 * noun + verb}"
                }
            }
        }

        throw RuntimeException("No solution found")
    }
}