package me.salzinger.puzzles.puzzle10

import me.salzinger.common.Grid2D

object PipeMaze {

    enum class PipeType {
        VERTICAL,
        HORIZONTAL,
        NORTH_EAST,
        EAST_SOUTH,
        SOUTH_WEST,
        WEST_NORTH,
        ANY,
        NONE,
    }

    enum class NeighborPosition {
        NORTH,
        EAST,
        SOUTH,
        WEST,
    }


    class Pipe(val pipeSymbol: Char) {
        val type = when (pipeSymbol) {
            '|' -> PipeType.VERTICAL
            '-' -> PipeType.HORIZONTAL
            'L' -> PipeType.NORTH_EAST
            'F' -> PipeType.EAST_SOUTH
            '7' -> PipeType.SOUTH_WEST
            'J' -> PipeType.WEST_NORTH
            '.' -> PipeType.NONE
            'S' -> PipeType.ANY
            else -> throw RuntimeException("Unknown input: $pipeSymbol")
        }
    }

    fun Sequence<String>.toGrid(): Grid2D<Pipe> {
        return map { row ->
            row.map { pipeSymbol -> Pipe(pipeSymbol) }
        }
            .toList()
            .let(::Grid2D)
    }

    enum class MoveDirection {
        UP,
        RIGHT,
        DOWN,
        LEFT,
    }

    operator fun Grid2D.Coordinate.invoke(moveDirection: MoveDirection): Grid2D.Coordinate {
        return when (moveDirection) {
            MoveDirection.UP -> up()
            MoveDirection.RIGHT -> right()
            MoveDirection.DOWN -> down()
            MoveDirection.LEFT -> left()
        }
    }

    data class StepCount(
        val pipe: Pipe,
        var stepCount: Int? = null,
    )

    fun Grid2D<StepCount>.getNeighborsThanCanAccess(targetCell: Grid2D.Cell<StepCount>): List<Grid2D.Cell<StepCount>> {
        return setOf(
            targetCell.coordinate.up() to NeighborPosition.NORTH,
            targetCell.coordinate.right() to NeighborPosition.EAST,
            targetCell.coordinate.down() to NeighborPosition.SOUTH,
            targetCell.coordinate.left() to NeighborPosition.WEST,
        ).mapNotNull { (coordinate, position) ->
            getCellAtOrNull(coordinate)?.run {
                this to position
            }
        }.filter { (cell, neighborPosition) ->
            targetCell.value.pipe.connectsTo(cell.value.pipe, neighborPosition)
        }.map { it.first }
    }

