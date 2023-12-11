package me.salzinger.puzzles.puzzle11

import me.salzinger.common.Grid2D

object CosmicExpansion {
    sealed interface ObservationType {
        data object EmptySpace : ObservationType
        data object Galaxy : ObservationType
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

    fun Grid2D<ObservationType>.expandEmptySpace(): Grid2D<ObservationType> {
        val (emptyRows, emptyColumns) = findEmptyRowsAndColumns()

        val rowValues = map { it.value }
            .chunked(columns)
            .map {
                it.toMutableList()
            }
            .toMutableList()

        emptyRows.sortedDescending().forEach { emptyRowIndex ->
            rowValues.add(emptyRowIndex, MutableList(columns) { ObservationType.EmptySpace })
        }

        emptyColumns.sortedDescending().forEach { emptyColumnIndex ->
            rowValues
                .forEach { columnValues ->
                    columnValues.add(emptyColumnIndex, ObservationType.EmptySpace)
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

    fun Sequence<Pair<Grid2D.Coordinate, Grid2D.Coordinate>>.mapToManhattenDistances(): Sequence<Int> {
        return map {
            it.getManhattenDistance()
        }
    }

    fun Pair<Grid2D.Coordinate, Grid2D.Coordinate>.getManhattenDistance(): Int {
        return first.getManhattenDistanceTo(second)
    }

    fun Sequence<String>.getSumOfGalaxyPairDistances(): Long {
        return toObservedImage()
            .expandEmptySpace()
            .getAllGalaxyPairs()
            .mapToManhattenDistances()
            .sumOf { it.toLong() }
    }
}
