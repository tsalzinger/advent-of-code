package me.salzinger.puzzles.puzzle20

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle20.`Race Condition`.countAvailableCheatsSavingAtLeast100PicosecondsWithUpdatedRules

fun main() {
    "puzzles/puzzle20/puzzle-20.in"
        .streamInput()
        .countAvailableCheatsSavingAtLeast100PicosecondsWithUpdatedRules()
        .also(::println)
}