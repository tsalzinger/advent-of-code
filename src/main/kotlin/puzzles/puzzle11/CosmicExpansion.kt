package me.salzinger.puzzles.puzzle11

import me.salzinger.common.Grid2D
import kotlin.math.max
import kotlin.math.min

object CosmicExpansion {
    sealed interface ObservationType {
        data object EmptySpace : ObservationType
        data object Galaxy : ObservationType
        data class ExpandedSpace(val size: Int) : ObservationType
    }

    fun Sequence<String>.toObservedImage(): Grid2D<ObservationType> {
        return Grid2D(
            map { input ->
                input.toList().map {
                    when (it) {
                        '.' -> ObservationType.EmptySpace
                        '#' -> ObservationType.Galaxy
                        else -> throw RuntimeException("Unexpected observation type $it")
                    }
                }
            }.toList()
        )
    }

    fun Grid2D<ObservationType>.findEmptyRowsAndColumns(): Pair<Set<Int>, Set<Int>> {
        val emptyRows = (0..<rows).toMutableSet()
        val emptyColumns = (0..<rows).toMutableSet()

        forEach {
            if (it.value !is ObservationType.EmptySpace) {
                emptyRows.remove(it.coordinate.row)
                emptyColumns.remove(it.coordinate.column)
            }
        }

        return emptyRows to emptyColumns
    }

    fun Grid2D<ObservationType>.expandEmptySpace(expansionFactor: Int = 2): Grid2D<ObservationType> {
        val (emptyRows, emptyColumns) = findEmptyRowsAndColumns()

        val rowValues = map { it.value }
            .chunked(columns)
            .map {
                it.toMutableList()
            }
            .toMutableList()

        val expandedSpace = ObservationType.ExpandedSpace(expansionFactor)

        emptyRows.sortedDescending().forEach { emptyRowIndex ->
            rowValues[emptyRowIndex].replaceAll { expandedSpace }
        }

        emptyColumns.sortedDescending().forEach { emptyColumnIndex ->
            rowValues
                .forEach { columnValues ->
                    columnValues[emptyColumnIndex] = expandedSpace
                }
        }

        return Grid2D(rowValues)
    }

    fun Grid2D<ObservationType>.getAllGalaxyPairs(): Sequence<Pair<Grid2D.Coordinate, Grid2D.Coordinate>> {
        val galaxyCoordinates = filter { it.value is ObservationType.Galaxy }.map { it.coordinate }

        return galaxyCoordinates
            .indices
            .asSequence()
            .flatMap { galaxyIndex ->
                val startGalaxyCoordiante = galaxyCoordinates[galaxyIndex]

                galaxyCoordinates
                    .drop(galaxyIndex + 1)
                    .asSequence()
                    .map {
                        startGalaxyCoordiante to it
                    }
            }
    }

    fun Sequence<Pair<Grid2D.Coordinate, Grid2D.Coordinate>>.mapToSpaceDistances(
        expandedRows: Set<Int>,
        expandedColumns: Set<Int>,
        expansionFactor: Int,
    ): Sequence<Long> {
        return map {
            it.getSpaceDistance(
                expandedRows,
                expandedColumns,
                expansionFactor,
            )
        }
    }

    fun Pair<Grid2D.Coordinate, Grid2D.Coordinate>.getSpaceDistance(
        expandedRows: Set<Int>,
        expandedColumns: Set<Int>,
        expansionFactor: Int,
    ): Long {
        val rowDistance = first.getRowRangeBetween(second).sumOf {
            if (it in expandedRows) {
                expansionFactor
            } else {
                1
            }
        }

        val columnDistance = first.getColumnRangeBetween(second).sumOf {
            if (it in expandedColumns) {
                expansionFactor
            } else {
                1
            }
        }

        return rowDistance.toLong() + columnDistance
    }

    fun Grid2D.Coordinate.getRowRangeBetween(coordinate: Grid2D.Coordinate): IntRange {
        val minRow = min(row, coordinate.row)
        val maxRow = max(row, coordinate.row)

        return minRow..<maxRow
    }

    fun Grid2D.Coordinate.getColumnRangeBetween(coordinate: Grid2D.Coordinate): IntRange {
        val minColumn = min(column, coordinate.column)
        val maxColumn = max(column, coordinate.column)

        return minColumn..<maxColumn
    }

    fun Sequence<String>.getSumOfGalaxyPairDistances(expansionFactor: Int = 2): Long {
        val observedImage = toObservedImage()

        val (expandedRows, expandedColumns) = observedImage.findEmptyRowsAndColumns()

        return observedImage
            .getAllGalaxyPairs()
            .mapToSpaceDistances(expandedRows, expandedColumns, expansionFactor)
            .sumOf { it.toLong() }
    }
}
