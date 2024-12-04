package me.salzinger.puzzles.puzzle4

import me.salzinger.common.Grid2D
import me.salzinger.common.Grid2D.Coordinate

object CeresSearch {
    private val SEARCH_STRING = "XMAS"
    fun Sequence<String>.countOccurrencesOfXmas(): Int {

        val grid = Grid2D(
            values = map { it.toCharArray().toList() }.toList(),
            neighborProvider = Coordinate.NeighborModes.RING,
        )

        return listOf(
            grid.countRight(SEARCH_STRING),
            grid.countDown(SEARCH_STRING),
            grid.countRightDown(SEARCH_STRING),
            grid.countUpRight(SEARCH_STRING),
            grid.countRight(SEARCH_STRING.reversed()),
            grid.countDown(SEARCH_STRING.reversed()),
            grid.countRightDown(SEARCH_STRING.reversed()),
            grid.countUpRight(SEARCH_STRING.reversed()),
        ).sum()
    }

    fun Grid2D<Char>.countRight(searchString: String): Int {
        return mapRows { rowIndex ->
            asString().countOccurrences(searchString)
        }.sum()
    }

    fun Grid2D<Char>.countDown(searchString: String): Int {
        return mapColumns { columnIndex ->
            asString().countOccurrences(searchString)
        }.sum()
    }

    fun Grid2D<Char>.countRightDown(searchString: String): Int {
        val grid = this

        val diagonals = (0..<rows).mapNotNull { rowIndex ->
            buildList {
                var coordinate = Coordinate(row = rowIndex, column = 0)
                while (grid.contains(coordinate)) {
                    add(get(coordinate))
                    coordinate = coordinate.rightDown()
                }
            }
        } + (1..<columns).mapNotNull { columnIndex ->
            buildList {
                var coordinate = Coordinate(row = 0, column = columnIndex)
                while (grid.contains(coordinate)) {
                    add(get(coordinate))
                    coordinate = coordinate.rightDown()
                }
            }
        }

        return diagonals.sumOf { it.asString().countOccurrences(searchString) }
    }

    fun List<Grid2D.Cell<Char>>.asString(): String {
        return String(map { cell -> cell.value }.toCharArray())
    }

    fun String.countOccurrences(searchString: String): Int {
        return Regex(searchString).findAll(this).count()
    }


    fun Grid2D<Char>.countUpRight(searchString: String): Int {
        val grid = this

        val diagonals = (0..<rows).mapNotNull { rowIndex ->
            buildList {
                var coordinate = Coordinate(row = rowIndex, column = 0)
                while (grid.contains(coordinate)) {
                    add(get(coordinate))
                    coordinate = coordinate.upRight()
                }
            }
        } + (1..<columns).mapNotNull { columnIndex ->
            buildList {
                var coordinate = Coordinate(row = lastRow, column = columnIndex)
                while (grid.contains(coordinate)) {
                    add(get(coordinate))
                    coordinate = coordinate.upRight()
                }
            }
        }

        return diagonals.sumOf { it.asString().countOccurrences(searchString) }
    }
}