package me.salzinger

data class Line(
    val start: Point,
    val end: Point,
) {
    fun mapToPoints(): List<Point> {
        return when {
            start.x == end.x -> {
                val startY = start.y
                val endY = end.y

                if (startY < endY) {
                    startY..endY
                } else {
                    startY downTo endY
                }.map { y ->
                    Point(start.x, y)
                }
            }
            start.y == end.y -> {
                val startX = start.x
                val endX = end.x

                if (startX < endX) {
                    startX..endX
                } else {
                    startX downTo endX
                }.map { y ->
                    Point(y, start.y)
                }
            }
            else -> emptyList()
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
