package puzzles

import me.salzinger.common.FileType
import me.salzinger.common.getFile
import me.salzinger.common.solve
import me.salzinger.common.solveExample

object Puzzle03RucksackReorganization {

    fun Char.itemPriority(): Int = when (this) {
        in 'a'..'z' -> code - 'a'.code + 1
        in 'A'..'Z' -> code - 'A'.code + 27
        else -> throw RuntimeException("Unsupported item encountered: $this")
    }

    fun String.itemPriorities(): List<Int> = map { it.itemPriority() }

    fun <T> List<T>.equalSplit(): Pair<List<T>, List<T>> {
        return dropLast(count() / 2) to drop(count() / 2)
    }

    object Part1 {

        fun List<String>.sumItemPrioritiesPresentInBothRucksackCompartments(): Int {
            return sumOf {
                it.itemPriorities()
                    .equalSplit()
                    .let { (firstRucksackPriorities, secondRucksackPriorities) ->
                        firstRucksackPriorities.toSet().intersect(secondRucksackPriorities.toSet()).single()
                    }
            }
        }

        object Examples {
            @JvmStatic
            fun main(args: Array<String>) {
                3.solveExample(
                    exampleNumber = 1, expectedSolution = getFile(
                        level = 3, part = "1-example-1", fileType = FileType.SOLUTION
                    ).readLines().first()
                ) {
                    sumItemPrioritiesPresentInBothRucksackCompartments()
                        .toString()
                }
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            3.solve(1) {
                sumItemPrioritiesPresentInBothRucksackCompartments()
                    .toString()
            }
        }
    }

    object Part2 {

        fun List<String>.sumGroupBadgePriorities(): Int {
            return map {
                it.itemPriorities().toSet()
            }.chunked(3) {
                it.reduce { acc, priorities ->
                    acc.intersect(priorities)
                }.single()
            }.sum()
        }

        object Examples {
            @JvmStatic
            fun main(args: Array<String>) {
                3.solveExample(
                    exampleNumber = 1, expectedSolution = getFile(
                        level = 3, part = "2-example-1", fileType = FileType.SOLUTION
                    ).readLines().first()
                ) {
                    sumGroupBadgePriorities()
                        .toString()
                }
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            3.solve(2) {
                sumGroupBadgePriorities()
                    .toString()
            }
        }
    }
}
