package puzzles.puzzle8

import assertIterable
import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle8.HauntedWasteland
import me.salzinger.puzzles.puzzle8.HauntedWasteland.getParallelStepCountWithLeftRightInstructions
import me.salzinger.puzzles.puzzle8.HauntedWasteland.getStepCountWithLeftRightInstructions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class HauntedWastelandTest {

    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle8/puzzle-8-example-1.in"
            .streamInput()
            .getStepCountWithLeftRightInstructions()
            .assertThat {
                isEqualTo(2)
            }
    }

    @Test
    fun `example 2 - part 1`() {
        "puzzles/puzzle8/puzzle-8-example-2.in"
            .streamInput()
            .getStepCountWithLeftRightInstructions()
            .assertThat {
                isEqualTo(6)
            }
    }

    @Test
    fun `example 3 - part 2`() {
        "puzzles/puzzle8/puzzle-8-example-3.in"
            .streamInput()
            .getParallelStepCountWithLeftRightInstructions()
            .assertThat {
                isEqualTo(6)
            }
    }


    @Nested
    inner class LeftRightInstructions {
        @Test
        fun `loops indefinitely`() {
            HauntedWasteland.LeftRightInstructions(
                listOf('A', 'B', 'B')
            ).take(7)
                .toList()
                .assertIterable {
                    containsExactly(
                        'A',
                        'B',
                        'B',
                        'A',
                        'B',
                        'B',
                        'A',
                    )
                }
        }
    }
}
