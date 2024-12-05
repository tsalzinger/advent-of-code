package me.salzinger.puzzles.puzzle5

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle5.PrintQueue.sumOfMiddlePageOfSortedUpdates

fun main() {
    "puzzles/puzzle5/puzzle-5.in"
        .streamInput()
        .sumOfMiddlePageOfSortedUpdates()
        .also(::println)
}