package me.salzinger.puzzles.puzzle16

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle16.TheFloorWillBeLava.getMaxCountOfEnergizedTiles

fun main() {
    "puzzles/puzzle16/puzzle-16.in"
        .streamInput()
        .getMaxCountOfEnergizedTiles()
        .also(::println)
}
