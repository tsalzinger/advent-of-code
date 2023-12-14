package me.salzinger.puzzles.puzzle14

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle14.ParabolicReflectorDish.getTotalLoadOnNorthBeamAfterSpins

fun main() {
    "puzzles/puzzle14/puzzle-14.in"
        .streamInput()
        .getTotalLoadOnNorthBeamAfterSpins(1_000_000_000)
        .also(::println)
}
