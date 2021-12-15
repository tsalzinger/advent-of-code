package me.salzinger

import java.io.File
import kotlin.math.abs
import kotlin.math.sqrt

fun Double.floor() = kotlin.math.floor(this)

fun <T> List<T>.countOccurrences(value: T) = this.count { it == value }

fun <T> List<T>.toPairs(): List<Pair<T, T>> = chunked(2).map { Pair(it[0], it[1]) }

enum class FileType(val extension: String) {
    IN("in"),
    OUT("out")
}

fun getFile(level: Int, part: Int?, fileType: FileType) =
    File("src/main/resources/puzzle-$level${part?.run { "-$this" } ?: ""}.${fileType.extension}")

fun getFile(level: Int, part: String, fileType: FileType) =
    File("src/main/resources/puzzle-$level-$part.${fileType.extension}")

fun getNextInput(puzzle: Int, part: Int? = null) =
    getFile(puzzle, part, FileType.IN)
        .readLines()
        .map(String::trim)
        .filter(String::isNotBlank)

fun getNextInput(puzzle: Int, part: String) =
    getFile(puzzle, part, FileType.IN)
        .readLines()
        .map(String::trim)
        .filter(String::isNotBlank)

fun Int.solveExample(expectedSolution: String, solver: List<String>.() -> String) {
    getNextInput(this, 0)
        .solver()
        .also { solution ->
            check(solution == expectedSolution) {
                "Expected $expectedSolution but got $solution"
            }
        }
}

fun Int.solveExample(exampleNumber: Int, expectedSolution: String, solver: List<String>.() -> String) {
    getNextInput(this, "example-$exampleNumber")
        .solver()
        .also { solution ->
            check(solution == expectedSolution) {
                "Expected $expectedSolution but got $solution"
            }
        }
}

fun Int.solve(part: Int, solver: List<String>.() -> String) {
    getNextInput(this, part)
        .solver()
        .writePuzzleSolution(this, part)
}

fun String.toIntList(vararg delimiters: String = arrayOf(",")): List<Int> {
    return split(delimiters = delimiters).map { it.toInt() }
}

fun String.toIntList(regex: Regex): List<Int> {
    return split(regex = regex).map { it.toInt() }
}

fun Int.solve(solver: List<String>.() -> String) {
    getNextInput(this)
        .solver()
        .writePuzzleSolution(this)
}

fun <T> List<T>.writePuzzleSolution(level: Int, part: Int?) =
    getFile(level, part, FileType.OUT).writeText(joinToString("\n"))

fun String.writePuzzleSolution(level: Int, part: Int? = null) = listOf(this).writePuzzleSolution(level, part)

data class Point(
    val x: Int,
    val y: Int
) {
    fun manhattenDistance(anotherPoint: Point) = abs(x - anotherPoint.x) + abs(y - anotherPoint.y)

    operator fun plus(vector: Vector) = Point(x + vector.dx, y + vector.dy)

    fun vectorTo(other: Point) = Vector(other.x - x, other.y - y)

    companion object {
        val ZERO = Point(0, 0)
    }
}

data class Vector(
    val dx: Int,
    val dy: Int
) {
    val length: Double by lazy {
        sqrt((dx.toDouble() * dx) + (dy.toDouble() * dy))
    }
    val angle: Double by lazy {
        val radiants = dx.toDouble() / length

        if (dx >= 0 && dy > 0) {
            2 - radiants
        } else if (dx < 0 && dy >= 0) {
            2 - radiants
        } else if (dx < 0 && dy < 0) {
            4 + radiants
        } else {
            radiants
        }
    }

    override fun toString(): String {
        return "($dx,$dy|${angle})"
    }

    fun scale(factor: Int) = Vector(dx * factor, dy * factor)

    operator fun times(factor: Int) = scale(factor)
    operator fun div(factor: Int) = Vector(dx / factor, dy / factor)

    fun minimize() = this / ggt(dx, dy)
}

fun ggt(x: Int = 0, y: Int = 0): Int {
    var a: Int = abs(x)
    var b: Int = abs(y)

    if (a == 0) {
        return b
    }

    while (b != 0) {
        if (a > b) {
            a -= b
        } else {
            b -= a
        }
    }

    return a
}

fun ByteArray.toInt(): Int {
    return joinToString("").toInt(2)
}

class Grid<T>(
    values: List<List<T>>,
    private val neighborProvider: (Coordinate.() -> Set<Coordinate>) = Coordinate.NeighborModes.CROSS
) :
    Iterable<Grid.Cell<T>> {
    val rows = values.count()
    val columns = values.firstOrNull()?.count() ?: 0
    private val cells = values.flatMapIndexed { rowIndex, rowValues ->
        rowValues.mapIndexed { columnIndex, value ->
            val coordinate = if (neighborProvider != null) {
                Coordinate(
                    rowIndex,
                    columnIndex,
                )
            } else {
                Coordinate(rowIndex, columnIndex)
            }
            coordinate to Cell(coordinate, value)
        }
    }.toMap()

    data class Cell<T>(val coordinate: Coordinate, val value: T)

    fun transformValues(transformer: (Grid.Cell<T>) -> T): Grid<T> {
        return Grid(
            values = map(transformer).chunked(columns),
            neighborProvider = neighborProvider,
        )
    }

    data class Coordinate(
        val row: Int,
        val column: Int,
    ) {
        fun up() = copy(row = row - 1)
        fun right() = copy(column = column + 1)
        fun down() = copy(row = row + 1)
        fun left() = copy(column = column - 1)

        fun getNeighbors(neighborProvider: Coordinate.() -> Set<Coordinate>): Set<Coordinate> = neighborProvider()

        object NeighborModes {
            val CROSS: Coordinate.() -> Set<Coordinate> = {
                setOf(
                    up(),
                    right(),
                    down(),
                    left(),
                )
            }
            val RING: Coordinate.() -> Set<Coordinate> = {
                setOf(
                    up(),
                    up().right(),
                    right(),
                    right().down(),
                    down(),
                    down().left(),
                    left(),
                    left().up(),
                )
            }
        }
    }

    fun getCellAt(coordinate: Coordinate): Cell<T> {
        return cells.getValue(coordinate)
    }

    fun getNeighborsOf(coordinate: Coordinate): Set<Cell<T>> {
        return coordinate
            .getNeighbors(neighborProvider)
            .mapNotNull {
                cells[it]
            }
            .toSet()
    }

    fun getNeighborsOf(cell: Cell<T>): Set<Cell<T>> {
        return getNeighborsOf(cell.coordinate)
    }

    override fun iterator(): Iterator<Cell<T>> {
        return cells.values.iterator()
    }
}

fun <T> Grid<T>.toConsoleString(transform: ((Grid.Cell<T>) -> String)? = null): String {
    return chunked(columns).joinToString("\n") { row ->
        row.joinToString(separator = "", transform = transform)
    }
}
