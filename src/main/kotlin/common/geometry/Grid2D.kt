package me.salzinger.common

import me.salzinger.common.Grid2D.Coordinate
import me.salzinger.common.geometry.Direction
import kotlin.math.abs

typealias NeighborProvider = Coordinate.() -> Set<Coordinate>

class Grid2D<T>(
    values: List<List<T>>,
    private val neighborProvider: NeighborProvider = Coordinate.NeighborModes.CROSS,
) : Iterable<Grid2D.Cell<T>> {
    val rows = values.count()
    val columns = values.firstOrNull()?.count() ?: 0
    private val cells =
        values
            .flatMapIndexed { rowIndex, rowValues ->
                rowValues.mapIndexed { columnIndex, value ->
                    val coordinate =
                        Coordinate(
                            rowIndex,
                            columnIndex,
                        )
                    coordinate to Cell(coordinate, value)
                }
            }.toMap()

    val rowsRange: IntRange
        get() = 0..<rows

    val columnsRange: IntRange
        get() = 0..<columns

    val lastRow = rows - 1
    val lastColumn = columns - 1

    data class Cell<T>(
        val coordinate: Coordinate,
        val value: T,
    )

    fun <R> transformValues(transformer: (Cell<T>) -> R): Grid2D<R> =
        Grid2D(
            values = map(transformer).chunked(columns),
            neighborProvider = neighborProvider,
        )

    data class Coordinate(
        val row: Int,
        val column: Int,
    ) {
        fun up() = copy(row = row - 1)

        fun right() = copy(column = column + 1)

        fun down() = copy(row = row + 1)

        fun left() = copy(column = column - 1)

        fun rightDown() = right().down()

        fun downLeft() = down().left()

        fun leftUp() = left().up()

        fun upRight() = up().right()

        fun withColumn(column: Int) = copy(column = column)

        fun withRow(row: Int) = copy(row = row)

        fun getNeighbors(neighborProvider: Coordinate.() -> Set<Coordinate>): Set<Coordinate> = neighborProvider()

        fun getManhattenDistanceTo(coordinate: Coordinate) = abs(column - coordinate.column) + abs(row - coordinate.row)

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
                    upRight(),
                    right(),
                    rightDown(),
                    down(),
                    downLeft(),
                    left(),
                    leftUp(),
                )
            }
        }

        companion object {
            val ZERO = Coordinate(0, 0)
        }
    }

    fun getCellAt(coordinate: Coordinate): Cell<T> = cells.getValue(coordinate)

    fun getCellAtOrNull(coordinate: Coordinate): Cell<T>? = cells[coordinate]

    operator fun get(coordinate: Coordinate): Cell<T> = getCellAt(coordinate)

    operator fun contains(coordinate: Coordinate): Boolean = coordinate in cells

    fun getNeighborsOf(coordinate: Coordinate): Set<Cell<T>> =
        coordinate
            .getNeighbors(neighborProvider)
            .mapNotNull {
                cells[it]
            }.toSet()

    fun <S : T> getNeighborsOf(cell: Cell<S>): Set<Cell<T>> = getNeighborsOf(cell.coordinate)

    override fun iterator(): Iterator<Cell<T>> = cells.values.iterator()

    fun <R> mapRows(transform: List<Cell<T>>.(rowIndex: Int) -> R): List<R> =
        chunked(columns).mapIndexed { rowIndex, row -> row.transform(rowIndex) }

    fun <R> mapColumns(transform: List<Cell<T>>.(rowIndex: Int) -> R): List<R> =
        (0..<columns)
            .map { columnIndex ->
                (0..<rows).map { rowIndex ->
                    getCellAt(Coordinate(row = rowIndex, column = columnIndex))
                }
            }.mapIndexed { columnIndex, column -> column.transform(columnIndex) }
}

fun Coordinate.nextIn(direction: Direction): Coordinate =
    when (direction) {
        Direction.UP -> up()
        Direction.RIGHT -> right()
        Direction.DOWN -> down()
        Direction.LEFT -> left()
        Direction.RIGHT_DOWN -> rightDown()
        Direction.DOWN_LEFT -> downLeft()
        Direction.LEFT_UP -> leftUp()
        Direction.UP_RIGHT -> upRight()
    }

fun <T> Grid2D<T>.toConsoleString(transform: ((Grid2D.Cell<T>) -> String)? = null): String =
    chunked(columns).joinToString("\n") { row ->
        row.joinToString(separator = "", transform = transform)
    }
