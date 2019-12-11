package me.salzinger

import java.io.File
import kotlin.math.abs
import kotlin.math.sqrt

fun Double.floor() = kotlin.math.floor(this)

fun <T> List<T>.countOccurrences(value: T) = this.count { it == value }

enum class FileType(val extension: String) {
    IN("in"),
    OUT("out")
}

fun getFile(level: Int, fileType: FileType) = File("src/main/resources/puzzle-$level.${fileType.extension}")

fun getNextInput(puzzle: Int) =
    getFile(puzzle, FileType.IN)
        .readLines()
        .map(String::trim)
        .filter(String::isNotEmpty)

fun Int.solve(solver: List<String>.() -> String) {
    getNextInput(this)
        .solver()
        .writePuzzleSolution(this)
}

fun <T> List<T>.writePuzzleSolution(level: Int) =
    getFile(level, FileType.OUT).writeText(joinToString("\n"))

fun Int.writePuzzleSolution(level: Int) = listOf(this).writePuzzleSolution(level)
fun String.writePuzzleSolution(level: Int) = listOf(this).writePuzzleSolution(level)

data class Point(
    val x: Int,
    val y: Int
) {
    fun manhattenDistance(anotherPoint: Point) = abs(x - anotherPoint.x) + abs(y - anotherPoint.y)

    operator fun plus(vector: Vector) = Point(x + vector.dx, y + vector.dy)

    fun vectorTo(other: Point) = Vector(other.x - x, other.y - y)
}

data class Vector(
    val dx: Int,
    val dy: Int
) {
    val length: Double by lazy {
        sqrt((dx.toDouble() * dx) + (dy.toDouble() * dy))
    }
    val angle: Double by lazy {
        val radiants = dx.toDouble() / length

        if (dx >= 0 && dy > 0) {
            2 - radiants
        } else if (dx < 0 && dy >= 0) {
            2 - radiants
        } else if (dx < 0 && dy < 0) {
            4 + radiants
        } else {
            radiants
        }
    }

    override fun toString(): String {
        return "($dx,$dy|${angle})"
    }

    fun scale(factor: Int) = Vector(dx * factor, dy * factor)

    operator fun times(factor: Int) = scale(factor)
    operator fun div(factor: Int) = Vector(dx / factor, dy / factor)

    fun minimize() = this / ggt(dx, dy)
}

fun ggt(x: Int = 0, y: Int = 0): Int {
    var a: Int = abs(x)
    var b: Int = abs(y)

    if (a == 0) {
        return b
    }

    while (b != 0) {
        if (a > b) {
            a -= b
        } else {
            b -= a
        }
    }

    return a
}