package me.salzinger

fun main() {
    19.solve {
        toAsteroidMap()
            .monitoringStation
            .visibleAsteroids
            .toString()
    }

    20.solve {
        toAsteroidMap()
            .destroyAllAsteroids()[199]
            .run {
                "${x * 100 + y}"
            }
    }
}

enum class MapState {
    EMPTY,
    ASTEROID,
    OBSTRUCTED,
    MONITORING_STATION
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

class AsteroidMap(
    val map: Map<Point, MapState>,
    val width: Int,
    val height: Int
) {
    val monitoringStation: MonitoringStation by lazy {
        val monitoringStations = map.entries
            .filter { (_, entry) -> entry == MapState.MONITORING_STATION }
            .map {
                MonitoringStation(it.key, findAsteroidsFrom(it.key)!!.size)
            }
            .toMutableList()

        if (monitoringStations.isEmpty()) {
            for (x in 0 until width) {
                for (y in 0 until height) {
                    val startPosition = Point(x, y)

                    val asteroids = findAsteroidsFrom(startPosition)

                    if (asteroids != null) {
                        monitoringStations.add(MonitoringStation(startPosition, asteroids.size))
                    }
                }
            }
        }

        monitoringStations.maxBy(MonitoringStation::visibleAsteroids)!!
    }

    fun hasAsteroids() =
        map.any { (location, mapState) ->
            location != monitoringStation.location && mapState == MapState.ASTEROID
        }

    fun destroy(asteroidPositions: List<Point>): AsteroidMap {
        return AsteroidMap(
            map.toMutableMap().apply {
                asteroidPositions.forEach {
                    this[it] = MapState.EMPTY
                }
            },
            width,
            height
        )
    }

    fun getDestroyable() =
        findAsteroidsFrom(monitoringStation.location)!!
            .sortedBy { monitoringStation.location.vectorTo(it).angle }

    fun findAsteroidsFrom(startPosition: Point): List<Point>? {
        return if (setOf(MapState.ASTEROID, MapState.MONITORING_STATION).contains(map[startPosition])) {
            val mapWithObstructions = map.toMutableMap()
            val asteroids = mutableListOf<Point>()
            var currentRing = startPosition.getRingAround(1)
            var mapStatesInCurrentRing = currentRing.toMapStatesMap(mapWithObstructions)

            while (mapStatesInCurrentRing.isNotEmpty()) {
                val asteroidsInCurrentRing = mapStatesInCurrentRing.filter { it.value == MapState.ASTEROID }.keys

                asteroidsInCurrentRing
                    .onEach { asteroid ->
                        val baseVector = startPosition.vectorTo(asteroid).minimize()
                        var endPoint = asteroid + baseVector

                        while (mapWithObstructions.containsKey(endPoint)) {
                            mapWithObstructions.compute(endPoint) { key, value ->
                                if (value == MapState.ASTEROID) {
                                    MapState.OBSTRUCTED
                                } else {
                                    value
                                }
                            }

                            endPoint += baseVector
                        }
                    }
                    .run(asteroids::addAll)

                currentRing = currentRing.grow()
                mapStatesInCurrentRing = currentRing.toMapStatesMap(mapWithObstructions)
            }
            asteroids.toList()
        } else {
            null
        }
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

    fun destroyAllAsteroids(): List<Point> {
        var asteroidMap = this
        val destroyedAsteroids = mutableListOf<Point>()
        var rotation = 1

        while (asteroidMap.hasAsteroids()) {
            val destroyable = asteroidMap.getDestroyable()
            destroyedAsteroids.addAll(destroyable)
            asteroidMap = asteroidMap.destroy(destroyable)
            println("Removed ${destroyable.size} asteroids in rotation $rotation")
            rotation++
        }

        return destroyedAsteroids.toList()
    }

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
                    'X' -> MapState.MONITORING_STATION
                    else -> throw RuntimeException("Unsupported input character $input")
                }
            }
        }
    }.toMap()
    return AsteroidMap(map, width, height)
}