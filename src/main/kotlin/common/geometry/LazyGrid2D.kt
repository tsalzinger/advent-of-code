package me.salzinger.common.geometry

import me.salzinger.common.Grid2D.Cell
import me.salzinger.common.Grid2D.Coordinate

class LazyGrid2D<T>(
    val valuesProvider: (Coordinate) -> T,
    val boundaryProvider: () -> Boundary,
    private val neighborProvider: (Coordinate.() -> Set<Coordinate>) = Coordinate.NeighborModes.CROSS,
) :
    Iterable<Cell<T>> {
    val rows: Int
        get() = boundaryProvider().rows
    val columns: Int
        get() = boundaryProvider().columns

    data class Boundary(
        val minRow: Int,
        val maxRow: Int,
        val minColumn: Int,
        val maxColumn: Int,
    ) {
        val rows = maxRow - minRow + 1
        val columns = maxColumn - minColumn + 1
        operator fun contains(coordinate: Coordinate): Boolean {
            return coordinate.row in (minRow..maxRow) &&
                    coordinate.column in (minColumn..maxColumn)
        }
    }

    fun getCellAt(coordinate: Coordinate): Cell<T> {
        return getCellAtOrNull(coordinate) ?: throw RuntimeException("Coordinate is outside of grid boundary")
    }

    fun getCellAtOrNull(coordinate: Coordinate): Cell<T>? {
        return if (coordinate in this) {
            Cell(coordinate, valuesProvider(coordinate))
        } else {
            null
        }
    }

    operator fun contains(coordinate: Coordinate): Boolean {
        return coordinate in boundaryProvider()
    }

    fun getNeighborsOf(coordinate: Coordinate): Set<Cell<T>> {
        return coordinate
            .getNeighbors(neighborProvider)
            .mapNotNull {
                getCellAtOrNull(it)
            }
            .toSet()
    }

    fun getNeighborsOf(cell: Cell<T>): Set<Cell<T>> {
        return getNeighborsOf(cell.coordinate)
    }

    override fun iterator(): Iterator<Cell<T>> {
        val boundary = boundaryProvider()

        return (boundary.minRow..boundary.maxRow)
            .asSequence()
            .flatMap { row ->
                (boundary.minColumn..boundary.maxColumn)
                    .asSequence()
                    .map { column ->
                        getCellAt(Coordinate(row, column))
                    }
            }
            .iterator()
    }
}

fun <T> LazyGrid2D<T>.toConsoleString(transform: ((Cell<T>) -> String)? = null): String {
    return chunked(columns).joinToString("\n") { row ->
        row.joinToString(separator = "", transform = transform)
    }
}
