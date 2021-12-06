package me.salzinger

data class Line(
    val start: Point,
    val end: Point,
) {
    private fun progressionOf(v1: Int, v2: Int): IntProgression {
        return if (v1 < v2) {
            v1..v2
        } else {
            v1 downTo v2
        }
    }

    fun mapToPoints(): List<Point> {
        return when {
            start.x == end.x -> {
                progressionOf(start.y, end.y)
                    .map { y ->
                        Point(start.x, y)
                    }
            }
            start.y == end.y -> {
                progressionOf(start.x, end.x)
                    .map { x ->
                        Point(x, start.y)
                    }
            }
            else -> {
                val xValues = progressionOf(start.x, end.x)
                val yValues = progressionOf(start.y, end.y).toList()

                xValues.mapIndexed { index, x ->
                    Point(x, yValues[index])
                }
            }
        }
    }

    companion object {
        fun String.toPoint(): Point {
            val (x, y) = trim().toIntList()
            return Point(x, y)
        }

        fun String.toLine(): Line {
            val (start, end) = split("->").map { it.toPoint() }
            return Line(start, end)
        }
    }
}