    fun Pipe.connectsTo(neighborPipe: Pipe, neighborPosition: NeighborPosition): Boolean {
        return when (type) {
            PipeType.VERTICAL -> when (neighborPosition) {
                NeighborPosition.NORTH -> neighborPipe.type in setOf(
                    PipeType.VERTICAL,
                    PipeType.EAST_SOUTH,
                    PipeType.SOUTH_WEST,
                )

                NeighborPosition.EAST -> false
                NeighborPosition.SOUTH -> neighborPipe.type in setOf(
                    PipeType.VERTICAL,
                    PipeType.NORTH_EAST,
                    PipeType.WEST_NORTH,
                )

                NeighborPosition.WEST -> false
            }

            PipeType.HORIZONTAL -> when (neighborPosition) {
                NeighborPosition.NORTH -> false
                NeighborPosition.EAST -> neighborPipe.type in setOf(
                    PipeType.HORIZONTAL,
                    PipeType.SOUTH_WEST,
                    PipeType.WEST_NORTH,
                )

                NeighborPosition.SOUTH -> false
                NeighborPosition.WEST -> neighborPipe.type in setOf(
                    PipeType.HORIZONTAL,
                    PipeType.NORTH_EAST,
                    PipeType.EAST_SOUTH,
                )
            }

            PipeType.NORTH_EAST -> when (neighborPosition) {
                NeighborPosition.NORTH -> neighborPipe.type in setOf(
                    PipeType.VERTICAL,
                    PipeType.EAST_SOUTH,
                    PipeType.SOUTH_WEST,
                )

                NeighborPosition.EAST -> neighborPipe.type in setOf(
                    PipeType.HORIZONTAL,
                    PipeType.SOUTH_WEST,
                    PipeType.WEST_NORTH,
                )

                NeighborPosition.SOUTH -> false
                NeighborPosition.WEST -> false
            }

            PipeType.EAST_SOUTH -> when (neighborPosition) {
                NeighborPosition.NORTH -> false
                NeighborPosition.EAST -> neighborPipe.type in setOf(
                    PipeType.HORIZONTAL,
                    PipeType.SOUTH_WEST,
                    PipeType.WEST_NORTH,
                )

                NeighborPosition.SOUTH -> neighborPipe.type in setOf(
                    PipeType.VERTICAL,
                    PipeType.NORTH_EAST,
                    PipeType.WEST_NORTH,
                )

                NeighborPosition.WEST -> false
            }

            PipeType.SOUTH_WEST -> when (neighborPosition) {
                NeighborPosition.NORTH -> false
                NeighborPosition.EAST -> false
                NeighborPosition.SOUTH -> neighborPipe.type in setOf(
                    PipeType.VERTICAL,
                    PipeType.NORTH_EAST,
                    PipeType.WEST_NORTH,
                )

                NeighborPosition.WEST -> neighborPipe.type in setOf(
                    PipeType.HORIZONTAL,
                    PipeType.NORTH_EAST,
                    PipeType.EAST_SOUTH,
                )
            }

            PipeType.WEST_NORTH -> when (neighborPosition) {
                NeighborPosition.NORTH -> neighborPipe.type in setOf(
                    PipeType.VERTICAL,
                    PipeType.EAST_SOUTH,
                    PipeType.SOUTH_WEST,
                )

                NeighborPosition.EAST -> false
                NeighborPosition.SOUTH -> false
                NeighborPosition.WEST -> neighborPipe.type in setOf(
                    PipeType.HORIZONTAL,
                    PipeType.NORTH_EAST,
                    PipeType.EAST_SOUTH,
                )
            }

            PipeType.ANY -> {
                when (neighborPosition) {
                    NeighborPosition.NORTH -> neighborPipe.type in setOf(
                        PipeType.VERTICAL,
                        PipeType.EAST_SOUTH,
                        PipeType.SOUTH_WEST,
                    )

                    NeighborPosition.EAST -> neighborPipe.type in setOf(
                        PipeType.HORIZONTAL,
                        PipeType.SOUTH_WEST,
                        PipeType.WEST_NORTH,
                    )

                    NeighborPosition.SOUTH -> neighborPipe.type in setOf(
                        PipeType.VERTICAL,
                        PipeType.NORTH_EAST,
                        PipeType.WEST_NORTH,
                    )

                    NeighborPosition.WEST -> neighborPipe.type in setOf(
                        PipeType.HORIZONTAL,
                        PipeType.NORTH_EAST,
                        PipeType.EAST_SOUTH,
                    )
                }
            }

            PipeType.NONE -> false
        }
    }

    fun Sequence<String>.getStepsToFarthestPipe(): Int {
        return toGrid()
            .transformValues {
                StepCount(
                    pipe = it.value,
                )
            }
            .let { grid ->
                val startingCell = grid.single { it.value.pipe.pipeSymbol == 'S' }

                var currentSteps = 0
                startingCell.value.stepCount = currentSteps

                var nextCell =
                    grid.getNeighborsThanCanAccess(startingCell).firstOrNull { it.value.stepCount == null }
                while (nextCell != null) {
                    currentSteps++
                    nextCell.value.stepCount = currentSteps

                    nextCell = grid.getNeighborsThanCanAccess(nextCell).firstOrNull { it.value.stepCount == null }
                }

                (currentSteps + 1) / 2
            }
    }
}
