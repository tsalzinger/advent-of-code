package me.salzinger

fun main() {
    19.solve {
        toAsteroidMap()
            .monitoringStation
            .visibleAsteroids
            .toString()
    }
}

enum class MapState {
    EMPTY,
    ASTEROID,
    OBSTRUCTED
}

data class MonitoringStation(
    val location: Point,
    val visibleAsteroids: Int
)

fun Point.up(distance: Int) = Point(x, y + distance)
fun Point.left(distance: Int) = Point(x - distance, y)
fun Point.down(distance: Int) = Point(x, y - distance)
fun Point.right(distance: Int) = Point(x + distance, y)
fun Point.getRingAround(radius: Int) = PointRing(this, radius)

class PointRing(private val center: Point, private val radius: Int) {
    val points: Set<Point> by lazy {

        val topEdge = center.up(radius).run {
            (this.left(radius).x.rangeTo(this.right(radius).x)).map { Point(it, y) }
        }

        val rightEdge = center.right(radius).run {
            (this.up(radius).y.downTo(this.down(radius).y)).map { Point(x, it) }
        }


        val bottomEdge = center.down(radius).run {
            (this.right(radius).x.downTo(this.left(radius).x)).map { Point(it, y) }
        }

        val leftEdge = center.left(radius).run {
            (this.down(radius).y.rangeTo(this.up(radius).y)).map { Point(x, it) }
        }

        mutableSetOf<Point>().apply {
            addAll(topEdge)
            addAll(rightEdge)
            addAll(bottomEdge)
            addAll(leftEdge)
        }.toSet()
    }

    fun grow(difference: Int = 1) = PointRing(center, radius + difference)
}

class AsteroidMap(val map: Map<Point, MapState>, val width: Int, val height: Int) {
    val monitoringStation by lazy<MonitoringStation> {
        val monitoringStations = mutableListOf<MonitoringStation>()

        for (x in 0 until width) {
            for (y in 0 until height) {
                val mutableMap = map.toMutableMap()
                val startPosition = Point(x, y)

                if (mutableMap[startPosition] == MapState.ASTEROID) {
                    var currentRing = startPosition.getRingAround(1)
                    var mapNotNull = currentRing.toMapStatesMap(mutableMap)
                    var asteroidsCount = 0

                    while (mapNotNull.isNotEmpty()) {
                        val asteroids = mapNotNull.filter { it.value == MapState.ASTEROID }.keys

                        asteroidsCount += asteroids.onEach { asteroid ->
                            val baseVector = startPosition.vectorTo(asteroid).minimize()
                            var endPoint = asteroid + baseVector

                            while (mutableMap.containsKey(endPoint)) {
                                mutableMap.compute(endPoint) { key, value ->
                                    if (value == MapState.ASTEROID) {
                                        MapState.OBSTRUCTED
                                    } else {
                                        value
                                    }
                                }

                                endPoint += baseVector
                            }
                        }.size

                        currentRing = currentRing.grow()
                        mapNotNull = currentRing.toMapStatesMap(mutableMap)
                    }

                    monitoringStations.add(MonitoringStation(startPosition, asteroidsCount))
                }
            }
        }

        monitoringStations.maxBy(MonitoringStation::visibleAsteroids)!!
    }

    private fun PointRing.toMapStatesMap(map: Map<Point, MapState>) = points
        .mapNotNull {
            if (map.containsKey(it)) {
                it to map.getValue(it)
            } else {
                null
            }
        }
        .toMap()

}

fun List<String>.toAsteroidMap(): AsteroidMap {
    val height = this.size
    val width = this[0].length
    val map = mutableMapOf<Point, MapState>().also {
        this.forEachIndexed { y, rowInput ->
            rowInput.forEachIndexed { x, input ->
                it[Point(x, y)] = when (input) {
                    '.' -> MapState.EMPTY
                    '#' -> MapState.ASTEROID
                    else -> throw RuntimeException("Unsupported input character $input")
                }
            }
        }
    }.toMap()
    return AsteroidMap(map, width, height)
}