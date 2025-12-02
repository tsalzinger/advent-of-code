package me.salzinger.common.geometry

import me.salzinger.common.geometry.Point2D.Companion.toPoint2D as correctToPoint2D

data class Line2D(
    val start: Point2D,
    val end: Point2D,
) {
    private fun progressionOf(
        v1: Int,
        v2: Int,
    ): IntProgression =
        if (v1 < v2) {
            v1..v2
        } else {
            v1 downTo v2
        }

    fun mapToPoint2Ds(): List<Point2D> =
        when {
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

    companion object {
        @Deprecated(
            message = "This function was moved to the companion of Point2D where it was supposed to be from the start",
            replaceWith =
                ReplaceWith(
                    expression = "toPoint2D()",
                    imports = ["me.salzinger.common.geometry.Point2D.Companion.toPoint2D"],
                ),
        )
        fun String.toPoint2D(): Point2D = this.correctToPoint2D()

        fun String.toLine2D(): Line2D {
            val (start, end) = split("->").map { it.correctToPoint2D() }
            return Line2D(start, end)
        }
    }
}
