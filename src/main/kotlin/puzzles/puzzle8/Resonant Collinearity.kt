package me.salzinger.puzzles.puzzle8

import me.salzinger.common.Grid2D
import me.salzinger.common.extensions.permutate
import me.salzinger.common.extensions.toGrid2D
import me.salzinger.common.geometry.Point2D

object `Resonant Collinearity` {
    fun Sequence<String>.countUniqueAntinodeLocations(): Int {
        return getUniqueCount { (p1, p2) ->
            val diff = p1.vectorTo(p2)

            listOf(
                p1 - diff,
                p2 + diff,
            ).filter { it in this }
        }
    }

    fun Sequence<String>.countUniqueAntinodeLocationsWithResonantHarmonics(): Int {
        return getUniqueCount { (p1, p2) ->
            val diff = p1.vectorTo(p2)
            var antinodes = mutableListOf<Point2D>()
            var mutliplier = 0
            do {
                var nextAntinodes = listOf(
                    p1 - diff * mutliplier,
                    p2 + diff * mutliplier,
                ).filter { it in this }
                mutliplier++
                antinodes += nextAntinodes
            } while (nextAntinodes.isNotEmpty())

            antinodes
        }
    }

    private fun Sequence<String>.getUniqueCount(
        antinodeLocationProvider: Grid2D<Char>.(points: Pair<Point2D, Point2D>) -> List<Point2D>
    ): Int {
        val grid = toGrid2D()
        val antennaLocations = grid
            .filter { cell -> cell.value != '.' }
            .map { cell ->
                cell.value to cell.coordinate
            }
            .groupBy({ it.first }, { it.second.toPoint2D() })

        return antennaLocations
            .asSequence()
            .flatMap {
                it.value
                    .permutate()
            }
            .flatMap {
                grid.antinodeLocationProvider(it)
            }
            .toSet()
            .size
    }

    fun Grid2D.Coordinate.toPoint2D() = Point2D(x = column, y = row)
    fun Point2D.toCoordinate() = Grid2D.Coordinate(row = y, column = x)
    operator fun <T> Grid2D<T>.contains(point2D: Point2D) = point2D.toCoordinate() in this
}