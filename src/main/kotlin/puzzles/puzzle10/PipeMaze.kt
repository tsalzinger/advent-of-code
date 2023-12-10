package me.salzinger.puzzles.puzzle10

import me.salzinger.common.Grid2D

object PipeMaze {

    enum class PipeType(val pipeSymbol: Char) {
        VERTICAL('|'),
        HORIZONTAL('-'),
        NORTH_EAST('L'),
        EAST_SOUTH('F'),
        SOUTH_WEST('7'),
        WEST_NORTH('J'),
        ANY('S'),
        NONE('.')
    }

    fun PipeType.toPipe(): Pipe {
        return Pipe(pipeSymbol)
    }

    enum class NeighborPosition {
        NORTH,
        EAST,
        SOUTH,
        WEST,
    }


    class Pipe(val pipeSymbol: Char) {
        val type = PipeType.entries.single { it.pipeSymbol == pipeSymbol }
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
            .identifyLoop()
            .let { (_, loopLength) -> (loopLength + 1) / 2 }
    }

    fun Grid2D<StepCount>.identifyLoop(): Pair<Grid2D<StepCount>, Int> {
        val startingCell = single { it.value.pipe.pipeSymbol == 'S' }

        var currentSteps = 0
        startingCell.value.stepCount = currentSteps

        var nextCell =
            getNeighborsThanCanAccess(startingCell).firstOrNull { it.value.stepCount == null }
        while (nextCell != null) {
            currentSteps++
            nextCell.value.stepCount = currentSteps

            nextCell = getNeighborsThanCanAccess(nextCell).firstOrNull { it.value.stepCount == null }
        }

        return this to currentSteps
    }

    fun Sequence<String>.getTilesCountContainedWithinLoop(): Int {
        return toGrid()
            .transformValues {
                StepCount(
                    pipe = it.value,
                )
            }
            .identifyLoop()
            .let { (grid, _) ->
                grid.transformValues { cell ->
                    if (cell.value.pipe.type == PipeType.ANY) {
                        val coordinate = cell.coordinate
                        val neighborCoordinates = grid.getNeighborsThanCanAccess(cell).map {
                            it.coordinate
                        }

                        when {
                            neighborCoordinates.containsAll(
                                setOf(
                                    coordinate.up(),
                                    coordinate.down()
                                )
                            ) -> PipeType.VERTICAL

                            neighborCoordinates.containsAll(
                                setOf(
                                    coordinate.left(),
                                    coordinate.right()
                                )
                            ) -> PipeType.HORIZONTAL

                            neighborCoordinates.containsAll(
                                setOf(
                                    coordinate.up(),
                                    coordinate.right()
                                )
                            ) -> PipeType.NORTH_EAST

                            neighborCoordinates.containsAll(
                                setOf(
                                    coordinate.right(),
                                    coordinate.down()
                                )
                            ) -> PipeType.EAST_SOUTH

                            neighborCoordinates.containsAll(
                                setOf(
                                    coordinate.down(),
                                    coordinate.left()
                                )
                            ) -> PipeType.SOUTH_WEST

                            neighborCoordinates.containsAll(
                                setOf(
                                    coordinate.left(),
                                    coordinate.up()
                                )
                            ) -> PipeType.WEST_NORTH

                            else -> throw RuntimeException("Failed to map start tile to specific pipe")
                        }
                    } else if (cell.value.stepCount == null) {
                        PipeType.NONE
                    } else {
                        cell.value.pipe.type
                    }
                }
            }
            .expand()
            .toTileGrid()
            .identifyOuter()
            .reduce()
            .let { grid ->
                grid.count {
                    it.value is Tile.Floor && it.value.state == Tile.State.UNDETERMINED
                }
            }
    }

    fun Grid2D<Tile>.reduce(): Grid2D<Tile> {
        val expandedRows = chunked(columns)

        val reducedRows = expandedRows.drop(2).filterIndexed { index, cells ->
            index % 3 == 0
        }.map {
            it.drop(2).filterIndexed { index, cell -> index % 3 == 0 }
                .map {
                    it.value
                }
        }


        return Grid2D(reducedRows)
    }

    sealed interface Tile {
        data class Pipe(val pipeType: PipeType) : Tile {
            override fun toString(): String {
                return pipeType.pipeSymbol.toString()
            }
        }

        class Floor(var state: State) : Tile {
            override fun toString(): String {
                return when (state) {
                    State.UNDETERMINED -> "."
                    State.IN -> "I"
                    State.OUT -> "O"
                }
            }
        }

        enum class State {
            UNDETERMINED,
            IN,
            OUT,
        }
    }

    fun Grid2D<PipeType>.toTileGrid(): Grid2D<Tile> {
        return transformValues {
            when (it.value) {
                PipeType.NONE -> Tile.Floor(Tile.State.UNDETERMINED)
                else -> Tile.Pipe(it.value)
            }
        }
    }


