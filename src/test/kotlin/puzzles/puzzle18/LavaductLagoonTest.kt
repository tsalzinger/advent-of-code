package puzzles.puzzle18

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle18.LavaductLagoon.getLagoonSize
import org.junit.jupiter.api.Test

class LavaductLagoonTest {
    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle18/puzzle-18-example-1.in"
            .streamInput()
            .getLagoonSize()
            .assertThat {
                isEqualTo(62)
            }
    }
}
