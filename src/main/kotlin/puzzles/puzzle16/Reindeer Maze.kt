package me.salzinger.puzzles.puzzle16

import me.salzinger.common.Grid2D
import me.salzinger.common.extensions.toGrid2D
import me.salzinger.common.geometry.Direction
import me.salzinger.common.geometry.invoke
import me.salzinger.common.geometry.isOppositeOf

object `Reindeer Maze` {
    val POSSIBLE_MOVEMENT_DIRECTIONS = listOf(
        Direction.UP,
        Direction.RIGHT,
        Direction.DOWN,
        Direction.LEFT,
    )

    fun Sequence<String>.getLowestPossibleScore(): Long {

        val maze = toGrid2D(neighborProvider = Grid2D.Coordinate.NeighborModes.CROSS)

        val startCell = maze.single { it.value == 'S' }
        val endCell = maze.single { it.value == 'E' }

        val lowestScores = mutableMapOf(
            startCell.coordinate to 0L
        )

        var currentPositions = listOf(
            Triple(startCell.coordinate, Direction.RIGHT, 0L),
        )

        while (currentPositions.isNotEmpty()) {
            currentPositions = currentPositions
                .flatMap { (currentPosition, currentDirection, currentScore) ->
                    POSSIBLE_MOVEMENT_DIRECTIONS
                        .mapNotNull { direction ->
                            currentPosition(direction)
                                .takeIf { maze[it].value != '#' }
                                ?.let { direction to it }
                        }
                        .map { (direction, position) ->
                            Triple(
                                position, direction, when {
                                    currentDirection == direction -> currentScore + 1
                                    currentDirection.isOppositeOf(direction) -> currentScore + 2001
                                    else -> currentScore + 1001
                                }
                            )
                        }
                        .filter { (position, _, score) ->
                            position !in lowestScores || lowestScores.getValue(position) > score
                        }
                        .onEach { (position, _, score) ->
                            lowestScores[position] = score
                        }
                }
        }

        return lowestScores.getValue(endCell.coordinate)
    }

    fun Sequence<String>.countTilesOnBestPaths(): Int {
        val maze = toGrid2D(neighborProvider = Grid2D.Coordinate.NeighborModes.CROSS)

        val startCell = maze.single { it.value == 'S' }
        val endCell = maze.single { it.value == 'E' }

        val lowestScores = mutableMapOf(
            startCell.coordinate to 0L
        )

        var currentPositions = listOf(
            Triple(listOf(startCell.coordinate), Direction.RIGHT, 0L),
        )

        val bestPathCandidates = mutableSetOf<Pair<List<Grid2D.Coordinate>, Long>>()

        while (currentPositions.isNotEmpty()) {
            currentPositions = currentPositions
                .flatMap { (pathSoFar, currentDirection, currentScore) ->
                    POSSIBLE_MOVEMENT_DIRECTIONS
                        .mapNotNull { direction ->
                            pathSoFar.last()(direction)
                                .takeIf { maze[it].value != '#' && it !in pathSoFar }
                                ?.let { direction to it }
                        }
                        .map { (direction, position) ->
                            Triple(
                                pathSoFar + position,
                                direction,
                                when {
                                    currentDirection == direction -> currentScore + 1
                                    currentDirection.isOppositeOf(direction) -> currentScore + 2001
                                    else -> currentScore + 1001
                                }
                            )
                        }
                        .filter { (path, _, score) ->
                            (path.last() !in lowestScores) ||
                                    ((lowestScores.getValue(path.last()) + 1000) >= score)
                        }
                        .onEach { (path, _, score) ->
                            if (path.last() == endCell.coordinate) {
                                bestPathCandidates += path to score
                            }
                            lowestScores[path.last()] = score
                        }
                }
        }

        val lowestScore = lowestScores.getValue(endCell.coordinate)


        val bestPaths = bestPathCandidates
            .filter { (_, score) ->
                score == lowestScore
            }
            .map { it.first.toSet() }

        val tilesOnBestPaths = bestPaths
            .flatMap { it }
            .toSet()

        return tilesOnBestPaths.count()
    }
}