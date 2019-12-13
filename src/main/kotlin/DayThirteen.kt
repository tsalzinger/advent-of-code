package me.salzinger

import java.math.BigInteger
import kotlin.math.sign

object ArcadeTile {
    const val EMPTY = 0
    const val WALL = 1
    const val BLOCK = 2
    const val PADDLE = 3
    const val BALL = 4
}

fun Map<Point, Int>.printArcadeGame() =
    print {
        when (it) {
            ArcadeTile.EMPTY -> " "
            ArcadeTile.WALL -> "█"
            ArcadeTile.BLOCK -> "X"
            ArcadeTile.PADDLE -> "▀"
            ArcadeTile.BALL -> "0"
            null -> "" // unassigned tile below scores
            else -> "Score: $it\n"
        }
    }

tailrec fun readJoystickInput(inputProvider: InputProvider) {
    print("Input: ")

    val input = when (readLine()) {
        "1" -> inputProvider.addValue(JoystickMovement.LEFT)
        "2" -> inputProvider.addValue(JoystickMovement.STOP)
        "3" -> inputProvider.addValue(JoystickMovement.RIGHT)
        "4" -> repeat(5) { inputProvider.addValue(JoystickMovement.LEFT) }
        "5" -> repeat(5) { inputProvider.addValue(JoystickMovement.STOP) }
        "6" -> repeat(5) { inputProvider.addValue(JoystickMovement.RIGHT) }
        "7" -> repeat(10) { inputProvider.addValue(JoystickMovement.LEFT) }
        "8" -> repeat(10) { inputProvider.addValue(JoystickMovement.STOP) }
        "9" -> repeat(10) { inputProvider.addValue(JoystickMovement.RIGHT) }
        else -> {
            println("Unknown Input")
            null
        }
    }

    if (input == null) {
        readJoystickInput(inputProvider)
    }
}

object JoystickMovement {
    val LEFT = -BigInteger.ONE
    val STOP = BigInteger.ZERO
    val RIGHT = BigInteger.ONE
}

typealias ArcadeField = Map<Point, Int>

fun ArcadeField.getBallPosition(): Point {
    return this.entries.first { it.value == ArcadeTile.BALL }.key
}

fun ArcadeField.getPaddlePosition(): Point {
    return this.entries.first { it.value == ArcadeTile.PADDLE }.key
}

fun List<BigInteger>.toArcadeField(): ArcadeField =
    chunked(3)
        .map { (x, y, t) ->
            Point(x.toInt(), -y.toInt()) to t.toInt()
        }
        .toMap()

class AutoSolver(private val outputRecorder: OutputRecorder) : InputProvider {

    override fun getNextInput(): BigInteger {
        return outputRecorder
            .getOutput()
            .toArcadeField().run {
                (getBallPosition().x - getPaddlePosition().x).sign
            }
            .toBigInteger()
    }

    override fun addValue(value: BigInteger) {
        TODO("not supported by this input recorder")
    }

    override fun hasNextInput() = true
}

class ManualSolver(private val outputRecorder: OutputRecorder) : InputProvider {
    val inputQueue = mutableListOf<BigInteger>()
    var currentInput = 0

    override fun getNextInput(): BigInteger {
        if (inputQueue.size <= currentInput) {
            outputRecorder.getOutput()
                .toArcadeField()
                .printArcadeGame()
                .run(::println)

            readJoystickInput(this)
        }

        return inputQueue[currentInput++]
    }

    override fun addValue(value: BigInteger) {
        inputQueue.add(value)
    }

    override fun hasNextInput() = true
}

fun main() {
    25.solve {
        convertIntcodeInput()
            .first()
            .run {
                IntcodeProgramInterpreter(
                    memory = this
                ).evaluate()
                    .run {
                        assert(executionState == ExecutionState.COMPLETED)
                        executionContext.output.getOutput()
                    }

            }
            .toArcadeField()
            .count { (_, value) -> value == ArcadeTile.BLOCK }
            .toString()
    }
    26.solve {
        convertIntcodeInput()
            .first()
            .run {
                val outputRecoder = ListOutputRecorder()
                IntcodeProgramInterpreter(
                    memory = this,
                    overrides = mapOf(0 to 2.toBigInteger()),
                    inputs = AutoSolver(outputRecoder),
                    outputRecorder = outputRecoder
                ).evaluate()
                    .run {
                        assert(executionState == ExecutionState.COMPLETED)
                        executionContext
                    }

            }
            .output
            .getOutput()
            .toArcadeField()
            .printArcadeGame()
            .also(::println)
    }
}