package me.salzinger.puzzles.puzzle17

import me.salzinger.common.extensions.toIntList

object `Chronospatial Computer` {

    enum class Register {
        A, B, C,
    }


    fun Int.literalOperand(): Int = this
    fun Int.comboOperand(registers: Map<Register, Int>): Int {
        return when (this) {
            in 0..3 -> this
            4 -> registers.getValue(Register.A)
            5 -> registers.getValue(Register.B)
            6 -> registers.getValue(Register.C)
            else -> error("Unsupported combo operand $this")
        }
    }

    infix fun Int.threeBitXor(other: Int): Int {
        return (this xor other)
            .toString(radix = 2)
            .run {
                slice((count() - 3)..<count())
            }
            .toInt()
    }

    class Computer(
        initialRegisters: Map<Register, Int>,
    ) {
        var instructionPointer = 0
        val registers = initialRegisters.toMutableMap()

        fun execute(program: List<Int>): List<Int> {
            val output = mutableListOf<Int>()
            while ((instructionPointer + 1) < program.size) {
                val opcode = program[instructionPointer]
                val operand = program[instructionPointer + 1]

                when (opcode) {
                    0 -> {
                        // adv
                        Register.A(
                            registerADivision(operand)
                        )
                        increaseInstructionPointer()
                    }

                    1 -> {
                        // bxl
                        Register.B(
                            Register.B() xor operand.literalOperand()
                        )
                        increaseInstructionPointer()
                    }

                    2 -> {
                        // bst
                        Register.B(
                            operand.comboOperand(registers) % 8,
                        )
                        increaseInstructionPointer()
                    }

                    3 -> {
                        // jnz
                        when (val a = Register.A()) {
                            0 -> increaseInstructionPointer()
                            else -> {
                                instructionPointer = operand.literalOperand()
                            }
                        }
                    }

                    4 -> {
                        // bxc
                        Register.B(
                            Register.B() xor Register.C()
                        )
                        increaseInstructionPointer()
                    }

                    5 -> {
                        // out
                        output += operand.comboOperand(registers) % 8
                        increaseInstructionPointer()
                    }

                    6 -> {
                        // bdv
                        Register.B(
                            registerADivision(operand)
                        )
                        increaseInstructionPointer()
                    }

                    7 -> {
                        // cdv
                        Register.C(
                            registerADivision(operand)
                        )
                        increaseInstructionPointer()
                    }
                }
            }
            return output
        }

        private fun registerADivision(operand: Int): Int {
            val exponent = operand.comboOperand(registers)

            return if (exponent == 0) {
                Register.A()
            } else {
                (0..<exponent)
                    .fold(Register.A()) { res, _ ->
                        res / 2
                    }
            }
        }

        fun increaseInstructionPointer() {
            instructionPointer += 2
        }

        operator fun Register.invoke(): Int {
            return registers.getValue(this)
        }

        operator fun Register.invoke(value: Int) {
            registers.put(this, value)
        }
    }

    fun Sequence<String>.getProgramOutput(): String {
        val (a, b, c, program) = toList()
            .mapNotNull {
                val parts = it.split(": ")
                if (parts.size == 2) {
                    parts[1]
                } else {
                    null
                }
            }

        return Computer(
            mapOf(
                Register.A to a.toInt(),
                Register.B to b.toInt(),
                Register.C to c.toInt(),
            )
        ).execute(program.toIntList(","))
            .joinToString(",")

    }
}