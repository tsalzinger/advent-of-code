package puzzles

import me.salzinger.common.Grid2D
import me.salzinger.common.streamInput
import me.salzinger.common.toConsoleString

object Puzzle09RopeBridge {
    private fun Grid2D.Coordinate.isNeighborOf(coordinate: Grid2D.Coordinate): Boolean {
        return getNeighbors(Grid2D.Coordinate.NeighborModes.RING).contains(coordinate)
    }

    private fun Grid2D.Coordinate.isDiagonalNeighborOf(coordinate: Grid2D.Coordinate): Boolean {
        return getNeighbors {
            setOf(
                up().right(),
                right().down(),
                down().left(),
                left().up()
            )
        }.contains(coordinate)
    }

    object Part1 {


        fun String.toCoordinateTransformations(): Sequence<Grid2D.Coordinate.() -> Grid2D.Coordinate> {
            val (direction, movementCount) = split(" ")

            val coordinateTransformation = when (direction) {
                "U" -> Grid2D.Coordinate::up
                "R" -> Grid2D.Coordinate::right
                "D" -> Grid2D.Coordinate::down
                "L" -> Grid2D.Coordinate::left
                else -> throw RuntimeException("Unknown movement direction: $direction")
            }

            return sequenceOf(*Array(movementCount.toInt()) { coordinateTransformation })
        }

        data class CellPrinter(val transformer: Grid2D.Coordinate.() -> String)

        fun Sequence<String>.solve(): Int {
            val startingCoordinate = Grid2D.Coordinate(50, 50)

            var headPosition = startingCoordinate
            var tailPosition = startingCoordinate
            val tailPositions = mutableSetOf(tailPosition)
            var lastCoordinateTransformer: Grid2D.Coordinate.() -> Grid2D.Coordinate = { this }

            val grid = Grid2D(
                List(101) {
                    List(101) {
                        CellPrinter {
                            when {
                                this == headPosition -> "H"
                                this == tailPosition -> "T"
                                this == startingCoordinate -> "s"
                                this in tailPositions -> "#"
                                else -> "."
                            }
                        }
                    }
                }
            )

            println("===================")
            println(grid.toConsoleString { it.value.transformer(it.coordinate) })

            flatMap { it.toCoordinateTransformations() }
//                .take(10)
                .forEach { coordinateTransformer ->
                    val newHeadPosition = coordinateTransformer(headPosition)

                    if (newHeadPosition != tailPosition && !newHeadPosition.isNeighborOf(tailPosition)) {
                        tailPosition = if (tailPosition.isDiagonalNeighborOf(headPosition)) {
                            headPosition
                        } else {
                            coordinateTransformer(tailPosition)
                        }
                    }

                    tailPositions.add(tailPosition)
                    headPosition = newHeadPosition
                    lastCoordinateTransformer = coordinateTransformer

                    println("===================")
                    println(grid.toConsoleString { it.value.transformer(it.coordinate) })
                }

            return tailPositions.count()
        }

        @JvmStatic
        fun main(args: Array<String>) {
            "puzzle-9.in"
                .streamInput()
                .solve()
                .run(::println)
        }
    }
}
