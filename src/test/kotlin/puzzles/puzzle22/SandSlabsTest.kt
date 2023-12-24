package puzzles.puzzle22

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle22.SandSlabs.getNumberOfBricksWhichCanSafelyBeDisintegrated
import org.junit.jupiter.api.Test

class SandSlabsTest {
    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle22/puzzle-22-example-1.in"
            .streamInput()
            .getNumberOfBricksWhichCanSafelyBeDisintegrated()
            .assertThat {
                isEqualTo(5)
            }
    }
}
