package me.salzinger.puzzles.puzzle5

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle5.IfYouGiveASeedAFertilizer.toAlmanac

fun main() {
    "puzzles/puzzle5/puzzle-5.in"
        .streamInput()
        .toAlmanac()
        .getLastCategoryNumbers()
        .min()
        .also(::println)
}