    fun Grid2D<PipeType>.expand(): Grid2D<PipeType> {
        val expanded = mutableListOf<MutableList<PipeType>>()

        forEachIndexed { index, cell ->
            if (index % columns == 0) {
                expanded.add(mutableListOf())
                expanded.add(mutableListOf())
                expanded.add(mutableListOf())
            }

            val first = expanded[expanded.count() - 3]
            val second = expanded[expanded.count() - 2]
            val third = expanded[expanded.count() - 1]


            when (cell.value) {
                PipeType.VERTICAL -> {
                    first.addAll(listOf(PipeType.NONE, PipeType.VERTICAL, PipeType.NONE))
                    second.addAll(listOf(PipeType.NONE, PipeType.VERTICAL, PipeType.NONE))
                    third.addAll(listOf(PipeType.NONE, PipeType.VERTICAL, PipeType.NONE))
                }

                PipeType.HORIZONTAL -> {
                    first.addAll(listOf(PipeType.NONE, PipeType.NONE, PipeType.NONE))
                    second.addAll(listOf(PipeType.HORIZONTAL, PipeType.HORIZONTAL, PipeType.HORIZONTAL))
                    third.addAll(listOf(PipeType.NONE, PipeType.NONE, PipeType.NONE))
                }

                PipeType.NORTH_EAST -> {
                    first.addAll(listOf(PipeType.NONE, PipeType.VERTICAL, PipeType.NONE))
                    second.addAll(listOf(PipeType.NONE, PipeType.NORTH_EAST, PipeType.HORIZONTAL))
                    third.addAll(listOf(PipeType.NONE, PipeType.NONE, PipeType.NONE))
                }

                PipeType.EAST_SOUTH -> {
                    first.addAll(listOf(PipeType.NONE, PipeType.NONE, PipeType.NONE))
                    second.addAll(listOf(PipeType.NONE, PipeType.EAST_SOUTH, PipeType.HORIZONTAL))
                    third.addAll(listOf(PipeType.NONE, PipeType.VERTICAL, PipeType.NONE))
                }

                PipeType.SOUTH_WEST -> {
                    first.addAll(listOf(PipeType.NONE, PipeType.NONE, PipeType.NONE))
                    second.addAll(listOf(PipeType.HORIZONTAL, PipeType.SOUTH_WEST, PipeType.NONE))
                    third.addAll(listOf(PipeType.NONE, PipeType.VERTICAL, PipeType.NONE))
                }

                PipeType.WEST_NORTH -> {
                    first.addAll(listOf(PipeType.NONE, PipeType.VERTICAL, PipeType.NONE))
                    second.addAll(listOf(PipeType.HORIZONTAL, PipeType.WEST_NORTH, PipeType.NONE))
                    third.addAll(listOf(PipeType.NONE, PipeType.NONE, PipeType.NONE))
                }

                PipeType.ANY -> {
                    first.addAll(listOf(PipeType.NONE, PipeType.ANY, PipeType.NONE))
                    second.addAll(listOf(PipeType.ANY, PipeType.ANY, PipeType.ANY))
                    third.addAll(listOf(PipeType.NONE, PipeType.ANY, PipeType.NONE))
                }

                PipeType.NONE -> {
                    first.addAll(listOf(PipeType.NONE, PipeType.NONE, PipeType.NONE))
                    second.addAll(listOf(PipeType.NONE, PipeType.NONE, PipeType.NONE))
                    third.addAll(listOf(PipeType.NONE, PipeType.NONE, PipeType.NONE))
                }
            }
        }

        expanded.forEach {
            it.add(0, PipeType.NONE)
            it.add(PipeType.NONE)
        }

        expanded.add(0, MutableList((columns * 3) + 2) {
            PipeType.NONE
        })

        expanded.add(MutableList((columns * 3) + 2) {
            PipeType.NONE
        })

        return Grid2D(expanded)
    }


    private fun <T : Tile> Grid2D<Tile>.getUndeterminedNeighborsOf(cell: Grid2D.Cell<T>): List<Grid2D.Cell<Tile.Floor>> {
        return getNeighborsOf(cell).filter {
            it.value is Tile.Floor && it.value.state == Tile.State.UNDETERMINED
        }.map { it as Grid2D.Cell<Tile.Floor> }
    }

    private fun Grid2D<Tile>.identifyOuter(): Grid2D<Tile> {
        val startingCell = getCellAt(Grid2D.Coordinate(0, 0))

        startingCell.value as Tile.Floor

        startingCell.value.state = Tile.State.OUT

        var undeterminedNeighbors = getUndeterminedNeighborsOf(startingCell)

        while (undeterminedNeighbors.isNotEmpty()) {
            undeterminedNeighbors.forEach {
                it.value.state = Tile.State.OUT
            }

            undeterminedNeighbors = undeterminedNeighbors
                .flatMap {
                    getUndeterminedNeighborsOf(it)
                }.distinctBy { it.coordinate }
        }


        return this
    }
}

