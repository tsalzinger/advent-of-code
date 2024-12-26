package me.salzinger.puzzles.puzzle22

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle22.`Monkey Market`.sumOf2000thSecretNumber

fun main() {
    "puzzles/puzzle22/puzzle-22.in"
        .streamInput()
        .sumOf2000thSecretNumber()
        .also(::println)
}