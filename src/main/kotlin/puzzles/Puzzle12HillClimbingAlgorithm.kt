package puzzles

import me.salzinger.common.Grid2D
import me.salzinger.common.streamInput
import me.salzinger.common.toConsoleString
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

    data class StepCount(
        val heightItem: HeightItem,
        var stepCount: Int? = null,
    )

    fun Grid2D<StepCount>.getNeighborsThanCanAccess(targetCell: Grid2D.Cell<StepCount>): List<Grid2D.Cell<StepCount>> {
        return getNeighborsOf(targetCell).filter { targetCell.value.heightItem.canBeAccessedFrom(it.value.heightItem) }
    }

    object Part1 {

        fun HeightItem.canBeAccessedFrom(heightItem: HeightItem): Boolean {
            return (heightItem.height + 1) >= height
        }

        fun Sequence<String>.solve(): Int {
            return toGrid()
                .transformValues {
                    StepCount(
                        heightItem = it.value,
                    )
                }
                .let { grid ->
                    // note - we invert start and end intentionally as we expect a narrow path (== less choices) near the target
                    val targetCell = grid.single { it.value.heightItem.heightCode == 'S' }
                    val startingCell = grid.single { it.value.heightItem.heightCode == 'E' }

                    var currentCells = setOf(startingCell)
                    var currentSteps = 0
                    startingCell.value.stepCount = currentSteps

                    while (targetCell !in currentCells && currentCells.isNotEmpty()) {
                        currentSteps++
                        currentCells = currentCells
                            .flatMap { currentCell ->
                                grid.getNeighborsThanCanAccess(currentCell)
                            }
                            .filter { it.value.stepCount == null }
                            .toSet()
                            .onEach { it.value.stepCount = currentSteps }
                    }


                    println(grid.toConsoleString { cell ->
                        cell.value.stepCount?.toString()
                            ?.padStart(4, ' ')
                            ?.let { "($it)" } ?: "(....)"
                    })

                    targetCell.value.stepCount!!
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

    object Part2 {

        fun Sequence<String>.solve(): Int {
            return toGrid()
                .transformValues {
                    StepCount(
                        heightItem = it.value,
                    )
                }
                .let { grid ->
                    // note - we invert start and end intentionally as we expect a narrow path (== less choices) near the target
                    val targetCell = grid.single { it.value.heightItem.heightCode == 'S' }
                    val startingCell = grid.single { it.value.heightItem.heightCode == 'E' }

                    var currentCells = setOf(startingCell)
                    var currentSteps = 0
                    startingCell.value.stepCount = currentSteps

                    while (currentCells.find { it.value.heightItem.height == 0 } == null) {
                        currentSteps++
                        currentCells = currentCells
                            .flatMap { currentCell ->
                                grid.getNeighborsThanCanAccess(currentCell)
                            }
                            .filter { it.value.stepCount == null }
                            .toSet()
                            .onEach { it.value.stepCount = currentSteps }
                    }


                    println(grid.toConsoleString { cell ->
                        cell.value.stepCount?.toString()
                            ?.padStart(4, ' ')
                            ?.let { "($it)" } ?: "(....)"
                    })

                    currentCells.first { it.value.heightItem.height == 0 }.value.stepCount!!
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
