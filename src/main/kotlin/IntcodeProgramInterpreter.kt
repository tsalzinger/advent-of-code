package me.salzinger

inline class MemoryAddress(val address: Int) {
    fun resolve(memory: Memory) =
        memory[address]

    operator fun plus(value: Int) = MemoryAddress(address + value)

    override fun toString() = address.toString()
}

interface InputProvider {
    fun getNextInput(): Int
    fun addValue(value: Int)
    fun hasNextInput(): Boolean
}

interface OutputRecorder {
    fun addValue(value: Int)
    fun getOutput(): List<Int>
}

open class ListOutputRecorder : OutputRecorder {
    private val output = mutableListOf<Int>()
    override fun addValue(value: Int) {
        output.add(value)
    }

    override fun getOutput() = output.toList()
}

object NoInputProvider : InputProvider {
    override fun getNextInput(): Int {
        throw NoSuchElementException("No input available")
    }

    override fun addValue(value: Int) {
        throw NotImplementedError("Not supported by this input provider")
    }

    override fun hasNextInput() = false
}

class ListInputProvider(list: List<Int> = emptyList()) : InputProvider {
    private var currentInput = 0
    private var source = list

    override fun getNextInput() = source[currentInput++]
    override fun addValue(value: Int) {
        source = source + listOf(value)
    }

    override fun hasNextInput() = source.size > currentInput
}

fun List<Int>.asInputProvider() = ListInputProvider(this)

class IntcodeProgramInterpreter(
    val memory: Memory,
    val overrides: Map<Int, Int> = emptyMap(),
    val inputs: InputProvider = NoInputProvider,
    val outputRecorder: OutputRecorder = ListOutputRecorder()
) {
    var executionStatus: ExecutionStatus? = null

    fun evaluate(): ExecutionStatus {
        return executionStatus.run {
            when {
                this == null -> {
                    executionStatus = ExecutionContext(
                        memory.toMutableList().apply {
                            overrides.entries.forEach {
                                this[it.key] = it.value
                            }
                        }.toList(),
                        inputs,
                        outputRecorder,
                        MemoryAddress(0)
                    ).evaluate()

                    executionStatus!!
                }
                executionState == ExecutionState.COMPLETED -> {
                    throw IllegalStateException("already completed")
                }
                else -> {
                    executionStatus = executionContext.evaluate()
                    executionStatus!!
                }
            }
        }
    }
}

enum class ExecutionState {
    COMPLETED,
    HALTED
}

data class ExecutionStatus(
    val executionState: ExecutionState,
    val executionContext: ExecutionContext
)

data class ExecutionContext(
    val memory: Memory,
    val inputs: InputProvider,
    val output: OutputRecorder,
    val instructionPointer: MemoryAddress?
) {
    fun evaluate(): ExecutionStatus {
        return if (instructionPointer == null) {
            ExecutionStatus(
                ExecutionState.COMPLETED,
                this
            )
        } else {
            val instruction = IntcodeInstruction.init(instructionPointer.resolve(memory))

            if (instruction.canExecute(this)) {
                instruction.execute(this).evaluate()
            } else {
                ExecutionStatus(
                    ExecutionState.HALTED,
                    this
                )
            }

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
    open fun canExecute(executionContext: ExecutionContext): Boolean {
        return true
    }

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
                this[output.value] = executionContext.inputs.getNextInput()
            }.toList(),
            instructionPointer = executionContext.instructionPointer!! + instructionLength
        )
    }

    override fun canExecute(executionContext: ExecutionContext): Boolean {
        return executionContext.inputs.hasNextInput()
    }
}

class Output : IntcodeInstruction(1) {
    override fun execute(executionContext: ExecutionContext): ExecutionContext {
        val (input) = executionContext.getParameters()

        executionContext.output.addValue(executionContext.read(input))

        return executionContext.copy(
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