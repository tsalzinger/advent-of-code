package me.salzinger.puzzles.puzzle16

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle16.TheFloorWillBeLava.getCountOfEnergizedTiles

fun main() {
    "puzzles/puzzle16/puzzle-16.in"
        .streamInput()
        .getCountOfEnergizedTiles()
        .also(::println)
}
