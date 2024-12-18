package me.salzinger.puzzles.puzzle18

import me.salzinger.common.Grid2D.Coordinate
import me.salzinger.common.geometry.LazyGrid2D
import me.salzinger.common.geometry.LazyGrid2D.Boundary
import me.salzinger.common.geometry.Point2D.Companion.toPoint2D
import me.salzinger.common.geometry.toConsoleString
import me.salzinger.puzzles.puzzle8.`Resonant Collinearity`.toCoordinate

object `RAM Run` {
    sealed interface Tile {
        object Safe : Tile

        object Unsafe : Tile
    }

    val GRID_SIZE = 71
    val START_COORDINATE = Coordinate.ZERO
    val END_COORDINATE = Coordinate(GRID_SIZE - 1, GRID_SIZE - 1)
    val BOUNDARY = Boundary.of(START_COORDINATE, END_COORDINATE)

    fun Sequence<String>.getMinNumberOfStepsToReachExitAfter1024BytesHaveFallen(): Int {
        val unsafeCoordinates = take(1024)
            .map {
                it
                    .toPoint2D()
                    .toCoordinate()
            }
            .toSet()

        val memoryMap = LazyGrid2D(
            valuesProvider = {
                when (it) {
                    in unsafeCoordinates -> Tile.Unsafe
                    else -> Tile.Safe
                }
            },
            boundaryProvider = { BOUNDARY },
            neighborProvider = Coordinate.NeighborModes.CROSS,
        )

        var steps = 0
        var currentCoordinates = setOf(START_COORDINATE)
        val alreadyVisited = currentCoordinates.toMutableSet()
        val coordinateToSteps = mutableMapOf<Coordinate, Int>(
            START_COORDINATE to steps,
        )

        while (END_COORDINATE !in currentCoordinates) {
            steps++
            currentCoordinates = currentCoordinates
                .flatMap {
                    memoryMap.getNeighborsOf(it)
                }
                .filter {
                    it.value == Tile.Safe
                }
                .map {
                    it.coordinate
                }
                .filter {
                    it !in alreadyVisited
                }
                .toSet()
                .also(alreadyVisited::addAll)
                .also {
                    it.onEach {
                        coordinateToSteps[it] = steps
                    }
                }
        }

        memoryMap
            .toConsoleString {
                when (it.coordinate) {
                    in unsafeCoordinates -> "\u001b[31m[ # ]\u001b[0m"
                    in currentCoordinates -> "[ X ]"
                    in alreadyVisited -> "[${coordinateToSteps.getValue(it.coordinate).toString().padStart(3)}]"
                    else -> "\u001b[34m[ . ]\u001b[0m"
                }
            }
            .also(::println)

        return steps
    }
}