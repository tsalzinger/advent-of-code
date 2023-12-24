package me.salzinger.puzzles.puzzle22

import me.salzinger.common.extensions.toIntList
import me.salzinger.common.geometry.Point2D

object SandSlabs {
    data class Coordinate3D(
        val x: Int,
        val y: Int,
        val z: Int,
    ) {
        fun up(): Coordinate3D {
            return copy(z = z + 1)
        }

        fun down(): Coordinate3D {
            return copy(z = z - 1)
        }

        fun isBelow(coordinate: Coordinate3D): Boolean {
            return z < coordinate.z
        }

        override fun toString(): String {
            return "$x,$y,$z"
        }
    }

    data class Brick(
        val startCoordinate: Coordinate3D,
        val endCoordinate: Coordinate3D,
    ) {
        init {
            require(startCoordinate.x <= endCoordinate.x)
            require(startCoordinate.y <= endCoordinate.y)
            require(startCoordinate.z <= endCoordinate.z)
        }

        fun down(): Brick {
            return Brick(
                startCoordinate = startCoordinate.down(),
                endCoordinate = endCoordinate.down(),
            )
        }

        val coordinates: Sequence<Coordinate3D>
            get() {
                return (startCoordinate.x..(endCoordinate.x))
                    .asSequence()
                    .flatMap { x ->
                        (startCoordinate.y..(endCoordinate.y))
                            .flatMap { y ->
                                (startCoordinate.z..(endCoordinate.z))
                                    .map { z ->
                                        Coordinate3D(
                                            x = x,
                                            y = y,
                                            z = z,
                                        )
                                    }
                            }
                    }
            }

        override fun toString(): String {
            return "$startCoordinate~$endCoordinate"
        }
    }

    fun String.toCoordinate3D(): Coordinate3D {
        return toIntList(",")
            .let { (x, y, z) ->
                Coordinate3D(x = x, y = y, z = z)
            }
    }

    fun String.toBrick(): Brick {
        return split("~")
            .let { (start, end) ->
                val startCoordinate = start.toCoordinate3D()
                val endCoordinate = end.toCoordinate3D()

                Brick(
                    startCoordinate = startCoordinate,
                    endCoordinate = endCoordinate,
                )
            }
    }

    fun Sequence<String>.toBricks(): Sequence<Brick> {
        return map { it.toBrick() }
    }

    fun Coordinate3D.isGround(): Boolean {
        return z == 0
    }

    fun Sequence<Brick>.settle(): Map<Coordinate3D, Brick> {
        return toList().settle()
    }

    fun List<Brick>.settle(): Map<Coordinate3D, Brick> {
        val bricks =
            sortedBy {
                it.startCoordinate.z
            }

        val minZ = bricks.first().startCoordinate.z
        val maxZ = bricks.last().startCoordinate.z

        val coordinateToBrickMap =
            bricks
                .flatMap { brick ->
                    brick.coordinates
                        .map {
                            it to brick
                        }
                }
                .toMap()
                .toMutableMap()

        for (currentLevel in minZ.coerceAtLeast(2)..maxZ) {
            val bricksAtLevel = bricks.filter { it.startCoordinate.z == currentLevel }

            bricksAtLevel.forEach { brick ->
                brick.coordinates.forEach(coordinateToBrickMap::remove)

                var current = brick
                while (current.startCoordinate.z > 1 && current.down().coordinates.none(coordinateToBrickMap::containsKey)) {
                    current = current.down()
                }

                current.coordinates.forEach { coordinateToBrickMap[it] = current }
            }
        }

        return coordinateToBrickMap
    }

    fun Brick.onLevel(level: Int): Boolean {
        return startCoordinate.z <= level && level <= endCoordinate.z
    }

    fun List<Brick>.visualizeLevels() {
        val minX = minOf { it.coordinates.minOf { it.x } }
        val maxX = maxOf { it.coordinates.minOf { it.x } }

        val minY = minOf { it.coordinates.minOf { it.y } }
        val maxY = maxOf { it.coordinates.minOf { it.y } }

        (1..maxOf { it.endCoordinate.z }).forEach {
            println("$it".padStart(15, '=').padEnd(30, '='))
            visualizeLevel(it, minX, maxX, minY, maxY)
        }
    }

    fun List<Brick>.visualizeLevel(
        level: Int,
        minX: Int,
        maxX: Int,
        minY: Int,
        maxY: Int,
    ) {
        val bricksAtLevel =
            filter { it.onLevel(level) }
                .mapIndexed { index, it ->
                    it to ('A' + index)
                }
        val cubesAtLevel =
            bricksAtLevel.flatMap { (brick, char) ->
                brick.coordinates.map {
                    it to char
                }
            }.map { (it, brick) ->
                Point2D(it.x, it.y) to brick
            }.distinct()

        val brickMap = cubesAtLevel.toMap()

        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val brickChar = brickMap[Point2D(x, y)] ?: '.'
                print(brickChar)
            }
            println()
        }
    }

    fun Sequence<String>.getNumberOfBricksWhichCanSafelyBeDisintegrated(): Int {
        val coordinateToBrickMap =
            toBricks()
                .settle()

        val bricks = coordinateToBrickMap.values.distinct()

        fun Brick.getBricksAbove(): List<Brick> {
            return coordinates
                .toList()
                .map {
                    it.up()
                }
                .filter {
                    endCoordinate.isBelow(it)
                }
                .mapNotNull {
                    coordinateToBrickMap[it]
                }
                .distinct()
        }

        fun Brick.getBricksBelow(): List<Brick> {
            return coordinates
                .toList()
                .map {
                    it.down()
                }
                .filter {
                    it.isBelow(startCoordinate)
                }
                .mapNotNull {
                    coordinateToBrickMap[it]
                }
                .distinct()
        }

        val safeBricks =
            bricks.filter { candidateBrick ->
                val bricksAbove =
                    candidateBrick
                        .getBricksAbove()

                if (bricksAbove.isNotEmpty()) {
                    bricksAbove
                        .flatMap {
                            it.getBricksBelow()
                        }.any {
                            it != candidateBrick
                        }
                } else {
                    true
                }
            }

        val reduced =
            safeBricks.filter {
                val brickList = bricks - it

                val settle = brickList.settle()

                brickList.toSet() == settle.values.toSet()
            }

        return reduced.count()
    }
}
