package me.salzinger.puzzles.puzzle4

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle4.CeresSearch.`countOccurrencesOfX-mas`
import me.salzinger.puzzles.puzzle4.CeresSearch.countOccurrencesOfXmas

fun main() {
    "puzzles/puzzle4/puzzle-4.in"
        .streamInput()
        .`countOccurrencesOfX-mas`()
        .also(::println)
}
