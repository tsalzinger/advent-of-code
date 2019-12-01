package me.salzinger

import java.io.File

fun Double.floor() = kotlin.math.floor(this)

enum class FileType(val extension: String) {
    IN("in"),
    OUT("out")
}

fun getFile(level: Int, fileType: FileType) = File("src/main/resources/puzzle-$level.${fileType.extension}")

fun getPuzzleInput(level: Int) =
    getFile(level, FileType.IN)
        .readLines()
        .map(String::trim)
        .filter(String::isNotEmpty)

fun <T> List<T>.writePuzzleSolution(level: Int) =
    getFile(level, FileType.OUT).writeText(joinToString("\n"))

fun Int.writePuzzleSolution(level: Int) = listOf(this).writePuzzleSolution(level)
fun String.writePuzzleSolution(level: Int) = listOf(this).writePuzzleSolution(level)