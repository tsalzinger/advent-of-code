package me.salzinger

inline class MemoryAddress(val address: Int) {
    fun resolve(memory: Memory) =
        memory[address]

    operator fun plus(value: Int) = MemoryAddress(address + value)

    override fun toString() = address.toString()
}

data class IntcodeProgramResult(
    val memory: Memory,
    val output: List<Int>
)

class IntcodeProgramInterpreter(
    val memory: Memory,
    val overrides: Map<Int, Int> = emptyMap(),
    val inputs: List<Int> = emptyList()
) {
    fun evaluate(): IntcodeProgramResult {
        val output = mutableListOf<Int>()

        return ExecutionContext(
            memory.toMutableList().apply {
                overrides.entries.forEach {
                    this[it.key] = it.value
                }
            }.toList(),
            inputs,
            emptyList(),
            MemoryAddress(0)
        ).evaluate()
    }
}

data class ExecutionContext(
    val memory: Memory,
    val inputs: List<Int>,
    val output: List<Int>,
    val instructionPointer: MemoryAddress?
) {
    fun evaluate(): IntcodeProgramResult {
        return if (instructionPointer == null) {
            IntcodeProgramResult(
                memory,
                output
            )
        } else {
            IntcodeInstruction.init(instructionPointer.resolve(memory)).execute(this).evaluate()
        }
    }
}

object ParameterModes {
    val POSITION = ParameterMode(0)
    val IMMEDIATE = ParameterMode(1)
}

inline class ParameterMode(val mode: Int) {
    fun resolve(value: Int, memory: Memory): Int {
        return when (ParameterMode(mode)) {
            ParameterModes.POSITION -> {
                MemoryAddress(value).resolve(memory)
            }
            ParameterModes.IMMEDIATE -> value
            else -> throw RuntimeException("Unsupported parameter mode $mode")
        }
    }
}

class Parameter(
    val value: Int,
    val parameterMode: ParameterMode
)

sealed class IntcodeInstruction(
    val parameterCount: Int
) {
    fun debug(executionContext: ExecutionContext): ExecutionContext {
        println("$executionContext: ${executionContext.getParameters()}")
        println(javaClass.simpleName)
        return execute(executionContext)
    }

    abstract fun execute(executionContext: ExecutionContext): ExecutionContext
    val instructionLength = parameterCount + 1

    fun ExecutionContext.getParameters(): List<Parameter> {
        return if (parameterCount == 0) {
            emptyList()
        } else {
            (0 until parameterCount).map {
                resolveParameter(it)
            }
        }
    }

    fun ExecutionContext.read(parameter: Parameter) = parameter.parameterMode.resolve(parameter.value, memory)

    fun ExecutionContext.resolveParameter(parameterIndex: Int): Parameter {
        val parameter = memory[instructionPointer!!.address + parameterIndex + 1]
        val parameterModes = getParameterModes()

        return Parameter(parameter, parameterModes.getOrElse(parameterIndex) { ParameterModes.POSITION })
    }

    private fun ExecutionContext.getParameterModes(): List<ParameterMode> {
        return memory[instructionPointer!!.address].toString().dropLast(OPCODE_RANGE_LENGTH).reversed()
            .map { ParameterMode(it.toString().toInt()) }
    }

    companion object {
        private const val OPCODE_RANGE_LENGTH = 2
        fun init(value: Int): IntcodeInstruction {
            return when (val opcode: Opcode = value.toString().takeLast(OPCODE_RANGE_LENGTH).toInt()) {
                1 -> Add()
                2 -> Multiply()
                3 -> Input()
                4 -> Output()
                5 -> JumpIfTrue()
                6 -> JumpIfFalse()
                7 -> LessThen()
                8 -> Equals()
                99 -> Return()
                else -> throw RuntimeException("Unsupported opcode $opcode")
            }
        }
    }
}

class Add : IntcodeInstruction(3) {
    override fun execute(executionContext: ExecutionContext): ExecutionContext {
        val (p1, p2, p3) = executionContext.getParameters()

        return executionContext.copy(
            memory = executionContext.memory.toMutableList().apply {
                this[p3.value] = executionContext.read(p1) + executionContext.read(p2)
            },
            instructionPointer = executionContext.instructionPointer!! + instructionLength
        )
    }
}

class Multiply : IntcodeInstruction(3) {
    override fun execute(executionContext: ExecutionContext): ExecutionContext {
        val (p1, p2, p3) = executionContext.getParameters()

        return executionContext.copy(
            memory = executionContext.memory.toMutableList().apply {
                this[p3.value] = executionContext.read(p1) * executionContext.read(p2)
            },
            instructionPointer = executionContext.instructionPointer!! + instructionLength
        )
    }
}

class Input : IntcodeInstruction(1) {
    override fun execute(executionContext: ExecutionContext): ExecutionContext {
        val (output) = executionContext.getParameters()

        return executionContext.copy(
            memory = executionContext.memory.toMutableList().apply {
                this[output.value] = executionContext.inputs.first()
            }.toList(),
            inputs = executionContext.inputs.drop(1),
            instructionPointer = executionContext.instructionPointer!! + instructionLength
        )
    }
}

class Output : IntcodeInstruction(1) {
    override fun execute(executionContext: ExecutionContext): ExecutionContext {
        val (input) = executionContext.getParameters()

        return executionContext.copy(
            output = executionContext.output.toMutableList().apply {
                add(executionContext.read(input))

            }.toList(),
            instructionPointer = executionContext.instructionPointer!! + instructionLength
        )
    }
}

class JumpIfTrue : IntcodeInstruction(2) {
    override fun execute(executionContext: ExecutionContext): ExecutionContext {
        val (compareTo, jumpTo) = executionContext.getParameters()

        return executionContext.copy(
            instructionPointer = if (executionContext.read(compareTo) != 0) {
                MemoryAddress(executionContext.read(jumpTo))
            } else {
                executionContext.instructionPointer!! + instructionLength
            }
        )
    }
}

class JumpIfFalse : IntcodeInstruction(2) {
    override fun execute(executionContext: ExecutionContext): ExecutionContext {
        val (compareTo, jumpTo) = executionContext.getParameters()

        return executionContext.copy(
            instructionPointer = if (executionContext.read(compareTo) == 0) {
                MemoryAddress(executionContext.read(jumpTo))
            } else {
                executionContext.instructionPointer!! + instructionLength
            }
        )
    }
}

class LessThen : IntcodeInstruction(3) {
    override fun execute(executionContext: ExecutionContext): ExecutionContext {
        val (p1, p2, output) = executionContext.getParameters()

        return executionContext.copy(
            memory = executionContext.memory.toMutableList().apply {
                this[output.value] = if (executionContext.read(p1) < executionContext.read(p2)) {
                    1
                } else {
                    0
                }

            }.toList(),
            instructionPointer = executionContext.instructionPointer!! + instructionLength
        )
    }
}

class Equals : IntcodeInstruction(3) {
    override fun execute(executionContext: ExecutionContext): ExecutionContext {
        val (p1, p2, output) = executionContext.getParameters()

        return executionContext.copy(
            memory = executionContext.memory.toMutableList().apply {
                this[output.value] = if (executionContext.read(p1) == executionContext.read(p2)) {
                    1
                } else {
                    0
                }

            }.toList(),
            instructionPointer = executionContext.instructionPointer!! + instructionLength
        )
    }
}

class Return : IntcodeInstruction(0) {
    override fun execute(executionContext: ExecutionContext) =
        executionContext.copy(instructionPointer = null)
}