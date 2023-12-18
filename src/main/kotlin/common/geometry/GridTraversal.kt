package me.salzinger.common.geometry

import me.salzinger.common.Grid2D

enum class Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT,
}

fun Direction.isOppositeOf(direction: Direction): Boolean {
    return when (this) {
        Direction.UP -> direction == Direction.DOWN
        Direction.RIGHT -> direction == Direction.LEFT
        Direction.DOWN -> direction == Direction.UP
        Direction.LEFT -> direction == Direction.RIGHT
    }
}

operator fun Grid2D.Coordinate.invoke(direction: Direction): Grid2D.Coordinate {
    return when (direction) {
        Direction.UP -> up()
        Direction.RIGHT -> right()
        Direction.DOWN -> down()
        Direction.LEFT -> left()
    }
}

fun Grid2D.Coordinate.getDirectionToNeighbor(neighbor: Grid2D.Coordinate): Direction {
    return when (neighbor) {
        up() -> Direction.UP
        right() -> Direction.RIGHT
        down() -> Direction.DOWN
        left() -> Direction.LEFT
        else -> throw RuntimeException("$neighbor is not a neighbor of $this")
    }
}
