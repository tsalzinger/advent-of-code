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

fun getFile(level: Int, part: Int?, fileType: FileType) =
    File("src/main/resources/puzzle-$level${part?.run { "-$this" } ?: ""}.${fileType.extension}")

fun getNextInput(puzzle: Int, part: Int? = null) =
    getFile(puzzle, part, FileType.IN)
        .readLines()
        .map(String::trim)
        .filter(String::isNotBlank)

fun Int.solve(part: Int, solver: List<String>.() -> String) {
    getNextInput(this, part)
        .solver()
        .writePuzzleSolution(this, part)
}

fun String.toIntList(vararg delimiters: String = arrayOf(",")): List<Int> {
    return split(delimiters = delimiters).map { it.toInt() }
}

fun String.toIntList(regex: Regex): List<Int> {
    return split(regex = regex).map { it.toInt() }
}

fun Int.solve(solver: List<String>.() -> String) {
    getNextInput(this)
        .solver()
        .writePuzzleSolution(this)
}

fun <T> List<T>.writePuzzleSolution(level: Int, part: Int?) =
    getFile(level, part, FileType.OUT).writeText(joinToString("\n"))

fun String.writePuzzleSolution(level: Int, part: Int? = null) = listOf(this).writePuzzleSolution(level, part)

data class Point(
    val x: Int,
    val y: Int
) {
    fun manhattenDistance(anotherPoint: Point) = abs(x - anotherPoint.x) + abs(y - anotherPoint.y)

    operator fun plus(vector: Vector) = Point(x + vector.dx, y + vector.dy)

    fun vectorTo(other: Point) = Vector(other.x - x, other.y - y)

    companion object {
        val ZERO = Point(0, 0)
    }
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

fun ByteArray.toInt(): Int {
    return joinToString("").toInt(2)
}
