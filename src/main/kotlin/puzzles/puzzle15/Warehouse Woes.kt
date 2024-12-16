package me.salzinger.puzzles.puzzle15

import me.salzinger.common.Grid2D
import me.salzinger.common.extensions.ChunkEvaluation
import me.salzinger.common.extensions.chunkedBy
import me.salzinger.common.extensions.toGrid2D
import me.salzinger.common.geometry.Direction
import me.salzinger.common.geometry.invoke
import me.salzinger.common.toConsoleString

object `Warehouse Woes` {
    enum class Tile {
        WALL,
        BOX,
        ROBOT,
        EMPTY,
    }

    fun Sequence<String>.sumOfGpsCoordinates(): Long {
        val (warehouseMapInput, movementsInput) = toList()
            .chunkedBy {
                if (it.isBlank()) {
                    ChunkEvaluation.END_CHUNK_AND_DISCARD
                } else {
                    ChunkEvaluation.APPEND_TO_CHUNK
                }
            }

        val initialWarehouseMap = warehouseMapInput
            .map {
                it.map { tileChar ->
                    when (tileChar) {
                        '#' -> Tile.WALL
                        'O' -> Tile.BOX
                        '@' -> Tile.ROBOT
                        '.' -> Tile.EMPTY
                        else -> error("Illegal tile $tileChar")
                    }
                }
            }
            .toGrid2D(neighborProvider = Grid2D.Coordinate.NeighborModes.CROSS)

        val initialRobotPosition = initialWarehouseMap.single { it.value == Tile.ROBOT }.coordinate


        val movements = movementsInput
            .flatMap {
                it.map { movementChar ->
                    when (movementChar) {
                        '^' -> Direction.UP
                        '>' -> Direction.RIGHT
                        'v' -> Direction.DOWN
                        '<' -> Direction.LEFT
                        else -> error("Illegal movement $movementChar")
                    }
                }
            }


        return movements.fold(
            initialRobotPosition to initialWarehouseMap,
        ) { (robotPosition, warehouseMap), movementDirection ->
            val nextPosition = robotPosition(movementDirection)
            val nextCell = warehouseMap[nextPosition]
            when (nextCell.value) {
                Tile.WALL -> robotPosition to warehouseMap
                Tile.BOX -> {
                    var currentCell = nextCell
                    while (currentCell.value == Tile.BOX) {
                        currentCell = warehouseMap[currentCell.coordinate(movementDirection)]
                    }

                    if (currentCell.value == Tile.EMPTY) {
                        nextPosition to warehouseMap.transformValues {
                            when (it.coordinate) {
                                robotPosition -> Tile.EMPTY
                                nextPosition -> Tile.ROBOT
                                currentCell.coordinate -> Tile.BOX
                                else -> it.value
                            }
                        }
                    } else {
                        robotPosition to warehouseMap
                    }
                }

                Tile.EMPTY -> nextPosition to warehouseMap.transformValues {
                    when (it.coordinate) {
                        robotPosition -> Tile.EMPTY
                        nextPosition -> Tile.ROBOT
                        else -> it.value
                    }
                }

                Tile.ROBOT -> error("Encountered a second robot at $nextPosition")
            }
        }
            .second
            .also {
                println(it.toConsoleString {
                    when (it.value) {
                        Tile.WALL -> "#"
                        Tile.BOX -> "O"
                        Tile.ROBOT -> "@"
                        Tile.EMPTY -> "."
                    }
                })
            }
            .filter { it.value == Tile.BOX }
            .sumOf { 100L * it.coordinate.row + it.coordinate.column }
    }
}