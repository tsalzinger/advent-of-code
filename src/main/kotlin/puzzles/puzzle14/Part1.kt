package me.salzinger.puzzles.puzzle14

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle14.ParabolicReflectorDish.getTotalLoadOnNorthBeamAfterATiltToNorth

fun main() {
    "puzzles/puzzle14/puzzle-14.in"
        .streamInput()
        .getTotalLoadOnNorthBeamAfterATiltToNorth()
        .also(::println)
}
