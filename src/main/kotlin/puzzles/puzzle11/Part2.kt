package me.salzinger.puzzles.puzzle11

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle11.CosmicExpansion.getSumOfGalaxyPairDistances

fun main() {
    "puzzles/puzzle11/puzzle-11.in"
        .streamInput()
        .getSumOfGalaxyPairDistances(1_000_000)
        .also(::println)
}
