package puzzles

import me.salzinger.common.FileType
import me.salzinger.common.getFile
import me.salzinger.common.writePuzzleSolution
import puzzles.Puzzle1.getCaloriesList
import puzzles.Puzzle1.maxCalories
import puzzles.Puzzle1.sumCalories

object Puzzle1 {
    fun getCaloriesList(level: Int, part: String): List<List<Int>> {
        return getFile(level, part, FileType.IN)
            .readLines()
            .toCaloriesList()
    }

    fun List<String>.toCaloriesList(): List<List<Int>> {
        return map { it.trim() }
            .dropLastWhile { it.isBlank() }
            .fold(mutableListOf(mutableListOf<Int>())) { acc, input ->
                if (input.isBlank()) {
                    acc.add(mutableListOf())
                } else {
                    acc.last().add(input.toInt())
                }
                acc
            }
    }

    fun List<List<Int>>.maxCalories(): Int {
        return maxOf { caloriesEntries ->
            caloriesEntries.sum()
        }
    }

    fun List<List<Int>>.sumCalories(): List<Int> {
        return map { caloriesEntries -> caloriesEntries.sum() }
    }
}

object Part1 {
    object Examples {
        @JvmStatic
        fun main(args: Array<String>) {
            getCaloriesList(1, "example-1")
                .maxCalories()
                .toString()
                .also { solution ->
                    val expectedSolution = getFile(1, "1-example-1", FileType.SOLUTION).readLines().first()
                    check(solution == expectedSolution) {
                        "Expected $expectedSolution but got $solution"
                    }
                }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        getCaloriesList(1, "1")
            .maxCalories()
            .toString()
            .writePuzzleSolution(1, 1)
    }
}

object Part2 {
    object Examples {
        @JvmStatic
        fun main(args: Array<String>) {
            getCaloriesList(1, "example-1")
                .sumCalories()
                .sortedDescending()
                .take(3)
                .sum()
                .toString()
                .also { solution ->
                    val expectedSolution = getFile(1, "2-example-1", FileType.SOLUTION).readLines().first()
                    check(solution == expectedSolution) {
                        "Expected $expectedSolution but got $solution"
                    }
                }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        getCaloriesList(1, "1")
            .sumCalories()
            .sortedDescending()
            .take(3)
            .sum()
            .toString()
            .writePuzzleSolution(1, 2)
    }
}
