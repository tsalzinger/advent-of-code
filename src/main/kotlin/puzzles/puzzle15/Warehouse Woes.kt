package me.salzinger.puzzles.puzzle15

import me.salzinger.common.Grid2D
import me.salzinger.common.extensions.ChunkEvaluation
import me.salzinger.common.extensions.chunkedBy
import me.salzinger.common.extensions.toGrid2D
import me.salzinger.common.geometry.*
import me.salzinger.common.toConsoleString

object `Warehouse Woes` {
    enum class Tile {
        WALL,
        BOX,
        LARGE_BOX_LEFT,
        LARGE_BOX_RIGHT,
        ROBOT,
        EMPTY,
    }

    fun Sequence<String>.getInitialMapAndRobotMovements(): Pair<Grid2D<Tile>, List<Direction>> {
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
                        '[' -> Tile.LARGE_BOX_LEFT
                        ']' -> Tile.LARGE_BOX_RIGHT
                        '@' -> Tile.ROBOT
                        '.' -> Tile.EMPTY
                        else -> error("Illegal tile $tileChar")
                    }
                }
            }
            .toGrid2D(neighborProvider = Grid2D.Coordinate.NeighborModes.CROSS)

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

        return initialWarehouseMap to movements
    }


    val largeBoxTiles = setOf(Tile.LARGE_BOX_LEFT, Tile.LARGE_BOX_RIGHT)

    fun Grid2D<Tile>.getAffectedLargeBoxesOrNull(
        position: Grid2D.Coordinate,
        direction: Direction,
    ): List<Grid2D.Cell<Tile>>? {
        return when (direction) {
            Direction.UP,
            Direction.DOWN -> {
                val cell = get(position(direction))

                when (cell.value) {
                    Tile.EMPTY -> emptyList()
                    Tile.LARGE_BOX_LEFT -> {
                        val otherCell = get(cell.coordinate(Direction.RIGHT))
                        listOf(
                            cell,
                            otherCell,
                        )
                    }

                    Tile.LARGE_BOX_RIGHT -> {
                        val otherCell = get(cell.coordinate(Direction.LEFT))
                        listOf(
                            cell,
                            otherCell,
                        )
                    }

                    else -> null
                }
            }

            else -> error("Illegal direction $direction")
        }?.let { cells ->
            cells + cells.flatMap {
                getAffectedLargeBoxesOrNull(it.coordinate, direction) ?: return null
            }
        }
    }

    fun Grid2D<Tile>.performRobotMovements(movements: List<Direction>): Grid2D<Tile> {
        val initialRobotPosition = single { it.value == Tile.ROBOT }.coordinate

        return movements.fold(
            initialRobotPosition to this,
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

                Tile.LARGE_BOX_LEFT -> {
                    when (movementDirection) {
                        Direction.RIGHT -> {
                            var currentCell = nextCell
                            val largeBoxCoordinates = mutableSetOf(currentCell.coordinate)
                            while (currentCell.value in setOf(Tile.LARGE_BOX_LEFT, Tile.LARGE_BOX_RIGHT)) {
                                currentCell = warehouseMap[currentCell.coordinate(movementDirection)]
                                largeBoxCoordinates += currentCell.coordinate
                            }

                            if (currentCell.value == Tile.EMPTY) {
                                nextPosition to warehouseMap.transformValues {
                                    when (it.coordinate) {
                                        robotPosition -> Tile.EMPTY
                                        nextPosition -> Tile.ROBOT
                                        in largeBoxCoordinates -> when (it.value) {
                                            Tile.LARGE_BOX_LEFT -> Tile.LARGE_BOX_RIGHT
                                            Tile.LARGE_BOX_RIGHT -> Tile.LARGE_BOX_LEFT
                                            Tile.EMPTY -> Tile.LARGE_BOX_RIGHT
                                            else -> error("Illegal tile $it")
                                        }

                                        currentCell.coordinate -> Tile.LARGE_BOX_RIGHT
                                        else -> it.value
                                    }
                                }
                            } else {
                                robotPosition to warehouseMap
                            }
                        }

                        Direction.UP, Direction.DOWN -> {
                            val boxCells =
                                warehouseMap.getAffectedLargeBoxesOrNull(robotPosition, movementDirection)
                                    ?.associateBy { it.coordinate }

                            if (
                                boxCells != null
                            ) {
                                nextPosition to warehouseMap.transformValues {
                                    when (it.coordinate) {
                                        nextPosition -> Tile.ROBOT
                                        robotPosition -> Tile.EMPTY
                                        in boxCells -> {
                                            val sourcePosition = it.coordinate(movementDirection.flip())
                                            boxCells[sourcePosition]?.value ?: Tile.EMPTY
                                        }

                                        else -> {
                                            val sourcePosition = it.coordinate(movementDirection.flip())
                                            boxCells[sourcePosition]?.value ?: it.value
                                        }
                                    }
                                }
                            } else {
                                robotPosition to warehouseMap
                            }
                        }

                        else -> error("Cannot encounter left part of large box in direction $movementDirection")
                    }
                }

                Tile.LARGE_BOX_RIGHT ->
                    when (movementDirection) {
                        Direction.LEFT -> {
                            var currentCell = nextCell
                            val largeBoxCoordinates = mutableSetOf(currentCell.coordinate)
                            while (currentCell.value in setOf(Tile.LARGE_BOX_LEFT, Tile.LARGE_BOX_RIGHT)) {
                                currentCell = warehouseMap[currentCell.coordinate(movementDirection)]
                                largeBoxCoordinates += currentCell.coordinate
                            }

                            if (currentCell.value == Tile.EMPTY) {
                                nextPosition to warehouseMap.transformValues {
                                    when (it.coordinate) {
                                        robotPosition -> Tile.EMPTY
                                        nextPosition -> Tile.ROBOT
                                        in largeBoxCoordinates -> when (it.value) {
                                            Tile.LARGE_BOX_LEFT -> Tile.LARGE_BOX_RIGHT
                                            Tile.LARGE_BOX_RIGHT -> Tile.LARGE_BOX_LEFT
                                            Tile.EMPTY -> Tile.LARGE_BOX_LEFT
                                            else -> error("Illegal tile $it")
                                        }

                                        currentCell.coordinate -> Tile.LARGE_BOX_LEFT
                                        else -> it.value
                                    }
                                }
                            } else {
                                robotPosition to warehouseMap
                            }
                        }

                        Direction.UP, Direction.DOWN -> {
                            val boxCells =
                                warehouseMap.getAffectedLargeBoxesOrNull(robotPosition, movementDirection)
                                    ?.associateBy { it.coordinate }

                            if (
                                boxCells != null
                            ) {
                                nextPosition to warehouseMap.transformValues {
                                    when (it.coordinate) {
                                        nextPosition -> Tile.ROBOT
                                        robotPosition -> Tile.EMPTY
                                        in boxCells -> {
                                            val sourcePosition = it.coordinate(movementDirection.flip())
                                            boxCells[sourcePosition]?.value ?: Tile.EMPTY
                                        }

                                        else -> {
                                            val sourcePosition = it.coordinate(movementDirection.flip())
                                            boxCells[sourcePosition]?.value ?: it.value
                                        }
                                    }
                                }
                            } else {
                                robotPosition to warehouseMap
                            }
                        }

                        else -> error("Cannot encounter right part of large box in direction $movementDirection")
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
    }

    private fun Grid2D<Tile>.printWarehouseMap(): Grid2D<Tile> {
        println(toConsoleString {
            when (it.value) {
                Tile.WALL -> "#"
                Tile.BOX -> "O"
                Tile.LARGE_BOX_LEFT -> "["
                Tile.LARGE_BOX_RIGHT -> "]"
                Tile.ROBOT -> "@"
                Tile.EMPTY -> "."
            }
        })

        return this
    }

    private fun Grid2D<Tile>.sumOfGpsCoordinates(): Long {
        return filter { it.value in setOf(Tile.BOX, Tile.LARGE_BOX_LEFT) }
            .sumOf { 100L * it.coordinate.row + it.coordinate.column }
    }

    fun Sequence<String>.sumOfGpsCoordinates(): Long {
        val (initialWarehouseMap, movements) = getInitialMapAndRobotMovements()

        return initialWarehouseMap
            .performRobotMovements(movements)
            .printWarehouseMap()
            .sumOfGpsCoordinates()
    }

    fun Sequence<String>.sumOfGpsCoordinatesOfExtendedWarehouse(): Long {
        val (initialWarehouseMap, movements) = map { row ->
            row.map { tile ->
                when (tile) {
                    '.' -> ".."
                    'O' -> "[]"
                    '#' -> "##"
                    '@' -> "@."
                    in setOf('<', '^', '>', 'v') -> tile
                    else -> error("Illegal tile $tile")
                }
            }.joinToString("")
        }.getInitialMapAndRobotMovements()

        return initialWarehouseMap
            .performRobotMovements(movements)
            .printWarehouseMap()
            .sumOfGpsCoordinates()
    }
}