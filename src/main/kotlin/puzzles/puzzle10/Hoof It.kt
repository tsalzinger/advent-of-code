package me.salzinger.puzzles.puzzle10

import me.salzinger.common.Grid2D
import me.salzinger.common.extensions.toGrid2D

object `Hoof It` {
    fun Sequence<String>.sumOfAllTrailheadScores(): Int {
        val topographicMap = toGrid2D(neighborProvider = Grid2D.Coordinate.NeighborModes.CROSS)
            .transformValues { "${it.value}".toInt() }

        val trailheadCandidates = topographicMap
            .filter { it.value == 0 }

        return List(9) {}
            .fold(trailheadCandidates.map { listOf(it) }) { currentTrailEndsPerTrailhead, _ ->
                currentTrailEndsPerTrailhead
                    .map { currentTrailEnds ->
                        currentTrailEnds
                            .flatMap {
                                topographicMap
                                    .getNeighborsOf(it)
                                    .filter { neighbor -> neighbor.value == it.value + 1 }
                            }
                            .toSet()
                            .toList()
                    }
            }
            .flatten()
            .count()
    }
}