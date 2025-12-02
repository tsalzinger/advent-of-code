package me.salzinger.common.geometry

import me.salzinger.common.Grid2D

enum class Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT,
    RIGHT_DOWN,
    DOWN_LEFT,
    LEFT_UP,
    UP_RIGHT,
}

fun Direction.isOppositeOf(direction: Direction): Boolean =
    when (this) {
        Direction.UP -> direction == Direction.DOWN
        Direction.RIGHT -> direction == Direction.LEFT
        Direction.DOWN -> direction == Direction.UP
        Direction.LEFT -> direction == Direction.RIGHT
        Direction.RIGHT_DOWN -> direction == Direction.LEFT_UP
        Direction.DOWN_LEFT -> direction == Direction.UP_RIGHT
        Direction.LEFT_UP -> direction == Direction.RIGHT_DOWN
        Direction.UP_RIGHT -> direction == Direction.DOWN_LEFT
    }

fun Direction.flip(): Direction =
    when (this) {
        Direction.UP -> Direction.DOWN
        Direction.RIGHT -> Direction.LEFT
        Direction.DOWN -> Direction.UP
        Direction.LEFT -> Direction.RIGHT
        Direction.RIGHT_DOWN -> Direction.LEFT_UP
        Direction.DOWN_LEFT -> Direction.UP_RIGHT
        Direction.LEFT_UP -> Direction.RIGHT_DOWN
        Direction.UP_RIGHT -> Direction.DOWN_LEFT
    }

fun Direction.rotate90DegreesClockwise(): Direction =
    when (this) {
        Direction.UP -> Direction.RIGHT
        Direction.RIGHT -> Direction.DOWN
        Direction.DOWN -> Direction.LEFT
        Direction.LEFT -> Direction.UP
        Direction.RIGHT_DOWN -> Direction.UP_RIGHT
        Direction.DOWN_LEFT -> Direction.RIGHT_DOWN
        Direction.LEFT_UP -> Direction.DOWN_LEFT
        Direction.UP_RIGHT -> Direction.LEFT_UP
    }

fun Direction.rotate90DegreesCounterclockwise(): Direction =
    when (this) {
        Direction.UP -> Direction.LEFT
        Direction.RIGHT -> Direction.UP
        Direction.DOWN -> Direction.RIGHT
        Direction.LEFT -> Direction.DOWN
        Direction.RIGHT_DOWN -> Direction.UP_RIGHT
        Direction.DOWN_LEFT -> Direction.RIGHT_DOWN
        Direction.LEFT_UP -> Direction.DOWN_LEFT
        Direction.UP_RIGHT -> Direction.LEFT_UP
    }

operator fun Grid2D.Coordinate.invoke(direction: Direction): Grid2D.Coordinate =
    when (direction) {
        Direction.UP -> up()
        Direction.RIGHT -> right()
        Direction.DOWN -> down()
        Direction.LEFT -> left()
        Direction.RIGHT_DOWN -> rightDown()
        Direction.DOWN_LEFT -> downLeft()
        Direction.LEFT_UP -> leftUp()
        Direction.UP_RIGHT -> upRight()
    }

fun Grid2D.Coordinate.getDirectionToNeighbor(neighbor: Grid2D.Coordinate): Direction =
    when (neighbor) {
        up() -> Direction.UP
        right() -> Direction.RIGHT
        down() -> Direction.DOWN
        left() -> Direction.LEFT
        rightDown() -> Direction.RIGHT_DOWN
        downLeft() -> Direction.DOWN_LEFT
        leftUp() -> Direction.LEFT_UP
        upRight() -> Direction.UP_RIGHT
        else -> throw RuntimeException("$neighbor is not a neighbor of $this")
    }
