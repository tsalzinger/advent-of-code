package me.salzinger

import java.math.BigInteger

class ConsoleInputProvider(
    private val inputTransformer: (String) -> BigInteger
) : ListInputProvider() {
    lateinit var lastInput: BigInteger

    override fun getNextInput(): BigInteger {
        readNextInput()
        return super.getNextInput()
    }

    override fun hasNextInput() = true

    fun readNextInput() {
        try {
            print("Input: ")
            readLine()!!
                .let(inputTransformer::invoke)
                .also(::addValue)
                .run {
                    lastInput = this
                }
        } catch (exception: InvalidInputException) {
            println("Invalid input")
            readNextInput()
        }
    }

    class InvalidInputException : Exception()
}

object DroidMovement {
    val NORTH: DroidMovementCode = BigInteger.ONE
    val EAST: DroidMovementCode = 4.toBigInteger()
    val SOUTH: DroidMovementCode = BigInteger.TWO
    val WEST: DroidMovementCode = 3.toBigInteger()

    val ALL = listOf(NORTH, EAST, SOUTH, WEST)

    fun invert(movementCode: DroidMovementCode) = when (movementCode) {
        NORTH -> SOUTH
        EAST -> WEST
        SOUTH -> NORTH
        WEST -> EAST
        else -> throw RuntimeException("Unkown movment code $movementCode")
    }
}

object DroidStatusCodes {
    val WALL_HIT: DroidStatusCode = BigInteger.ZERO
    val CONFIRMED: DroidStatusCode = BigInteger.ONE
    val OXYGEN_SYSTEM: DroidStatusCode = BigInteger.TWO
}

typealias DroidMovementCode = BigInteger
typealias DroidStatusCode = BigInteger

class Environment(
    val repairDroid: RepairDroid = RepairDroid()
) {
    val revealed: Boolean
        get() {
            return map.filterValues { it.droidStatusCode != DroidStatusCodes.WALL_HIT }.map { it.value.neighbors.size }.min()!! == 4
        }
    val statusAtCurrentPosition
        get() = map.getValue(repairDroid.position)

    val map = mutableMapOf(
        repairDroid.position to EnvironmentStatus(
            repairDroid.position,
            DroidStatusCodes.CONFIRMED,
            0
        )
    )

    fun recordStatusReport(
        movementCommand: DroidMovementCode,
        statusCode: DroidStatusCode
    ) {
        val previousPosition = repairDroid.position
        val previousStatus = map.getValue(previousPosition)
        val newPosition = repairDroid.move(movementCommand)
        map.putIfAbsent(newPosition, EnvironmentStatus(newPosition, statusCode, previousStatus.distance + 1))
        map.getValue(newPosition).run {
            addNeighbor(previousStatus)
        }

        if (statusCode == DroidStatusCodes.WALL_HIT) {
            // droid didn't actually move
            repairDroid.revert(movementCommand)
        }
    }

    override fun toString(): String {
        return map.print { value, position ->
            val statusCode = value?.droidStatusCode
            if (position == repairDroid.position) {
                if (statusCode == DroidStatusCodes.OXYGEN_SYSTEM) {
                    "⨂"
                } else {
                    "⨀"
                }
            } else {
                when (statusCode) {
                    null -> " "
                    DroidStatusCodes.WALL_HIT -> "█"
                    DroidStatusCodes.CONFIRMED -> "•"
                    DroidStatusCodes.OXYGEN_SYSTEM -> "⨁"
                    else -> "?"
                }
            }
        }
    }

    class EnvironmentStatus(
        val position: Point,
        val droidStatusCode: DroidStatusCode,
        var distance: Int
    ) {
        val neighbors = mutableMapOf<Point, EnvironmentStatus>()
        fun addNeighbor(environmentStatus: EnvironmentStatus) {
            if ((environmentStatus.distance + 1) < distance) {
                updateDistance(environmentStatus.distance + 1)
            }

            neighbors.putIfAbsent(environmentStatus.position, environmentStatus)
            environmentStatus.neighbors.putIfAbsent(position, this)
        }

        private fun updateDistance(distance: Int) {
            if (distance < this.distance) {
                val oldDistance = this.distance
                val diff = oldDistance - distance
                this.distance = distance
                if (droidStatusCode != DroidStatusCodes.WALL_HIT) {
                    neighbors.values.forEach {
                        if (it.distance > oldDistance) {
                            it.updateDistance(it.distance - diff)
                        }
                    }
                }
            }
        }

        fun hasNeighborAt(droidMovementCode: DroidMovementCode): Boolean {
            return when (droidMovementCode) {
                DroidMovement.NORTH -> neighbors.containsKey(position.up(1))
                DroidMovement.EAST -> neighbors.containsKey(position.right(1))
                DroidMovement.SOUTH -> neighbors.containsKey(position.down(1))
                DroidMovement.WEST -> neighbors.containsKey(position.left(1))
                else -> throw RuntimeException("Unsupported movement code $droidMovementCode")
            }
        }

        fun getDirectionOfUnknownNeighbor(): DroidMovementCode? {
            return DroidMovement.ALL
                .find { !hasNeighborAt(it) }
        }
    }
}

