package me.salzinger.puzzles

import me.salzinger.common.extensions.toPairs
import me.salzinger.common.geometry.Point2D
import me.salzinger.common.geometry.Vector2D
import me.salzinger.common.streamInput

object Puzzle15BeaconExclusionZone {
    object Part1 {

        fun String.toPosition(): Point2D {
            return split(", ")
                .map { it.drop(2) }
                .map { it.toInt() }
                .let { (x, y) -> Point2D(x, y) }
        }

        fun String.toSensorAndBeaconPosition(): Pair<Point2D, Point2D> {
            return removePrefix("Sensor at ")
                .split(": closest beacon is at ")
                .map { it.toPosition() }
                .toPairs()
                .single()
        }

        data class Boundary(
            val minX: Int,
            val maxX: Int,
            val minY: Int,
            val maxY: Int,
        ) {
            val xRange = minX..maxX
            val yRange = minY..maxY
        }

        fun Boundary?.expand(point1: Point2D, point2: Point2D): Boundary {
            return Boundary(
                minX = minOf(point1.x, point2.x, this?.minX ?: point2.x),
                maxX = maxOf(point1.x, point2.x, this?.maxX ?: point2.x),
                minY = minOf(point1.y, point2.y, this?.minY ?: point2.y),
                maxY = maxOf(point1.y, point2.y, this?.maxY ?: point2.y),
            )
        }

        fun Sequence<String>.solve(yToScan: Int): Int {
            var currentBoundary: Boundary? = null

            val coveredBySensors = map { it.toSensorAndBeaconPosition() }
                .map { (sensorPosition, beaconPosition) ->
                    val manhattenDistance = sensorPosition.manhattenDistance(beaconPosition)

                    val upVector = Vector2D(dx = 0, dy = manhattenDistance)
                    val rightVector = Vector2D(dx = manhattenDistance, dy = 0)

                    currentBoundary = currentBoundary
                        .expand(
                            sensorPosition + upVector + rightVector,
                            sensorPosition - upVector - rightVector
                        )

                    return@map { position: Point2D ->
                        sensorPosition.manhattenDistance(position) <= manhattenDistance && position != beaconPosition
                    }
                }.toList()

            val boundary = currentBoundary!!

            return if (yToScan !in boundary.yRange) {
                0
            } else {
                boundary
                    .xRange
                    .count { x ->
                        coveredBySensors.any { it(Point2D(x, yToScan)) }
                    }
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            "puzzle-15.in".streamInput().solve(yToScan = 2_000_000).let(::println)
        }
    }
}
