package me.salzinger

import kotlin.math.absoluteValue

fun main() {
    7.solve(2) {
        val startPositions = flatMap { it.toIntList() }
            .sorted()

        val targetCandidates = startPositions.first()..startPositions.last()

        targetCandidates
            .minOf { targetCandidate ->
                startPositions.sumOf { startPosition ->
                    getFuelCost(startPosition, targetCandidate)
                }
            }
            .toString()
    }
}

private fun getFuelCost(startPosition: Int, targetPosition: Int): Int {
    return (1..(startPosition - targetPosition).absoluteValue).sum()
}
