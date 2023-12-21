package puzzles.puzzle21

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle21.StepCounter.getNumberOfReachableTilesAfterSteps
import org.junit.jupiter.api.Test

class StepCounterTest {
    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle21/puzzle-21-example-1.in"
            .streamInput()
            .getNumberOfReachableTilesAfterSteps(stepCount = 6)
            .assertThat {
                isEqualTo(16)
            }
    }
}
