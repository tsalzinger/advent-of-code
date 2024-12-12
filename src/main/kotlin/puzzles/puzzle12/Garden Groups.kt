package me.salzinger.puzzles.puzzle12

import me.salzinger.common.Grid2D
import me.salzinger.common.extensions.toGrid2D

object `Garden Groups` {
    fun Sequence<String>.getFenceTotalPriceForAllRegions(): Long {
        val map = toGrid2D(neighborProvider = Grid2D.Coordinate.NeighborModes.CROSS)

        val unassignedPlots = map.toMutableSet()

        val regions = mutableListOf<Pair<Int, Int>>()

        while (unassignedPlots.isNotEmpty()) {
            val startingPlot = unassignedPlots.first()
            unassignedPlots -= startingPlot

            var area = 0
            var perimeter = 0

            var currentPlots = setOf(startingPlot)
            val currentRegion = currentPlots.toMutableSet()

            while (currentPlots.isNotEmpty()) {
                currentPlots = currentPlots.flatMap { currentPlot ->
                    currentRegion += currentPlot
                    unassignedPlots -= currentPlot
                    area++
                    val neighborPlotsInRegion = map.getNeighborsOf(currentPlot)
                        .filter {
                            it.value == currentPlot.value
                        }

                    perimeter += 4 - neighborPlotsInRegion.size

                    neighborPlotsInRegion
                }.filter { it !in currentRegion }.toSet()
            }

            regions.add(area to perimeter)
        }

        return regions.sumOf { (area, perimeter) -> area * perimeter.toLong() }
    }
}