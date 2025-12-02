package me.salzinger.common.geometry

import me.salzinger.common.extensions.toIntList
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.math.abs

typealias X = Int
typealias Y = Int

data class Point2D(
    val x: X,
    val y: Y,
) {
    fun manhattenDistance(anotherPoint: Point2D) = abs(x - anotherPoint.x) + abs(y - anotherPoint.y)

    operator fun plus(vector: Vector2D) = Point2D(x + vector.dx, y + vector.dy)

    operator fun minus(vector: Vector2D) = Point2D(x - vector.dx, y - vector.dy)

    fun vectorTo(other: Point2D) = Vector2D(other.x - x, other.y - y)

    companion object {
        val ZERO = Point2D(0, 0)

        fun String.toPoint2D(vararg delimiters: String): Point2D {
            val (x, y) =
                if (delimiters.isEmpty()) {
                    trim().toIntList()
                } else {
                    trim().toIntList(*delimiters)
                }
            return Point2D(x, y)
        }
    }
}
