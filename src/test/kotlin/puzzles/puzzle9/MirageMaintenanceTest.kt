package puzzles.puzzle9

import assertIterable
import assertThat
import me.salzinger.common.extensions.toIntList
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle9.MirageMaintenance.getStackOfStepDifferences
import me.salzinger.puzzles.puzzle9.MirageMaintenance.getSumOfExtrapolatedValues
import me.salzinger.puzzles.puzzle9.MirageMaintenance.predictNextValue
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import java.util.*

class MirageMaintenanceTest {
    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle9/puzzle-9-example-1.in"
            .streamInput()
            .getSumOfExtrapolatedValues()
            .assertThat {
                isEqualTo(114L)
            }
    }

    @Test
    fun `example 1`() {
        "puzzles/puzzle9/puzzle-9-example-1.in"
            .streamInput()
            .map {
                it.predictNextValue()
            }
            .assertIterable {
                containsExactly(
                    18,
                    28,
                    68,
                )
            }
    }

    @Test
    fun `getStackOfStepDifferences - example 1`() {
        "puzzles/puzzle9/puzzle-9-example-1.in"
            .streamInput()
            .map {
                it.toIntList(" ")
            }
            .map {
                it.getStackOfStepDifferences()
            }
            .assertIterable {
                containsExactly(
                    Stack<List<Int>>().apply {
                        push(listOf(0, 3, 6, 9, 12, 15))
                        push(listOf(3, 3, 3, 3, 3))
                        push(listOf(0, 0, 0, 0))
                    },
                    Stack<List<Int>>().apply {
                        push(listOf(1, 3, 6, 10, 15, 21))
                        push(listOf(2, 3, 4, 5, 6))
                        push(listOf(1, 1, 1, 1))
                        push(listOf(0, 0, 0))
                    },
                    Stack<List<Int>>().apply {
                        push(listOf(10, 13, 16, 21, 30, 45))
                        push(listOf(3, 3, 5, 9, 15))
                        push(listOf(0, 2, 4, 6))
                        push(listOf(2, 2, 2))
                        push(listOf(0, 0))
                    },
                )
            }
    }

    @TestFactory
    fun `predictNextValue`(): Iterable<DynamicTest> {
        return listOf(
            Stack<List<Int>>().apply {
                push(listOf(0, 3, 6, 9, 12, 15))
                push(listOf(3, 3, 3, 3, 3))
                push(listOf(0, 0, 0, 0))
            } to 18,
            Stack<List<Int>>().apply {
                push(listOf(1, 3, 6, 10, 15, 21))
                push(listOf(2, 3, 4, 5, 6))
                push(listOf(1, 1, 1, 1))
                push(listOf(0, 0, 0))
            } to 28,
            Stack<List<Int>>().apply {
                push(listOf(10, 13, 16, 21, 30, 45))
                push(listOf(3, 3, 5, 9, 15))
                push(listOf(0, 2, 4, 6))
                push(listOf(2, 2, 2))
                push(listOf(0, 0))
            } to 68,
        ).mapIndexed { index, (stack, expectedValue) ->
            DynamicTest.dynamicTest("Predict value for stack $index") {
                stack.predictNextValue()
                    .assertThat {
                        isEqualTo(expectedValue)
                    }
            }
        }
    }

}
