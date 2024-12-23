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
            .filter { (_, tracks) ->
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

    fun Sequence<String>.countAvailableCheatsSavingAtLeast100PicosecondsWithUpdatedRules(): Int {
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
            it.value in setOf(Tile.START, Tile.TRACK, Tile.END)
        }.permutate()
            .filter { (first, second) -> first.coordinate.getManhattenDistanceTo(second.coordinate) <= MAX_CHEAT_LENGTH }
            .mapNotNull {
                map.getCheatSavingsOrNull(it.first.coordinate, it.second.coordinate, coordinateToSteps)
            }
            .count { it >= 100 }
    }

    val MAX_CHEAT_LENGTH = 20

    fun Grid2D<Char>.getCheatSavingsOrNull(
        startCoordinate: Coordinate,
        endCoordinate: Coordinate,
        coordinateToSteps: Map<Coordinate, Int>,
    ): Int? {
        var steps = 0
        var currentCoordinates = setOf(startCoordinate)
        val alreadyVisited = currentCoordinates.toMutableSet()

        while (endCoordinate !in currentCoordinates && steps < MAX_CHEAT_LENGTH) {
            steps++
            currentCoordinates = currentCoordinates
                .flatMap {
                    getNeighborsOf(it)
                }
                .filter {
                    it.value == Tile.WALL || it.coordinate == endCoordinate
                }
                .map { it.coordinate }
                .toSet()
                .also(alreadyVisited::addAll)
        }

        return if (endCoordinate in currentCoordinates) {
            abs(coordinateToSteps.getValue(startCoordinate) - coordinateToSteps.getValue(endCoordinate)) - steps - 1
                .also {
                    println("Found cheat saving $it")
                }
        } else {
            null
        }
    }
}