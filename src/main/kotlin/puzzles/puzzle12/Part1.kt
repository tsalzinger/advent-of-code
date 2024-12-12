package me.salzinger.puzzles.puzzle12

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle12.`Garden Groups`.getFenceTotalPriceForAllRegions

fun main() {
    "puzzles/puzzle12/puzzle-12.in"
        .streamInput()
        .getFenceTotalPriceForAllRegions()
        .also(::println)
}