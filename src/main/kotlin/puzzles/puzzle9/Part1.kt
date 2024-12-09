package me.salzinger.puzzles.puzzle9

import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle9.`Disk Fragmenter`.compactedFilesystemChecksum

fun main() {
    "puzzles/puzzle9/puzzle-9.in"
        .streamInput()
        .compactedFilesystemChecksum()
        .also(::println)
}