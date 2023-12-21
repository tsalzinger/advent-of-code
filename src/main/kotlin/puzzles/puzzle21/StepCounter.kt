package me.salzinger.puzzles.puzzle21

import me.salzinger.common.Grid2D
import me.salzinger.puzzles.puzzle21.StepCounter.Tile.Companion.toTile

object StepCounter {
    sealed interface Tile {
        val canAccess: Boolean

        data object Start : Tile {
            override val canAccess = true
        }

        data object Garden : Tile {
            override val canAccess = true
        }

        data object Rock : Tile {
            override val canAccess = false
        }

        companion object {
            fun Char.toTile(): Tile {
                return when (this) {
                    '.' -> Garden
                    '#' -> Rock
                    'S' -> Start
                    else -> throw RuntimeException("Unknown type $this")
                }
            }
        }
    }

    fun Sequence<String>.toGrid(): Grid2D<Tile> {
        return map { row ->
            row.map { it.toTile() }
        }.toList().run(::Grid2D)
    }

    fun Sequence<String>.getNumberOfReachableTilesAfterSteps(stepCount: Int): Int {
        require(stepCount % 2 == 0)

        val grid = toGrid()
        val startTile = grid.single { it.value is Tile.Start }

        val visitedTiles = mutableSetOf<Grid2D.Cell<Tile>>()
        var finalTilesCount = 1
        var currentTiles = listOf(startTile)

        repeat(stepCount) { step ->
            visitedTiles += currentTiles

            currentTiles =
                currentTiles
                    .flatMap {
                        grid.getNeighborsOf(it)
                    }
                    .filter {
                        it.value.canAccess
                    }
                    .distinct()
                    .filter {
                        it !in visitedTiles
                    }

            if ((step + 1) % 2 == 0) {
                finalTilesCount += currentTiles.count()
            }
        }

        return finalTilesCount
    }
}
