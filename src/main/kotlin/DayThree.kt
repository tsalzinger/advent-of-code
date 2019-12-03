package me.salzinger

enum class Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT
}

data class Movement(
    val direction: Direction,
    val distance: Int
) {
    companion object {
        fun fromString(input: String): Movement {
            val direction = when (input[0]) {
                'U' -> Direction.UP
                'R' -> Direction.RIGHT
                'D' -> Direction.DOWN
                'L' -> Direction.LEFT
                else -> throw RuntimeException("Unsupported direction")
            }

            return Movement(
                direction,
                input.drop(1).toInt()
            )
        }
    }

    override fun toString() = "${direction.name[0]}$distance"
}

fun String.toMovement() = Movement.fromString(this)

fun Point.move(movement: Movement) = when (movement.direction) {
    Direction.UP -> Point(x, y + movement.distance)
    Direction.DOWN -> Point(x, y - movement.distance)
    Direction.RIGHT -> Point(x + movement.distance, y)
    Direction.LEFT -> Point(x - movement.distance, y)
}

fun Point.getPointsTo(movement: Movement): List<Point> {
    return (1..(movement.distance)).map { distance ->
        when (movement.direction) {
            Direction.UP -> Point(x, y + distance)
            Direction.DOWN -> Point(x, y - distance)
            Direction.RIGHT -> Point(x + distance, y)
            Direction.LEFT -> Point(x - distance, y)
        }
    }
}

fun Point.getPointsTo(movement: String) = getPointsTo(movement.toMovement())

class Wire(private val points: List<Point>) : List<Point> by points {
    companion object {
        fun fromMovements(movements: List<Movement>, startingPoint: Point = Point(0, 0)): Wire {
            val points = mutableListOf<Point>()
            var currentPoint = startingPoint

            movements.forEach {
                points.addAll(currentPoint.getPointsTo(it))
                currentPoint = points.last()
            }

            return Wire(points)
        }
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Wire -> points.equals(other.points)
            else -> false
        }
    }

    override fun hashCode(): Int {
        return points.hashCode()
    }

    override fun toString(): String {
        return points.toString()
    }
}

class Grid(
    wires: List<Wire>
) {
    val grid: Map<Point, Set<Wire>>
    val intersections: List<Point>

    init {
        val grid = mutableMapOf<Point, Set<Wire>>()
        wires.forEach { wire ->
            wire.forEach { point ->
                grid.merge(point, setOf(wire)) { a, b -> a.union(b) }
            }
        }
        this.grid = grid.toMap()
        intersections = grid.filter { (_, value) -> value.size > 1 }.keys.toList()
    }

    fun getClosestOverlapDistanceToCenter(): Int {
        return intersections.sortedBy { it.manhattenDistance(Point(0, 0)) }.first().manhattenDistance(Point(0, 0))
    }
}

fun List<Wire>.toGrid() = Grid(this)

fun String.convertToMovements(): List<Movement> =
    split(",").map(Movement.Companion::fromString)

fun List<String>.convertToMovements(): List<List<Movement>> =
    map(String::convertToMovements)

fun List<Movement>.toWire(): Wire {
    return Wire.fromMovements(this, Point(0, 0))
}

fun main() {
    solvePuzzle5()
}

private fun solvePuzzle5() {
    5.solve {
        convertToMovements()
            .map { it.toWire() }
            .toGrid()
            .getClosestOverlapDistanceToCenter()
            .toString()
    }
}