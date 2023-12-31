package me.salzinger.common

import kotlin.math.abs

class Grid2D<T>(
    values: List<List<T>>,
    private val neighborProvider: (Coordinate.() -> Set<Coordinate>) = Coordinate.NeighborModes.CROSS,
) :
    Iterable<Grid2D.Cell<T>> {
    val rows = values.count()
    val columns = values.firstOrNull()?.count() ?: 0
    private val cells = values.flatMapIndexed { rowIndex, rowValues ->
        rowValues.mapIndexed { columnIndex, value ->
            val coordinate = Coordinate(
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

    data class Cell<T>(val coordinate: Coordinate, val value: T)

    fun <R> transformValues(transformer: (Cell<T>) -> R): Grid2D<R> {
        return Grid2D(
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
        fun withColumn(column: Int) = copy(column = column)
        fun withRow(row: Int) = copy(row = row)

        fun getNeighbors(neighborProvider: Coordinate.() -> Set<Coordinate>): Set<Coordinate> = neighborProvider()

        fun getManhattenDistanceTo(coordinate: Coordinate) =
            abs(column - coordinate.column) + abs(row - coordinate.row)

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

    fun getCellAtOrNull(coordinate: Coordinate): Cell<T>? {
        return cells[coordinate]
    }

    operator fun get(coordinate: Coordinate): Cell<T> = getCellAt(coordinate)
    operator fun contains(coordinate: Coordinate): Boolean {
        return coordinate in cells
    }

    fun getNeighborsOf(coordinate: Coordinate): Set<Cell<T>> {
        return coordinate
            .getNeighbors(neighborProvider)
            .mapNotNull {
                cells[it]
            }
            .toSet()
    }

    fun <S : T> getNeighborsOf(cell: Cell<S>): Set<Cell<T>> {
        return getNeighborsOf(cell.coordinate)
    }

    override fun iterator(): Iterator<Cell<T>> {
        return cells.values.iterator()
    }
}

fun <T> Grid2D<T>.toConsoleString(transform: ((Grid2D.Cell<T>) -> String)? = null): String {
    return chunked(columns).joinToString("\n") { row ->
        row.joinToString(separator = "", transform = transform)
    }
}
