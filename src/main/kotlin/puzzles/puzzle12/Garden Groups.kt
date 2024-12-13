package me.salzinger.puzzles.puzzle12

import me.salzinger.common.Grid2D
import me.salzinger.common.extensions.toGrid2D
import me.salzinger.common.geometry.Direction
import me.salzinger.common.geometry.LazyGrid2D
import me.salzinger.common.geometry.rotate90DegreesClockwise
import me.salzinger.common.geometry.rotate90DegreesCounterclockwise
import me.salzinger.common.nextIn

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

    fun Set<Grid2D.Cell<Char>>.getSides(): Int {
        val regionPlotCoordinates = map { it.coordinate }.toSet()

        val regionGrid = LazyGrid2D(
            valuesProvider = {
                it in regionPlotCoordinates
            },
            boundaryProvider = {
                LazyGrid2D.Boundary(
                    minRow = minOf { it.coordinate.row - 1 },
                    maxRow = maxOf { it.coordinate.row + 1 },
                    minColumn = minOf { it.coordinate.column - 1 },
                    maxColumn = maxOf { it.coordinate.column + 1 },
                )
            },
            neighborProvider = Grid2D.Coordinate.NeighborModes.CROSS,
        )

        var sides = 0L

        val candidateStartingPlots =
            regionGrid
                .filter {it.value && !regionGrid.getCellAt(it.coordinate.up()).value}
                .toMutableSet()

        while (candidateStartingPlots.isNotEmpty()) {
            // we traverse the border clockwise, starting at the leftmost plot in the top row
            // the actual position can always be considered to be the top-left corner of a plot relative to the current direction
            val startingPlot = candidateStartingPlots.first()
            val startingDirection = Direction.RIGHT

            var currentPlot = startingPlot
            var currentDirection = startingDirection
            var ccwDirection = currentDirection.rotate90DegreesCounterclockwise()

            var nextInDirection = regionGrid.getCellAt(currentPlot.coordinate.nextIn(currentDirection))
            do {
                if (currentDirection == startingDirection) {
                    candidateStartingPlots -= currentPlot
                }
                if (nextInDirection.value) {
                    // still inside the region
                    // can a CCW turn be made?
                    val ccwPosition =
                        regionGrid.getCellAt(nextInDirection.coordinate.nextIn(ccwDirection))

                    if (ccwPosition.value) {
                        // we can make a left turn, this side is done
                        sides++
                        currentPlot = ccwPosition
                        currentDirection = ccwDirection
                        ccwDirection = ccwDirection.rotate90DegreesCounterclockwise()
                    } else {
                        // we can move forward in the same direction, no new side
                        currentPlot = nextInDirection
                    }
                } else {
                    // moving straight is not an option, we turn cw instead
                    sides++
                    ccwDirection = currentDirection
                    currentDirection = currentDirection.rotate90DegreesClockwise()
                }

                nextInDirection = regionGrid.getCellAt(currentPlot.coordinate.nextIn(currentDirection))

            } while (nextInDirection.coordinate != startingPlot.coordinate.nextIn(startingDirection) || currentDirection != startingDirection)
        }


        return sides.toInt()
    }

    fun Sequence<String>.getFenceTotalPriceForAllRegionsWithBulkDiscount(): Long {
        val map = toGrid2D(neighborProvider = Grid2D.Coordinate.NeighborModes.CROSS)

        val unassignedPlots = map.toMutableSet()

        val regions = mutableSetOf<Pair<Set<Grid2D.Cell<Char>>, Int>>()

        while (unassignedPlots.isNotEmpty()) {
            val startingPlot = unassignedPlots.first()
            unassignedPlots -= startingPlot

            var area = 0

            var currentPlots = setOf(startingPlot)
            val currentRegion = currentPlots.toMutableSet()

            while (currentPlots.isNotEmpty()) {
                currentPlots = currentPlots.flatMap { currentPlot ->
                    currentRegion += currentPlot
                    unassignedPlots -= currentPlot
                    area++
                    map.getNeighborsOf(currentPlot)
                        .filter {
                            it.value == currentPlot.value && it !in currentRegion
                        }
                }.toSet()
            }

            regions.add(currentRegion to area)
        }

        return regions.sumOf { (currentRegion, area) -> currentRegion.getSides() * area.toLong() }
    }
}