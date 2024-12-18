package me.salzinger.puzzles.puzzle18

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle18.`RAM Run`.getMinNumberOfStepsToReachExitAfter1024BytesHaveFallen

fun main() {
    "puzzles/puzzle18/puzzle-18.in"
        .streamInput()
        .getMinNumberOfStepsToReachExitAfter1024BytesHaveFallen()
        .also(::println)
}