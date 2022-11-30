package me.salzinger.common.geometry

import me.salzinger.common.math.ggt
import kotlin.math.sqrt

data class Vector2D(
    val dx: Int,
    val dy: Int,
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

    fun scale(factor: Int) = Vector2D(dx * factor, dy * factor)

    operator fun times(factor: Int) = scale(factor)
    operator fun div(factor: Int) = Vector2D(dx / factor, dy / factor)

    fun minimize() = this / ggt(dx, dy)
}
