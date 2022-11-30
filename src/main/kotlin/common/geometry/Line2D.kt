package me.salzinger.common.geometry

import me.salzinger.common.extensions.toIntList

data class Line2D(
    val start: Point2D,
    val end: Point2D,
) {
    private fun progressionOf(v1: Int, v2: Int): IntProgression {
        return if (v1 < v2) {
            v1..v2
        } else {
            v1 downTo v2
        }
    }

    fun mapToPoint2Ds(): List<Point2D> {
        return when {
            start.x == end.x -> {
                progressionOf(start.y, end.y)
                    .map { y ->
                        Point2D(start.x, y)
                    }
            }

            start.y == end.y -> {
                progressionOf(start.x, end.x)
                    .map { x ->
                        Point2D(x, start.y)
                    }
            }

            else -> {
                val xValues = progressionOf(start.x, end.x)
                val yValues = progressionOf(start.y, end.y).toList()

                xValues.mapIndexed { index, x ->
                    Point2D(x, yValues[index])
                }
            }
        }
    }

    companion object {
        fun String.toPoint2D(): Point2D {
            val (x, y) = trim().toIntList()
            return Point2D(x, y)
        }

        fun String.toLine2D(): Line2D {
            val (start, end) = split("->").map { it.toPoint2D() }
            return Line2D(start, end)
        }
    }
}
