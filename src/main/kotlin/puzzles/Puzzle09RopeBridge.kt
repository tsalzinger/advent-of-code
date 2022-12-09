package puzzles

import me.salzinger.common.Grid2D
import me.salzinger.common.streamInput

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

        fun Sequence<Grid2D.Coordinate.() -> Grid2D.Coordinate>.simulateTailMovement(): Sequence<Grid2D.Coordinate.() -> Grid2D.Coordinate> {
            val startingCoordinate = Grid2D.Coordinate(0, 0)
            var headPosition = startingCoordinate
            var tailPosition = startingCoordinate

            return map { coordinateTransformer ->
                val currentHeadPosition = headPosition
                val currentTailPosition = tailPosition
                val newHeadPosition = coordinateTransformer(headPosition)
                val tailPositionTransformer: (Grid2D.Coordinate.() -> Grid2D.Coordinate) =
                    when {
                        newHeadPosition == currentTailPosition -> {
                            { currentTailPosition }
                        }

                        newHeadPosition.isNeighborOf(currentTailPosition) -> {
                            { currentTailPosition }
                        }


                        else -> {
                            { currentHeadPosition }
                        }
                    }

                headPosition = newHeadPosition
                tailPosition = tailPositionTransformer(tailPosition)
                tailPositionTransformer
            }
        }

        fun Sequence<String>.toCoordinateTransformations(): Sequence<Grid2D.Coordinate.() -> Grid2D.Coordinate> {
            return flatMap { it.toCoordinateTransformations() }
        }


        fun Sequence<String>.solve(): Int {
            val startingCoordinate = Grid2D.Coordinate(
                0,
                0
            )
            return toCoordinateTransformations()
                .simulateTailMovement()
                .fold(
                    startingCoordinate to setOf<Grid2D.Coordinate>(startingCoordinate)
                ) { (currentCoordinate, visitedCoordinates), coordinateTransformation ->
                    coordinateTransformation(currentCoordinate).run {
                        this to (visitedCoordinates + this)
                    }
                }.second.count()
        }

        @JvmStatic
        fun main(args: Array<String>) {
            "puzzle-9.in"
                .streamInput()
                .solve()
                .run(::println)
        }
    }

    object Part2 {

        fun Sequence<String>.solve(): Int {
            TODO()
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
