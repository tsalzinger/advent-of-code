package me.salzinger.common.geometry

import kotlin.math.abs

data class Point2D(
    val x: Int,
    val y: Int,
) {
    fun manhattenDistance(anotherPoint: Point2D) = abs(x - anotherPoint.x) + abs(y - anotherPoint.y)

    operator fun plus(vector: Vector2D) = Point2D(x + vector.dx, y + vector.dy)

    fun vectorTo(other: Point2D) = Vector2D(other.x - x, other.y - y)

    companion object {
        val ZERO = Point2D(0, 0)
    }
}
