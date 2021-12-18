package me.salzinger

import me.salzinger.TargetArea.Companion.fromInput

data class TargetArea(
    val xRange: IntRange,
    val yRange: IntRange,
) {
    val maxX = xRange.last
    val minX = xRange.first

    val maxY = yRange.last
    val minY = yRange.first

    operator fun contains(point: Point): Boolean {
        return point.x in xRange && point.y in yRange
    }

    companion object {
        fun String.fromInput(): TargetArea {
            return drop("target area: ".count())
                .split(",")
                .map { range ->
                    range
                        .trim()
                        .drop(2)
                        .split("..")
                        .map { it.toInt() }
                        .let { (start, end) ->
                            start..end
                        }

                }
                .let { (xRange, yRange) ->
                    TargetArea(xRange = xRange, yRange = yRange)
                }
        }
    }
}

private fun List<String>.solve(): String {
    val targetArea = single().fromInput()

    var steps = 1
    var startXVelocity = 1

    val maxStartXVelocity = (targetArea.maxX / steps)


    return targetArea.toString()
}

fun main() {
    17.solveExamples(
        expectedSolutions = listOf("45"),
        solver = List<String>::solve
    )

    17.solve(1, List<String>::solve)
}
