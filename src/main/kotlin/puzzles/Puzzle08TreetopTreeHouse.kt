package puzzles

import me.salzinger.common.Grid2D
import me.salzinger.common.streamInput
import me.salzinger.common.toConsoleString

object Puzzle08TreetopTreeHouse {
    private fun String.toIntList(): List<Int> {
        return map { it.digitToInt() }
    }

    fun <T> Grid2D<T>.countBorderCells(): Int {
        return when {
            rows == 1 -> columns
            columns == 1 -> rows
            else -> 2 * columns + 2 * rows - 4
        }
    }

    fun <T> Grid2D<T>.isBorderCell(cell: Grid2D.Cell<T>): Boolean {
        return isBorderCoordinate(cell.coordinate)
    }

    fun <T> Grid2D<T>.isBorderCoordinate(coordinate: Grid2D.Coordinate): Boolean {
        return coordinate.row.let { it == 0 || it == (rows - 1) } ||
                coordinate.column.let { it == 0 || it == (columns - 1) }
    }

    fun <T> Grid2D<T>.isInnerCell(cell: Grid2D.Cell<T>): Boolean {
        return !isBorderCell(cell)
    }

    enum class TreeVisibility {
        VISIBLE,
        INVISIBLE,
    }

    enum class ViewDirection {
        UP,
        RIGHT,
        DOWN,
        LEFT,
    }

    fun <T> Grid2D<T>.getNeighborOfCellInViewDirection(
        cell: Grid2D.Cell<T>,
        viewDirection: ViewDirection,
    ): Grid2D.Cell<T> {
        return getCellAt(cell.coordinate.getNeighborInViewDirection(viewDirection))
    }

    fun <T> Grid2D<T>.getNeighborsOfCellInViewDirection(
        cell: Grid2D.Cell<T>,
        viewDirection: ViewDirection,
    ): List<Grid2D.Cell<T>> {
        return buildList {
            var currentCell = cell
            while (!isBorderCell(currentCell)) {
                currentCell = getNeighborOfCellInViewDirection(currentCell, viewDirection)
                    .also(::add)
            }
        }
    }

    fun Grid2D.Coordinate.getNeighborInViewDirection(viewDirection: ViewDirection): Grid2D.Coordinate {
        return when (viewDirection) {
            ViewDirection.UP -> up()
            ViewDirection.RIGHT -> right()
            ViewDirection.DOWN -> down()
            ViewDirection.LEFT -> left()
        }
    }

    fun Grid2D<Int>.getVisibilityOfTree(
        cell: Grid2D.Cell<Int>,
        viewDirection: ViewDirection,
        visibilityCache: MutableMap<Grid2D.Coordinate, MutableMap<ViewDirection, TreeVisibility>>,
    ): TreeVisibility {
        return visibilityCache.getOrPut(cell.coordinate) {
            mutableMapOf()
        }.getOrPut(viewDirection) {
            if (isBorderCell(cell)) {
                TreeVisibility.VISIBLE
            } else {
                val heightestNeighbor = getNeighborsOfCellInViewDirection(cell, viewDirection)
                    .maxBy { it.value }

                if (heightestNeighbor.value >= cell.value) {
                    TreeVisibility.INVISIBLE
                } else {
                    TreeVisibility.VISIBLE
                }
            }
        }
    }

    fun <T> List<List<T>>.toGrid2D(): Grid2D<T> {
        return Grid2D(this)
    }

    object Part1 {
        fun Sequence<String>.solve(): Int {
            return mapNotNull { inputRow ->
                if (inputRow.isNotBlank()) {
                    inputRow.toIntList()
                } else {
                    null
                }
            }.toList()
                .toGrid2D()
                .run {
                    val visibilityCache = mutableMapOf<Grid2D.Coordinate, MutableMap<ViewDirection, TreeVisibility>>()

                    transformValues { cell ->
                        ViewDirection
                            .values()
                            .map {
                                getVisibilityOfTree(cell, it, visibilityCache)
                            }
                            .reduce { treeVisibility, directionalTreeVisibility ->
                                when (treeVisibility) {
                                    TreeVisibility.VISIBLE -> TreeVisibility.VISIBLE
                                    TreeVisibility.INVISIBLE -> directionalTreeVisibility
                                }
                            }

                    }
                        .also { grid -> println(grid.toConsoleString { it.value.name.substring(0..0) }) }
                        .count { it.value == TreeVisibility.VISIBLE }
                }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            "puzzle-8.in"
                .streamInput()
                .solve()
                .run(::println)
        }
    }

}