class RepairDroid {
    var position = Point.ZERO
    lateinit var lastMovementCommand: BigInteger

    fun move(movementCommand: BigInteger): Point {
        position = when (movementCommand) {
            DroidMovement.NORTH -> position.up(1)
            DroidMovement.EAST -> position.right(1)
            DroidMovement.SOUTH -> position.down(1)
            DroidMovement.WEST -> position.left(1)
            else -> throw RuntimeException("Invalid movement command $movementCommand")
        }
        lastMovementCommand = movementCommand
        return position
    }

    fun revert(movementCommand: BigInteger) {
        position = when (movementCommand) {
            DroidMovement.NORTH -> position.down(1)
            DroidMovement.EAST -> position.left(1)
            DroidMovement.SOUTH -> position.up(1)
            DroidMovement.WEST -> position.right(1)
            else -> throw RuntimeException("Invalid movement command $movementCommand")
        }
    }
}

class AutoReveal(private val environment: Environment) : ListInputProvider() {
    var lastPosition: Point? = null
    var commands = mutableListOf<DroidMovementCode>()
    var lastCommand: DroidMovementCode? = null

    override fun getNextInput(): BigInteger {
        if (environment.repairDroid.position != lastPosition) {
            // we are at a new position
            lastPosition = environment.repairDroid.position

            return tryNext()
        } else {
            // we hit a wall drop the last command
            commands.removeAt(commands.size - 1)
            return tryNext()
        }
    }

    private fun tryNext(): DroidMovementCode {
        val directionOfUnknownNeighbor = environment
            .statusAtCurrentPosition
            .getDirectionOfUnknownNeighbor()
            ?.also {
                commands.add(it)
            }

        return (directionOfUnknownNeighbor ?: backtrack())
            .also {
                addValue(it)
                lastCommand = it
            }
    }

    private fun backtrack(): DroidMovementCode {
        return DroidMovement.invert(commands.removeAt(commands.size - 1))
    }

    override fun hasNextInput(): Boolean {
        return inputs.isEmpty() || !commands.isEmpty()//!environment.revealed
    }
}

fun main() {
    29.solve {
        first()
            .convertIntcodeInput()
            .run {
                val environment = Environment()
                val autoReveal = AutoReveal(environment)
                val inputProvider = ConsoleInputProvider {
                    when (it) {
                        "8" -> DroidMovement.NORTH
                        "6" -> DroidMovement.EAST
                        "2" -> DroidMovement.SOUTH
                        "4" -> DroidMovement.WEST
                        else -> throw ConsoleInputProvider.InvalidInputException()
                    }
                }
                val outputRecorder = object : ListOutputRecorder() {
                    override fun addValue(value: BigInteger) {
                        super.addValue(value)
                        environment.recordStatusReport(
                            movementCommand = autoReveal.lastCommand!!,
                            statusCode = value
                        )
                    }
                }

                IntcodeProgramInterpreter(
                    memory = this,
                    inputs = autoReveal,//inputProvider,
                    outputRecorder = outputRecorder
                ).evaluate()

                println(environment)

                environment
                    .map
                    .values
                    .first { it.droidStatusCode == DroidStatusCodes.OXYGEN_SYSTEM }
                    .distance
            }
            .toString()
    }
}