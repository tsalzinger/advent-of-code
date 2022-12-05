package puzzles

import me.salzinger.common.FileType
import me.salzinger.common.extensions.toPairs
import me.salzinger.common.getFile
import me.salzinger.common.solve
import me.salzinger.common.solveExample
import puzzles.Puzzle04CampCleanup.Part1.toRangePair

object Puzzle04CampCleanup {


    object Part1 {

        fun IntRange.fullyContains(range: IntRange): Boolean {
            return first <= range.first && last >= range.last
        }

        fun String.toRange(): IntRange {
            return split("-")
                .map { it.toInt() }
                .let { (start, end) ->
                    start..end
                }
        }

        fun String.toRangePair(): Pair<IntRange, IntRange> {
            return split(",")
                .map { it.toRange() }
                .toPairs()
                .single()
        }

        fun List<String>.countFullyContainedRanges(): Int {
            return map {
                it.toRangePair()
            }.count { (firstRange, secondRange) ->
                firstRange.fullyContains(secondRange) || secondRange.fullyContains(firstRange)
            }
        }

        object Example1 {
            @JvmStatic
            fun main(args: Array<String>) {
                4.solveExample(
                    exampleNumber = 1, expectedSolution = getFile(
                        level = 4, part = "1-example-1", fileType = FileType.SOLUTION
                    ).readLines().first()
                ) {
                    countFullyContainedRanges()
                        .toString()
                }
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            4.solve(1) {
                countFullyContainedRanges()
                    .toString()
            }
        }
    }

    object Part2 {

        fun IntRange.overlaps(range: IntRange): Boolean {
            return first <= range.last && last >= range.first
        }

        fun List<String>.countOverlappingRanges(): Int {
            return map {
                it.toRangePair()
            }.count { (firstRange, secondRange) ->
                firstRange.overlaps(secondRange)
            }
        }

        object Example1 {
            @JvmStatic
            fun main(args: Array<String>) {
                4.solveExample(
                    exampleNumber = 1, expectedSolution = getFile(
                        level = 4, part = "2-example-1", fileType = FileType.SOLUTION
                    ).readLines().first()
                ) {
                    countOverlappingRanges()
                        .toString()
                }
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            4.solve(2) {
                countOverlappingRanges()
                    .toString()
            }
        }
    }
}
