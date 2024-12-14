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

    data class Guard(
        val position: Point2D,
        val velocity: Vector2D,
    ) {
        fun advance(): Guard {
            var newPosition = position + velocity
            newPosition = newPosition.copy(x = newPosition.x % roomWidth, y = newPosition.y % roomHeight)
            newPosition = newPosition.copy(
                x = if (newPosition.x >= 0) newPosition.x else (newPosition.x + roomWidth),
                y = if (newPosition.y >= 0) newPosition.y else (newPosition.y + roomHeight),
            )

            return Guard(newPosition, velocity)
        }
    }

    fun Sequence<String>.findTheEasterEgg(): Int {
        val boundary = LazyGrid2D.Boundary(0, roomHeight - 1, 0, roomWidth - 1)
        var guards = map { it.toPositionAndVector() }
            .map { (position, vector) ->
                Guard(position, vector)
            }
            .groupBy { it.position }

        var seconds = 0

        while (guards.values.any { it.count() != 1 }) {
            seconds++
            guards = guards.values
                .flatten()
                .map { it.advance() }
                .groupBy { it.position }
        }


        LazyGrid2D<String>(
            valuesProvider = { guards[it.toPoint2D()]?.size?.toString() ?: "." },
            boundaryProvider = { boundary }
        )
            .also {
                println("============= ${seconds.toString().padStart(4)}  =============")
                println(it.toConsoleString { it.value })
            }

        return seconds
    }
}