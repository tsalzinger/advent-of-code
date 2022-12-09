package puzzles

import me.salzinger.common.Grid2D
import me.salzinger.common.streamInput
import me.salzinger.common.toConsoleString
import puzzles.Puzzle09RopeBridge.Part1.printAsGrid
import puzzles.Puzzle09RopeBridge.Part1.simulateTailMovement
import puzzles.Puzzle09RopeBridge.Part1.toCoordinateTransformations
import puzzles.Puzzle09RopeBridge.Part1.visitedCoordinates
import kotlin.math.sign

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

        data class CellPrinter(val transformer: Grid2D.Coordinate.() -> String) {
            operator fun invoke(coordinate: Grid2D.Coordinate) = transformer(coordinate)
        }

        fun Sequence<Grid2D.Coordinate.() -> Grid2D.Coordinate>.simulateTailMovement(): Sequence<Grid2D.Coordinate.() -> Grid2D.Coordinate> {
            val startingCoordinate = Grid2D.Coordinate(0, 0)
            var headPosition = startingCoordinate
            var tailPosition = startingCoordinate

            return map { coordinateTransformer ->
                val currentTailPosition = tailPosition
                headPosition = coordinateTransformer(headPosition)
                val tailPositionTransformer: (Grid2D.Coordinate.() -> Grid2D.Coordinate) =
                    when {
                        headPosition == currentTailPosition -> {
                            { currentTailPosition }
                        }

                        headPosition.isNeighborOf(currentTailPosition) -> {
                            { currentTailPosition }
                        }

                        else -> {
                            {
                                Grid2D.Coordinate(
                                    row = row + (headPosition.row - currentTailPosition.row).sign,
                                    column = column + (headPosition.column - currentTailPosition.column).sign,
                                )
                            }
                        }
                    }

                tailPosition = tailPositionTransformer(tailPosition)
                tailPositionTransformer
            }
        }

        fun Sequence<String>.toCoordinateTransformations(): Sequence<Grid2D.Coordinate.() -> Grid2D.Coordinate> {
            return flatMap { it.toCoordinateTransformations() }
        }


        fun Sequence<Grid2D.Coordinate.() -> Grid2D.Coordinate>.visitedCoordinates(): Set<Grid2D.Coordinate> {
            val startingCoordinate = Grid2D.Coordinate(
                0,
                0
            )

            return fold(
                startingCoordinate to setOf<Grid2D.Coordinate>(startingCoordinate)
            ) { (currentCoordinate, visitedCoordinates), coordinateTransformation ->
                coordinateTransformation(currentCoordinate).run {
                    this to (visitedCoordinates + this)
                }
            }.second
        }

        fun Set<Grid2D.Coordinate>.printAsGrid(): Set<Grid2D.Coordinate> {
            val minRow = minOf { it.row }
            val maxRow = maxOf { it.row }
            val minColumn = minOf { it.column }
            val maxColumn = maxOf { it.column }

            val rows = maxRow - minRow
            val columns = maxColumn - minColumn

            Grid2D(
                List(rows + 1) {
                    List(columns + 1) {
                        CellPrinter {
                            when (copy(row = row + minRow, column = column + minColumn)) {
                                in this@printAsGrid -> "#"
                                else -> "."
                            }
                        }
                    }
                }
            ).toConsoleString { it.value(it.coordinate) }
                .run(::println)

            return this
        }

        fun Sequence<Grid2D.Coordinate.() -> Grid2D.Coordinate>.countVisitedCoordinates(): Int {
            return visitedCoordinates().count()
        }

        fun Sequence<String>.solve(): Int {
            return toCoordinateTransformations()
                .simulateTailMovement()
                .countVisitedCoordinates()
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
            return toCoordinateTransformations()
                .simulateTailMovement()
                .simulateTailMovement()
                .simulateTailMovement()
                .simulateTailMovement()
                .simulateTailMovement()
                .simulateTailMovement()
                .simulateTailMovement()
                .simulateTailMovement()
                .simulateTailMovement()
                .visitedCoordinates()
                .printAsGrid()
                .count()
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
