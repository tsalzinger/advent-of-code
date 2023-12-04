package me.salzinger.puzzles.puzzle3

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle3.GearRatios.getSumOfAllNumbersAdjacentToASymbol
import me.salzinger.puzzles.puzzle3.GearRatios.parseAsGameGrid

fun main() {
    "puzzles/puzzle3/puzzle-3.in"
        .streamInput()
        .parseAsGameGrid()
        .getSumOfAllNumbersAdjacentToASymbol()
        .also(::println)
}
