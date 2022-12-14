package puzzles

import me.salzinger.common.Grid2D
import me.salzinger.common.extensions.toIntList
import me.salzinger.common.geometry.LazyGrid2D
import me.salzinger.common.geometry.toConsoleString
import me.salzinger.common.streamInput

object Puzzle14RegolithReservoir {
    object Part1 {

        fun Grid2D.Coordinate.lineTo(target: Grid2D.Coordinate): List<Grid2D.Coordinate> {
            return when {
                this == target -> listOf(this)
                row == target.row -> {
                    if (column > target.column) {
                        (column downTo target.column)
                            .map { Grid2D.Coordinate(row, it) }
                    } else {
                        (column..target.column)
                            .map { Grid2D.Coordinate(row, it) }
                    }
                }

                column == target.column -> {
                    if (row > target.row) {
                        (row downTo target.row)
                            .map { Grid2D.Coordinate(it, column) }
                    } else {
                        (row..target.row)
                            .map { Grid2D.Coordinate(it, column) }
                    }
                }

                else -> throw RuntimeException("Coordinates need to be on the same row or column: $this, $target")
            }
        }

        fun String.toLineCoordinates(): List<Grid2D.Coordinate> {
            return split(" -> ")
                .map {
                    val (x, y) = it.toIntList(",")
                    Grid2D.Coordinate(row = y, column = x)
                }
                .windowed(2) { (first, second) ->
                    first.lineTo(second)
                }.reduce { line, coordinates ->
                    line + coordinates
                }.distinct()
        }

        enum class OccupationType {
            Air,
            Rock,
            Sand,
        }

        fun Sequence<List<Grid2D.Coordinate>>.toGrid(
            boundaryTransformer: (LazyGrid2D.Boundary) -> LazyGrid2D.Boundary,
            sandLocations: () -> Set<Grid2D.Coordinate>,
        ): LazyGrid2D<OccupationType> {
            val rockLocations = flatten().toSet()
            val boundary = LazyGrid2D.Boundary(
                minRow = rockLocations.minOf { it.row },
                maxRow = rockLocations.maxOf { it.row },
                minColumn = rockLocations.minOf { it.column },
                maxColumn = rockLocations.maxOf { it.column },
            ).let(boundaryTransformer)

            return LazyGrid2D(
                valuesProvider = { coordinate ->
                    when (coordinate) {
                        in sandLocations() -> OccupationType.Sand
                        in rockLocations -> OccupationType.Rock
                        else -> OccupationType.Air
                    }
                },
                boundaryProvider = { boundary },
            )
        }

        fun LazyGrid2D<OccupationType>.getSandTargetCoordinateOrNull(spawnCoordinate: Grid2D.Coordinate): Grid2D.Coordinate? {
            var currentCell = getCellAtOrNull(spawnCoordinate.down())

            while (currentCell != null) {
                // move down
                while (currentCell?.value == OccupationType.Air) {
                    currentCell = getCellAtOrNull(currentCell.coordinate.down())
                }

                if (currentCell != null) {
                    // can move diagonal?
                    val diagonalLeftCell = getCellAtOrNull(currentCell.coordinate.left())
                    currentCell = when {
                        diagonalLeftCell == null -> {
                            null
                        }

                        diagonalLeftCell.value == OccupationType.Air -> {
                            diagonalLeftCell
                        }

                        else -> {
                            val diagonalRightCell = getCellAtOrNull(currentCell.coordinate.right())
                            when {
                                diagonalRightCell == null -> {
                                    null
                                }

                                diagonalRightCell.value == OccupationType.Air -> {
                                    diagonalRightCell
                                }

                                else -> {
                                    return currentCell.coordinate.up()
                                }
                            }
                        }
                    }
                }
            }

            return currentCell
        }

        fun Sequence<String>.solve(): Int {
            val sandLocations = mutableSetOf<Grid2D.Coordinate>()
            val startingLocation = Grid2D.Coordinate(row = 0, column = 500)

            map {
                it.toLineCoordinates()
            }.toGrid(
                boundaryTransformer = {
                    if (startingLocation in it) {
                        it
                    } else {
                        it.copy(
                            minRow = startingLocation.row
                        )
                    }
                },
                sandLocations = { sandLocations },
            ).also { grid ->
                var nextSandLocation = grid.getSandTargetCoordinateOrNull(startingLocation)
                while (nextSandLocation != null) {
                    sandLocations += nextSandLocation
                    nextSandLocation = grid.getSandTargetCoordinateOrNull(startingLocation)
                }
            }
                .also { grid ->
                    println(grid.toConsoleString { cell ->
                        when (cell.value) {
                            OccupationType.Air -> {
                                if (cell.coordinate == startingLocation) {
                                    "+"
                                } else {
                                    "."
                                }
                            }

                            OccupationType.Rock -> "#"
                            OccupationType.Sand -> "o"
                        }
                    })
                }


            return sandLocations.count()
        }

        @JvmStatic
        fun main(args: Array<String>) {
            "puzzle-14.in"
                .streamInput()
                .solve()
                .let(::println)
        }
    }
}
