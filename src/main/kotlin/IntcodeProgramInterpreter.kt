package me.salzinger

import java.math.BigInteger

inline class MemoryAddress(val address: Int) {
    fun resolve(memory: Memory) =
        memory[address] ?: BigInteger.ZERO

    operator fun plus(value: Int) = MemoryAddress(address + value)

    override fun toString() = address.toString()
}

interface InputProvider {
    fun getNextInput(): BigInteger
    fun addValue(value: BigInteger)
    fun hasNextInput(): Boolean
}

interface OutputRecorder {
    fun addValue(value: BigInteger)
    fun getOutput(): List<BigInteger>
}

open class ListOutputRecorder : OutputRecorder {
    private val output = mutableListOf<BigInteger>()
    override fun addValue(value: BigInteger) {
        output.add(value)
    }

    override fun getOutput() = output.toList()
}

object NoInputProvider : InputProvider {
    override fun getNextInput(): BigInteger {
        throw NoSuchElementException("No input available")
    }

    override fun addValue(value: BigInteger) {
        throw NotImplementedError("Not supported by this input provider")
    }

    override fun hasNextInput() = false
}

class ListInputProvider(list: List<BigInteger> = emptyList()) : InputProvider {
    private var currentInput = 0
    private var source = list

    override fun getNextInput() = source[currentInput++]
    override fun addValue(value: BigInteger) {
        source = source + listOf(value)
    }

    override fun hasNextInput() = source.size > currentInput
}

fun List<BigInteger>.asInputProvider() = ListInputProvider(this)
fun List<Int>.toBigIntegerList() = this.map(Int::toBigInteger)

class IntcodeProgramInterpreter(
    val memory: Memory,
    val overrides: Map<Int, BigInteger> = emptyMap(),
    val inputs: InputProvider = NoInputProvider,
    val outputRecorder: OutputRecorder = ListOutputRecorder()
) {
    var executionStatus: ExecutionStatus? = null

    fun evaluate(): ExecutionStatus {
        return executionStatus.run {
            when {
                this == null -> {
                    executionStatus = ExecutionContext(
                        memory.toMutableMap().apply {
                            overrides.entries.forEach {
                                this[it.key] = it.value
                            }
                        }.toMap(),
                        inputs,
                        outputRecorder,
                        MemoryAddress(0),
                        relativeBase = 0
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
    val instructionPointer: MemoryAddress?,
    val relativeBase: Int
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
    val RELATIVE = ParameterMode(2)
}

inline class ParameterMode(val mode: Int) {
    fun resolve(memoryAddress: MemoryAddress, executionContext: ExecutionContext): MemoryAddress {
        return when (ParameterMode(mode)) {
            ParameterModes.POSITION -> {
                MemoryAddress(executionContext.memory.getValue(memoryAddress.address).toInt())
            }
            ParameterModes.IMMEDIATE -> memoryAddress
            ParameterModes.RELATIVE -> {
                MemoryAddress(executionContext.memory.getValue(memoryAddress.address).toInt() + executionContext.relativeBase)
            }
            else -> throw RuntimeException("Unsupported parameter mode $mode")
        }
    }
}

data class Parameter(
    val baseAddress: MemoryAddress,
    val parameterMode: ParameterMode,
    val executionContext: ExecutionContext
) {
    fun read() =
        executionContext.memory.getValue(parameterMode.resolve(baseAddress, executionContext).address)

    fun write(value: BigInteger): Memory {
        return executionContext.memory.toMutableMap().apply {
            this[parameterMode.resolve(baseAddress, executionContext).address] = value
        }.toMap()
    }
}

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

    fun ExecutionContext.resolveParameter(parameterIndex: Int): Parameter {
        val parameterModes = getParameterModes()

        return Parameter(
            MemoryAddress(instructionPointer!!.address + parameterIndex + 1),
            parameterModes.getOrElse(parameterIndex) { ParameterModes.POSITION },
            this
        )
    }

    private fun ExecutionContext.getParameterModes(): List<ParameterMode> {
        return memory[instructionPointer!!.address].toString().dropLast(OPCODE_RANGE_LENGTH).reversed()
            .map { ParameterMode(it.toString().toInt()) }
    }

    companion object {
        private const val OPCODE_RANGE_LENGTH = 2
        fun init(value: BigInteger): IntcodeInstruction {
            return when (val opcode: Opcode = value.toString().takeLast(OPCODE_RANGE_LENGTH).toInt()) {
                1 -> Add()
                2 -> Multiply()
                3 -> Input()
                4 -> Output()
                5 -> JumpIfTrue()
                6 -> JumpIfFalse()
                7 -> LessThen()
                8 -> Equals()
                9 -> RelativeBaseOffset()
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
            memory = p3.write(p1.read() + p2.read()),
            instructionPointer = executionContext.instructionPointer!! + instructionLength
        )
    }
}

class Multiply : IntcodeInstruction(3) {
    override fun execute(executionContext: ExecutionContext): ExecutionContext {
        val (p1, p2, p3) = executionContext.getParameters()

        return executionContext.copy(
            memory = p3.write(p1.read() * p2.read()),
            instructionPointer = executionContext.instructionPointer!! + instructionLength
        )
    }
}

class Input : IntcodeInstruction(1) {
    override fun execute(executionContext: ExecutionContext): ExecutionContext {
        val (output) = executionContext.getParameters()

        return executionContext.copy(
            memory = output.write(executionContext.inputs.getNextInput()),
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

        executionContext.output.addValue(input.read())

        return executionContext.copy(
            instructionPointer = executionContext.instructionPointer!! + instructionLength
        )
    }
}

class JumpIfTrue : IntcodeInstruction(2) {
    override fun execute(executionContext: ExecutionContext): ExecutionContext {
        val (compareTo, jumpTo) = executionContext.getParameters()

        return executionContext.copy(
            instructionPointer = if (compareTo.read() != BigInteger.ZERO) {
                MemoryAddress(jumpTo.read().toInt())
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
            instructionPointer = if (compareTo.read() == BigInteger.ZERO) {
                MemoryAddress(jumpTo.read().toInt())
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
            memory = output.write(
                if (p1.read() < p2.read()) {
                    BigInteger.ONE
                } else {
                    BigInteger.ZERO
                }
            ),
            instructionPointer = executionContext.instructionPointer!! + instructionLength
        )
    }
}

class Equals : IntcodeInstruction(3) {
    override fun execute(executionContext: ExecutionContext): ExecutionContext {
        val (p1, p2, output) = executionContext.getParameters()

        return executionContext.copy(
            memory = output.write(
                if (p1.read() == p2.read()) {
                    BigInteger.ONE
                } else {
                    BigInteger.ZERO
                }
            ),
            instructionPointer = executionContext.instructionPointer!! + instructionLength
        )
    }
}

class RelativeBaseOffset : IntcodeInstruction(1) {
    override fun execute(executionContext: ExecutionContext): ExecutionContext {
        val (p1) = executionContext.getParameters()

        return executionContext.copy(
            relativeBase = executionContext.relativeBase + p1.read().toInt(),
            instructionPointer = executionContext.instructionPointer!! + instructionLength
        )
    }
}

class Return : IntcodeInstruction(0) {
    override fun execute(executionContext: ExecutionContext) =
        executionContext.copy(instructionPointer = null)
}