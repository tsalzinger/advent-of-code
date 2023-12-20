package puzzles.puzzle20

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle20.PulsePropagation.getNumberOfPulsesAfter1000Iterations
import org.junit.jupiter.api.Test

class PulsePropagationTest {
    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle20/puzzle-20-example-1.in"
            .streamInput()
            .getNumberOfPulsesAfter1000Iterations()
            .assertThat {
                isEqualTo(32000000L)
            }
    }

    @Test
    fun `example 2 - part 1`() {
        "puzzles/puzzle20/puzzle-20-example-2.in"
            .streamInput()
            .getNumberOfPulsesAfter1000Iterations()
            .assertThat {
                isEqualTo(11687500L)
            }
    }
}
