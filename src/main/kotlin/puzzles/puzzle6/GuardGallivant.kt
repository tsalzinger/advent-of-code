package me.salzinger.puzzles.puzzle6

import me.salzinger.common.Grid2D
import me.salzinger.common.Grid2D.Coordinate
import me.salzinger.common.extensions.toGrid2D
import me.salzinger.common.geometry.Direction
import me.salzinger.common.geometry.invoke
import me.salzinger.puzzles.puzzle6.GuardGallivant.LabTile.Empty
import me.salzinger.puzzles.puzzle6.GuardGallivant.LabTile.Guard
import me.salzinger.puzzles.puzzle6.GuardGallivant.LabTile.Obstacle

object GuardGallivant {
    sealed class LabTile(val representation: Char) {
        object Guard : LabTile('^')
        object Obstacle : LabTile('#')

        sealed class Empty : LabTile('.') {
            object Visited : Empty()
            object NotVisited : Empty()
        }
    }

    fun Sequence<String>.countPositionsVisitedByGuard(): Int {
        val labMap = map { row ->
            row.map {
                when (it) {
                    '^' -> Guard
                    '#' -> Obstacle
                    '.' -> Empty.NotVisited
                    else -> error("Unsupported character: $it")
                }
            }
        }
            .toList()
            .toGrid2D()

        val startingGuardPosition = labMap.first { cell -> cell.value == Guard }.coordinate
        var currentDirection = Direction.UP
        var currentGuardPosition = startingGuardPosition
        val visitedPositions = mutableSetOf<Coordinate>()

        while (currentGuardPosition in labMap) {
            visitedPositions += currentGuardPosition

            while(!labMap.canMove(currentGuardPosition, currentDirection)) {
                currentDirection = currentDirection.turnRight()
            }

            currentGuardPosition = currentGuardPosition(currentDirection)
        }

        return visitedPositions.size
    }

    fun Grid2D<LabTile>.canMove(currentPosition: Coordinate, direction: Direction): Boolean {
        return when (this.getCellAtOrNull(currentPosition(direction))?.value) {
            Empty.NotVisited, Empty.Visited, null -> return true
            Obstacle -> return false
            Guard -> error("no second guard expected")
        }
    }

    fun Direction.turnRight(): Direction {
        return when (this) {
            Direction.UP -> Direction.RIGHT
            Direction.RIGHT -> Direction.DOWN
            Direction.DOWN -> Direction.LEFT
            Direction.LEFT -> Direction.UP
            Direction.RIGHT_DOWN -> Direction.DOWN_LEFT
            Direction.DOWN_LEFT -> Direction.LEFT_UP
            Direction.LEFT_UP -> Direction.UP_RIGHT
            Direction.UP_RIGHT -> Direction.RIGHT_DOWN
        }
    }

}