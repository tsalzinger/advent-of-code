package me.salzinger.puzzles.puzzle3

import me.salzinger.common.Grid2D

object GearRatios {
    class GameGrid(values: List<List<GridValue>>) {
        val grid: Grid2D<GridValue> = Grid2D(
            values = values,
            neighborProvider = Grid2D.Coordinate.NeighborModes.RING,
        )
    }

    sealed interface GridValue {
        data object Empty : GridValue

        data class Number(val value: Int) : GridValue

        data class Symbol(val symbol: Char) : GridValue
    }


    private fun Char.toGridValue(): GridValue {
        return when {
            isDigit() -> GridValue.Number(digitToInt())
            this == '.' -> GridValue.Empty
            else -> GridValue.Symbol(this)
        }
    }

    fun Sequence<String>.parseAsGameGrid(): GameGrid {
        return GameGrid(
            values = map { line ->
                line.map { it.toGridValue() }
            }.toList()
        )
    }

    fun GameGrid.buildNumberMap(): Map<Grid2D.Coordinate, Pair<Grid2D.Coordinate, Int>> {
        val numberMap = mutableMapOf<Grid2D.Coordinate, Pair<Grid2D.Coordinate, Int>>()
        var currentCoordinate = Grid2D.Coordinate(
            row = 0,
            column = 0
        )
        var currentValue = 0
        val numberCoordinates = mutableSetOf<Grid2D.Coordinate>()

        fun recordNumberCoordinates() {
            if (numberCoordinates.isNotEmpty()) {
                val numberStart = numberCoordinates.first() to currentValue
                numberCoordinates
                    .forEach {
                        numberMap[it] = numberStart
                    }

                numberCoordinates.clear()
                currentValue = 0
            }
        }

        while (currentCoordinate.row < grid.rows) {
            while (currentCoordinate.column < grid.columns) {
                val cell = grid.getCellAt(currentCoordinate)

                if (cell.value is GridValue.Number) {
                    numberCoordinates.add(cell.coordinate)
                    currentValue = (currentValue * 10) + cell.value.value
                } else {
                    recordNumberCoordinates()
                }

                currentCoordinate = currentCoordinate.right()
            }

            recordNumberCoordinates()

            currentCoordinate = currentCoordinate.down().withColumn(0)
        }

        return numberMap.toMap()
    }

    fun GameGrid.getAllNumbersAdjacentToASymbol(): Map<Grid2D.Coordinate, Int> {
        val numberMap = buildNumberMap()

        return symbolCells()
            .getNumberNeighbors(grid)
            .associate {
                numberMap.getValue(it.coordinate)
            }
    }

    fun GameGrid.getSumOfAllNumbersAdjacentToASymbol(): Int {
        return getAllNumbersAdjacentToASymbol()
            .values
            .sum()
    }


    fun Iterable<Grid2D.Cell<GridValue>>.getNumberNeighbors(grid: Grid2D<GridValue>): Iterable<Grid2D.Cell<GridValue>> {
        return flatMap {
            grid.getNeighborsOf(it)
                .filterValueIs<GridValue.Number>()
        }
    }

    fun GameGrid.symbolCells(): Set<Grid2D.Cell<GridValue>> {
        return grid
            .filterValueIs<GridValue.Symbol>().toSet()
    }

    inline fun <reified T : GridValue> Iterable<Grid2D.Cell<GridValue>>.filterValueIs(): Iterable<Grid2D.Cell<GridValue>> {
        return filter {
            it.value is T
        }
    }
}
