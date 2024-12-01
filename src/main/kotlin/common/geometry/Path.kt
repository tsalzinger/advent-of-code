package me.salzinger.common.geometry

import me.salzinger.common.Grid2D


data class Path(
    val coordinates: List<Grid2D.Coordinate>,
) : Iterable<Grid2D.Coordinate> {
    val directions = coordinates
        .windowed(2)
        .map { (first, second) -> first.getDirectionToNeighbor(second) }

    val lastPair by lazy { directions.last() to coordinates.last() }

    fun extend(direction: Direction, steps: Int): Path {
        require(steps >= 0)

        if (steps == 0) {
            return this
        }

        return Path(
            coordinates + (1..<steps).fold(listOf(coordinates.last()(direction))) { coordinates, _ ->
                coordinates + coordinates.last()(direction)
            }
        )
    }

    override fun toString(): String {
        return directions.toString()
    }

    operator fun contains(coordinate: Grid2D.Coordinate): Boolean {
        return coordinate in coordinates
    }

    operator fun contains(coordinateWithDirection: Pair<Direction, Grid2D.Coordinate>): Boolean {
        return coordinateWithDirection in directions.zip(coordinates.drop(1))
    }

    operator fun plus(coordinate: Grid2D.Coordinate): Path {
        return Path(
            coordinates + coordinate
        )
    }

    operator fun plus(direction: Direction): Path {
        return Path(
            coordinates + coordinates.last().invoke(direction)
        )
    }

    override fun iterator(): Iterator<Grid2D.Coordinate> {
        return coordinates.iterator()
    }
}
