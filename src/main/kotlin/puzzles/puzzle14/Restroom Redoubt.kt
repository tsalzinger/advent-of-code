package me.salzinger.puzzles.puzzle14

import common.extensions.productBy
import me.salzinger.common.geometry.LazyGrid2D
import me.salzinger.common.geometry.Point2D
import me.salzinger.common.geometry.Vector2D
import me.salzinger.common.geometry.toConsoleString
import me.salzinger.puzzles.puzzle8.`Resonant Collinearity`.toPoint2D

object `Restroom Redoubt` {
    val inputRegex = Regex("""^p=(\d+),(\d+) v=(-?\d+),(-?\d+)$""")
    val roomWidth = 101
    val roomHeight = 103

    fun String.toPositionAndVector(): Pair<Point2D, Vector2D> {
        val (x, y, dx, dy) = inputRegex
            .matchEntire(this)!!
            .destructured
            .toList()
            .map { it.toInt() }

        return Point2D(x = x, y = y) to Vector2D(dx = dx, dy = dy)
    }

    fun Point2D.getQuadrantOrNull(): Int? {
        return if (x == roomWidth / 2 || y == roomHeight / 2) {
            null
        } else if (x < roomWidth / 2) {
            if (y < roomHeight / 2) {
                0
            } else {
                2
            }
        } else {
            if (y < roomHeight / 2) {
                1
            } else {
                3
            }
        }
    }

    fun Sequence<String>.getSafetyFactorAfter100Seconds(): Int {
        val guardsPerQuadrant = mutableMapOf<Int, Int>()

        return map { it.toPositionAndVector() }
            .map { (position, vector) ->
                val extendedVector = vector * 100

                val endPosition = position + extendedVector

                val wrapped = Point2D(endPosition.x % roomWidth, endPosition.y % roomHeight)

                Point2D(
                    x = if (wrapped.x >= 0) wrapped.x else (wrapped.x + roomWidth),
                    y = if (wrapped.y >= 0) wrapped.y else (wrapped.y + roomHeight),
                )
            }
            .mapNotNull { it.getQuadrantOrNull() }
            .groupBy { it }
            .values
            .productBy { it.count() }
    }
}