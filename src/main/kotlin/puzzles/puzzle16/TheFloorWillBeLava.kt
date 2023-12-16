package me.salzinger.puzzles.puzzle16

import me.salzinger.common.Grid2D
import me.salzinger.puzzles.puzzle16.TheFloorWillBeLava.Tile.Companion.energize
import me.salzinger.puzzles.puzzle16.TheFloorWillBeLava.Tile.Companion.toTile

object TheFloorWillBeLava {
    enum class Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT,
    }

    operator fun Grid2D.Coordinate.invoke(direction: Direction): Grid2D.Coordinate {
        return when (direction) {
            Direction.UP -> up()
            Direction.RIGHT -> right()
            Direction.DOWN -> down()
            Direction.LEFT -> left()
        }
    }

    sealed class Tile(val representation: Char) {
        var energized: Boolean = false


        override fun toString(): String {
            return "$representation"
        }

        abstract fun getBeamOutputDirections(beamInputDirection: Direction): List<Direction>

        companion object {
            fun <T : Tile> T.energize(): T {
                energized = true
                return this
            }

            fun Char.toTile(): Tile {
                return when (this) {
                    '.' -> Empty()
                    '/' -> Mirror.UpRight()
                    '\\' -> Mirror.DownRight()
                    '|' -> Splitter.Vertical()
                    '-' -> Splitter.Horizontal()
                    else -> throw RuntimeException("Failed to map $this to a tile")
                }
            }
        }

        class Empty : Tile('.') {
            override fun getBeamOutputDirections(beamInputDirection: Direction): List<Direction> {
                return listOf(beamInputDirection)
            }
        }

        sealed class Splitter(representation: Char) : Tile(representation) {
            class Vertical : Splitter('|') {
                override fun getBeamOutputDirections(beamInputDirection: Direction): List<Direction> {
                    return when (beamInputDirection) {
                        Direction.UP -> listOf(Direction.UP)
                        Direction.RIGHT -> listOf(Direction.UP, Direction.DOWN)
                        Direction.DOWN -> listOf(Direction.DOWN)
                        Direction.LEFT -> listOf(Direction.UP, Direction.DOWN)
                    }
                }
            }

            class Horizontal : Splitter('-') {
                override fun getBeamOutputDirections(beamInputDirection: Direction): List<Direction> {
                    return when (beamInputDirection) {
                        Direction.UP -> listOf(Direction.LEFT, Direction.RIGHT)
                        Direction.RIGHT -> listOf(Direction.RIGHT)
                        Direction.DOWN -> listOf(Direction.LEFT, Direction.RIGHT)
                        Direction.LEFT -> listOf(Direction.LEFT)
                    }
                }
            }
        }

        sealed class Mirror(representation: Char) : Tile(representation) {
            class UpRight : Mirror('/') {
                override fun getBeamOutputDirections(beamInputDirection: Direction): List<Direction> {
                    return when (beamInputDirection) {
                        Direction.UP -> listOf(Direction.RIGHT)
                        Direction.RIGHT -> listOf(Direction.UP)
                        Direction.DOWN -> listOf(Direction.LEFT)
                        Direction.LEFT -> listOf(Direction.DOWN)
                    }
                }
            }

            class DownRight : Mirror('\\') {
                override fun getBeamOutputDirections(beamInputDirection: Direction): List<Direction> {
                    return when (beamInputDirection) {
                        Direction.UP -> listOf(Direction.LEFT)
                        Direction.RIGHT -> listOf(Direction.DOWN)
                        Direction.DOWN -> listOf(Direction.RIGHT)
                        Direction.LEFT -> listOf(Direction.UP)
                    }
                }
            }
        }
    }

    fun String.toTiles(): List<Tile> {
        return map {
            it.toTile()
        }
    }

    fun Sequence<String>.toTiles(): Sequence<List<Tile>> {
        return map {
            it.toTiles()
        }
    }

    fun Grid2D<Tile>.countEnergizedTiles(startingDirection: Direction, startingCoordinate: Grid2D.Coordinate): Int {
        var nextConfiguration = listOf(
            startingDirection to startingCoordinate
        )
        val alreadyEvaluated = mutableSetOf<Pair<Direction, Grid2D.Coordinate>>()

        while (nextConfiguration.isNotEmpty()) {
            nextConfiguration = nextConfiguration
                .flatMap { (direction, coordinate) ->
                    this[coordinate].value
                        .energize()
                        .getBeamOutputDirections(direction)
                        .map { newDirection ->
                            newDirection to coordinate(newDirection)
                        }
                }
                .filter { (_, coordinate) -> coordinate in this }
                .filter { it !in alreadyEvaluated }
                .distinct()
                .also(alreadyEvaluated::addAll)
        }

        return count {
            it.value.energized
        }
    }

    fun Grid2D<Tile>.reset(): Grid2D<Tile> {
        forEach { it.value.energized = false }
        return this
    }

    fun Sequence<String>.getCountOfEnergizedTiles(): Int {
        return toTiles()
            .toList()
            .run(::Grid2D)
            .countEnergizedTiles(Direction.RIGHT, Grid2D.Coordinate(0, 0))
    }

    fun Sequence<String>.getMaxCountOfEnergizedTiles(): Int {
        val grid = toTiles()
            .toList()
            .run(::Grid2D)

        val startingConfigurations =
            grid.rowsRange.flatMap { row ->
                listOf(
                    Direction.RIGHT to Grid2D.Coordinate(row, 0),
                    Direction.LEFT to Grid2D.Coordinate(row, grid.lastColumn),
                )
            } + grid.columnsRange.flatMap { column ->
                listOf(
                    Direction.DOWN to Grid2D.Coordinate(0, column),
                    Direction.UP to Grid2D.Coordinate(grid.lastRow, column),
                )
            }

        return startingConfigurations
            .map { (direction, coordinate) ->
                grid.countEnergizedTiles(direction, coordinate)
                    .also { grid.reset() }
            }
            .max()
    }
}
