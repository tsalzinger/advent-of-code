package me.salzinger.puzzles.puzzle18

import me.salzinger.common.Grid2D
import me.salzinger.common.geometry.Direction
import me.salzinger.common.geometry.LazyGrid2D
import me.salzinger.common.geometry.invoke

object LavaductLagoon {

    data class Instruction(
        val direction: Direction,
        val count: Int,
        val color: String,
    ) {
        fun asSequence(): Sequence<Pair<Direction, String>> {
            return sequence {
                repeat(count) {
                    yield(direction to color)
                }
            }
        }

        companion object {
            fun of(
                direction: Direction,
                count: Int,
                color: String,
            ): Instruction {
                return Instruction(
                    direction = direction,
                    count = count,
                    color = color.trim('(', ')')
                )
            }
        }
    }

    class Tile(
        var excavated: Boolean = false,
        var color: String? = null,
    )

    fun String.toInstruction(): Instruction {
        val (direction, count, color) = split(" ")

        return Instruction.of(
            direction = when (direction) {
                "U" -> Direction.UP
                "R" -> Direction.RIGHT
                "D" -> Direction.DOWN
                "L" -> Direction.LEFT
                else -> throw RuntimeException("Unkown direction $direction")
            },
            count = count.toInt(),
            color = color
        )
    }


    fun Sequence<String>.toInstructions(): Sequence<Instruction> {
        return map { it.toInstruction() }
    }

    fun Sequence<String>.getLagoonSize(): Int {
        val tiles = mutableMapOf<Grid2D.Coordinate, Tile>()
        val instructions = toInstructions()

        instructions.flatMap {
            it.asSequence()
        }.fold(Grid2D.Coordinate(0, 0)) { currentCoordinate, (direction, color) ->
            tiles[currentCoordinate] = Tile(excavated = true, color = color)
            currentCoordinate(direction)
        }

        val coordinates = tiles.keys

        val boundary = LazyGrid2D.Boundary(
            minRow = coordinates.minOf { it.row } - 1,
            maxRow = coordinates.maxOf { it.row } + 1,
            minColumn = coordinates.minOf { it.column } - 1,
            maxColumn = coordinates.maxOf { it.column } + 1,
        )

        var current = listOf(Grid2D.Coordinate(boundary.minRow, boundary.minColumn))

        while (current.isNotEmpty()) {
            current = current.flatMap {
                tiles[it] = Tile()
                it.getNeighbors(Grid2D.Coordinate.NeighborModes.RING)
            }.distinct().filter {
                it in boundary && it !in tiles
            }
        }

        val grid = LazyGrid2D(
            valuesProvider = {
                tiles[it] ?: Tile(excavated = true)
            },
            boundaryProvider = {
                boundary
            }
        )

        return grid.count {
            it.value.excavated
        }
    }
}
