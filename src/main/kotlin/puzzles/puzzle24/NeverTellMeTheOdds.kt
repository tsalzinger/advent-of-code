package me.salzinger.puzzles.puzzle24

import me.salzinger.common.extensions.permutate
import me.salzinger.common.extensions.toBigDecimalList
import me.salzinger.puzzles.puzzle24.NeverTellMeTheOdds.Plane2D.XY
import me.salzinger.puzzles.puzzle24.NeverTellMeTheOdds.Plane2D.XZ
import me.salzinger.puzzles.puzzle24.NeverTellMeTheOdds.Plane2D.YZ
import java.math.BigDecimal
import java.math.MathContext

object NeverTellMeTheOdds {
    data class Point3D(
        val x: BigDecimal,
        val y: BigDecimal,
        val z: BigDecimal,
    ) {
        fun projectTo(plane: Plane2D): Point2D {
            return when (plane) {
                XY -> Point2D(x, y)
                YZ -> Point2D(y, z)
                XZ -> Point2D(x, z)
            }
        }
    }

    data class Point2D(
        val x: BigDecimal,
        val y: BigDecimal,
    ) {
        override fun toString(): String {
            return "$x, $y"
        }
    }

    val BIG_DECIMAL_ONE = "1.0".toBigDecimal()

    fun String.toPoint3D(): Point3D {
        val (x, y, z) = toBigDecimalList(",")
        return Point3D(
            x = x, // * BIG_DECIMAL_ONE,
            y = y, // * BIG_DECIMAL_ONE,
            z = z, // * BIG_DECIMAL_ONE,
        )
    }

    enum class Plane2D {
        XY,
        YZ,
        XZ,
    }

    data class Line3D(
        val position: Point3D,
        val velocity: Point3D,
    ) {
        fun projectTo(plane: Plane2D): Line2D {
            return Line2D(position.projectTo(plane), velocity.projectTo(plane))
        }
    }

    data class Line2D(
        val position: Point2D,
        val velocity: Point2D,
    ) {
        val k = velocity.y.divide(velocity.x, MathContext.DECIMAL128)
        val d = position.y - k * position.x

        // y = k*x + d
        // k == Vy/Vx

        // d == y - K*x

        // k1*x + d1 == k2*x +d2
        // k1*x -k2*x = d2 - d1
        // x * (k1 - k2) = d2 - d1
        // x = (d2 - d1) / (k1 - k2)

        fun getIntersectionWith(line: Line2D): Point2D? {
            return if (k == line.k) {
                null
            } else {
                val x = (line.d - d) / (k - line.k)
                val y = k * x + d

                Point2D(x, y)
            }
        }

        override fun toString(): String {
            return "$position @ $velocity"
        }
    }

    fun Pair<Point3D, Point3D>.toLine(): Line3D {
        return Line3D(first, second)
    }

    fun String.toPositionAndVelocity(): Pair<Point3D, Point3D> {
        return replace(" ", "")
            .split("@")
            .let { (position, velocity) ->
                position.toPoint3D() to velocity.toPoint3D()
            }
    }

    fun Line2D.isFuturePoint(point: Point2D): Boolean {
        return if (position.x == point.x) {
            (point.y < position.y && velocity.y < BigDecimal.ZERO) ||
                (point.y > position.y && velocity.y > BigDecimal.ZERO)
        } else {
            (point.x < position.x && velocity.x < BigDecimal.ZERO) ||
                (point.x > position.x && velocity.x > BigDecimal.ZERO)
        }
    }

    fun Point2D.inBounds(
        min: BigDecimal,
        max: BigDecimal,
    ): Boolean {
        val boundaryRange = min..max
        return x in boundaryRange && y in boundaryRange
    }

    fun Point2D.inBounds(
        min: Long,
        max: Long,
    ): Boolean {
        return inBounds(min.toBigDecimal(), max.toBigDecimal())
    }

    fun Sequence<String>.getNumberOfFutureXYIntersectionsWithin(
        boundaryMin: Long,
        boundaryMax: Long,
    ): Int {
        return map {
            it.toPositionAndVelocity()
        }
            .map {
                it.toLine().projectTo(XY)
            }
            .permutate()
//            .onEach {
//                println(it)
//            }
            .partition { (first, second) ->
                first.getIntersectionWith(second)?.let { intersection ->
                    intersection.inBounds(boundaryMin, boundaryMax) &&
                        first.isFuturePoint(intersection) &&
                        second.isFuturePoint(intersection)
                } ?: false
            }
            .let {
                it.first.count()
            }
    }
}
