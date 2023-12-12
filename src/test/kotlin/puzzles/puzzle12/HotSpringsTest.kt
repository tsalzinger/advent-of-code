package puzzles.puzzle12

import assertIterable
import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle12.HotSprings.SpringGroup.Companion.toSpringGroup
import me.salzinger.puzzles.puzzle12.HotSprings.getPossibleSpringArrangements
import me.salzinger.puzzles.puzzle12.HotSprings.getSumOfPossibleSpringArrangements
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class HotSpringsTest {
    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle12/puzzle-12-example-1.in"
            .streamInput()
            .getSumOfPossibleSpringArrangements()
            .assertThat {
                isEqualTo(6L)
            }
    }

    @Test
    fun `example 2 - part 1`() {
        "puzzles/puzzle12/puzzle-12-example-2.in"
            .streamInput()
            .getSumOfPossibleSpringArrangements()
            .assertThat {
                isEqualTo(21L)
            }
    }

    @TestFactory
    fun `groupFits`(): Iterable<DynamicNode> {
        return listOf(
            "??? 1" to 3,
            "?#? 1" to 1,
            "#?# 1" to 0,
            "#?# 1,1" to 1,
            "??.? 1,1" to 2,
            "?.?.? 1" to 3,
            "?.?.? 1,1" to 3,
            "#.?.? 1,1" to 2,
            "#.#.? 1,1" to 1,
            "#.?.# 1,1" to 1,
            "??...??..#..#...? 1,1" to 1,
            ".??..?...?##. 1,1,3" to 2,
            "???.### 1,1,3" to 1,
            ".??..??...?##. 1,1,3" to 4,
            "?#?#?#?#?#?#?#? 1,3,1,6" to 1,
            "????.#...#... 4,1,1" to 1,
            "????.######..#####. 1,6,5" to 4,
            "?###???????? 3,2,1" to 10,
        ).map { (row, expectedNumber) ->
            DynamicTest.dynamicTest("$row has $expectedNumber possibilities") {
                row.getPossibleSpringArrangements()
                    .assertThat {
                        isEqualTo(expectedNumber)
                    }
            }
        }
    }


    @TestFactory
    fun `remaining possible groups`(): Iterable<DynamicNode> {
        return listOf(
            ("????" to 1) to listOf("??", "?", "", ""),
            ("???#" to 1) to listOf("?#", "#", ""),
            ("####" to 1) to emptyList(),
            ("???" to 1) to listOf("?", "", ""),
            ("??#" to 1) to listOf("#", ""),
            ("?#?" to 1) to listOf(""),
            ("#??" to 1) to listOf("?"),
            ("???" to 2) to listOf("", ""),
            ("??#" to 2) to listOf(""),
            ("?#?" to 2) to listOf("", ""),
            ("#??" to 2) to listOf(""),
        ).map { (input, expectedResult) ->
            val (row, groupLength) = input

            DynamicTest.dynamicTest("$row, $groupLength") {
                val springGroup = row.toSpringGroup()

                springGroup
                    .getRemainingPossibleGroups(groupLength)
                    .map {
                        it.toString()
                    }
                    .assertIterable {
                        containsExactlyElementsOf(
                            expectedResult
                        )
                    }

            }
        }
    }
}
