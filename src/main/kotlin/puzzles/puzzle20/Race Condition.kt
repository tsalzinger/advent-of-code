package me.salzinger.puzzles.puzzle20

import me.salzinger.common.Grid2D
import me.salzinger.common.Grid2D.Coordinate
import me.salzinger.common.extensions.permutate
import me.salzinger.common.extensions.toGrid2D
import kotlin.math.abs

object `Race Condition` {
    object Tile {
        val TRACK = '.'
        val START = 'S'
        val END = 'E'
        val WALL = '#'
    }

    fun Sequence<String>.countAvailableCheatsSavingAtLeast100Picoseconds(): Int {
        val map = toGrid2D(neighborProvider = Grid2D.Coordinate.NeighborModes.CROSS)

        val startCoordinate = map.single { it.value == Tile.START }.coordinate
        val endCoordinate = map.single { it.value == Tile.END }.coordinate

        var steps = 0
        var currentCoordinate = startCoordinate
        val alreadyVisited = mutableSetOf(currentCoordinate)
        val coordinateToSteps = mutableMapOf<Coordinate, Int>(
            startCoordinate to steps,
        )

        while (endCoordinate != currentCoordinate) {
            steps++
            currentCoordinate = map.getNeighborsOf(currentCoordinate)
                .single {
                    it.value in setOf(Tile.TRACK, Tile.END) &&
                            it.coordinate !in alreadyVisited
                }
                .coordinate
                .also(alreadyVisited::add)
                .also {
                    coordinateToSteps[it] = steps
                }
        }

        return map.filter {
            it.value == Tile.WALL
        }.map {
            it to map.getNeighborsOf(it)
                .filter { it.value in setOf(Tile.START, Tile.TRACK, Tile.END) }
        }
            .filter { (_, tracks ) ->
                tracks.count() >= 2
            }
            .flatMap { (_, tracks) ->
                tracks.map { it.coordinate }
                    .permutate()
                    .map { (first, second) ->
                        abs(coordinateToSteps.getValue(second) - coordinateToSteps.getValue(first)) - 1
                    }
            }
            .count { it >= 100 }
    }
}