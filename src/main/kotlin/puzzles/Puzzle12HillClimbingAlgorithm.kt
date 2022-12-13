package puzzles

import me.salzinger.common.Grid2D
import me.salzinger.common.streamInput
import puzzles.Puzzle12HillClimbingAlgorithm.Part1.canBeAccessedFrom

object Puzzle12HillClimbingAlgorithm {

    class HeightItem(val heightCode: Char) {
        val height = when (heightCode) {
            in 'a'..'z' -> heightCode.code - 'a'.code
            'S' -> 0
            'E' -> 26
            else -> throw RuntimeException("Unknown height input: $heightCode")
        }
    }

    fun Sequence<String>.toGrid(): Grid2D<HeightItem> {
        return map { row ->
            row.map { heightCode -> HeightItem(heightCode) }
        }
            .toList()
            .let(::Grid2D)
    }

    enum class MoveDirection {
        UP,
        RIGHT,
        DOWN,
        LEFT,
    }

    operator fun Grid2D.Coordinate.invoke(moveDirection: MoveDirection): Grid2D.Coordinate {
        return when (moveDirection) {
            MoveDirection.UP -> up()
            MoveDirection.RIGHT -> right()
            MoveDirection.DOWN -> down()
            MoveDirection.LEFT -> left()
        }
    }

    fun Grid2D<HeightItem>.getNeighborsThanCanAccess(targetCell: Grid2D.Cell<HeightItem>): List<Grid2D.Cell<HeightItem>> {
        return getNeighborsOf(targetCell).filter { targetCell.value.canBeAccessedFrom(it.value) }
    }

    object Part1 {

        fun HeightItem.canBeAccessedFrom(heightItem: HeightItem): Boolean {
            return (heightItem.height + 1) >= height
        }

        fun Grid2D<HeightItem>.getPathsFrom(
            startingCell: Grid2D.Cell<HeightItem>,
            targetCell: Grid2D.Cell<HeightItem>,
            alreadyVisitedCells: Set<Grid2D.Cell<HeightItem>> = setOf(startingCell),
        ): List<List<Grid2D.Cell<HeightItem>>> {
            return getNeighborsThanCanAccess(startingCell)
                .filter { it !in alreadyVisitedCells }
                .flatMap { nextCell ->
                    if (nextCell == targetCell) {
                        listOf(listOf(nextCell))
                    } else {
                        getPathsFrom(
                            startingCell = nextCell,
                            targetCell = targetCell,
                            alreadyVisitedCells = alreadyVisitedCells + nextCell
                        ).map {
                            buildList {
                                add(nextCell)
                                addAll(it)
                            }
                        }
                    }
                }
        }

        fun Sequence<String>.solve(): Int {
            return toGrid()
                .let { grid ->
                    val startingCell = grid.single { it.value.heightCode == 'S' }
                    val targetCell = grid.single { it.value.heightCode == 'E' }

                    grid.getPathsFrom(
                        startingCell = targetCell,
                        targetCell = startingCell,
                    ).minOf { it.count() }
                }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            "puzzle-12.in"
                .streamInput()
                .solve()
                .let(::println)
        }
    }
}
