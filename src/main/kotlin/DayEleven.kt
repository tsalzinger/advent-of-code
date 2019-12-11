package me.salzinger

import java.math.BigInteger

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

fun main() {
    21.solve {
        first()
            .convertIntcodeInput()
            .run {
                var currentPosition = Point(0, 0)
                var currentDirection = Direction.UP
                val paintedPanels = mutableSetOf<Point>()
                val map = mutableMapOf<Point, PanelColor>().withDefault { PanelColor.BLACK }

                IntcodeProgramInterpreter(
                    this,
                    inputs = object : InputProvider {
                        override fun getNextInput(): BigInteger {
                            return when (map.getValue(currentPosition)) {
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
                                paintedPanels.add(currentPosition)
                                map[currentPosition] = if (value == BigInteger.ZERO) {
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

                paintedPanels.size.toString()
            }
    }
}