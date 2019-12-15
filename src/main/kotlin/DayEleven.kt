package me.salzinger

import java.math.BigInteger
import kotlin.math.max
import kotlin.math.min

fun Point.move(direction: Direction, distance: Int = 1) = when (direction) {
    Direction.UP -> up(distance)
    Direction.RIGHT -> right(distance)
    Direction.DOWN -> down(distance)
    Direction.LEFT -> left(distance)
}

fun Direction.turnLeft() = when (this) {
    Direction.UP -> Direction.LEFT
    Direction.RIGHT -> Direction.UP
    Direction.DOWN -> Direction.RIGHT
    Direction.LEFT -> Direction.DOWN
}

fun Direction.turnRight() = turnLeft().turnLeft().turnLeft()

enum class PanelColor {
    BLACK,
    WHITE
}

fun <T> Map<Point, T>.print(transform: (value: T?, position: Point) -> String): String {
    var minX = Int.MAX_VALUE
    var minY = Int.MAX_VALUE
    var maxX = Int.MIN_VALUE
    var maxY = Int.MIN_VALUE

    for (key in this.keys) {
        minX = min(minX, key.x)
        minY = min(minY, key.y)

        maxX = max(maxX, key.x)
        maxY = max(maxY, key.y)
    }

    return (maxY downTo minY).map { y ->
        (minX..maxX).map { x ->
            val point = Point(x, y)
            transform.invoke(this[point], point)
        }.joinToString("")
    }.joinToString("\n")
}

private fun paintHull(panels: Map<Point, PanelColor>, memory: Memory): Map<Point, PanelColor> {
    var currentPosition = Point(0, 0)
    var currentDirection = Direction.UP
    val currentPanels = panels.toMutableMap().withDefault { PanelColor.BLACK }

    IntcodeProgramInterpreter(
        memory,
        inputs = object : InputProvider {
            override fun getNextInput(): BigInteger {
                return when (currentPanels.getValue(currentPosition)) {
                    PanelColor.BLACK -> BigInteger.ZERO
                    PanelColor.WHITE -> BigInteger.ONE
                }
            }

            override fun addValue(value: BigInteger) {
                TODO("not implemented")
            }

            override fun hasNextInput() = true
        },
        outputRecorder = object : OutputRecorder {
            var currentOutput = 0

            override fun addValue(value: BigInteger) {
                if (currentOutput == 0) {
                    currentPanels[currentPosition] = if (value == BigInteger.ZERO) {
                        PanelColor.BLACK
                    } else {
                        PanelColor.WHITE
                    }
                } else {
                    currentDirection = if (value == BigInteger.ZERO) {
                        currentDirection.turnLeft()
                    } else {
                        currentDirection.turnRight()
                    }
                    currentPosition = currentPosition.move(currentDirection)
                }
                currentOutput = (currentOutput + 1) % 2
            }

            override fun getOutput(): List<BigInteger> {
                TODO("not implemented")
            }
        }
    ).evaluate()
        .run {
            assert(this.executionState == ExecutionState.COMPLETED)
        }

    return currentPanels.toMap()
}

fun main() {
    21.solve {
        first()
            .convertIntcodeInput()
            .run {
                paintHull(emptyMap(), this)
                    .size
                    .toString()
            }
    }

    22.solve {
        first()
            .convertIntcodeInput()
            .run {
                paintHull(mapOf(Point(0, 0) to PanelColor.WHITE), this)
                    .print { value, _ ->
                        when (value) {
                            PanelColor.BLACK, null -> " "
                            PanelColor.WHITE -> "█"
                        }
                    }
            }
    }
}