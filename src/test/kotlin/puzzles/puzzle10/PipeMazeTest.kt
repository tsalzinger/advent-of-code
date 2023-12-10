package puzzles.puzzle10

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle10.PipeMaze.getStepsToFarthestPipe
import org.junit.jupiter.api.Test

class PipeMazeTest {

    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle10/puzzle-10-example-1.in"
            .streamInput()
            .getStepsToFarthestPipe()
            .assertThat {
                isEqualTo(4)
            }
    }

    @Test
    fun `example 2 - part 1`() {
        "puzzles/puzzle10/puzzle-10-example-2.in"
            .streamInput()
            .getStepsToFarthestPipe()
            .assertThat {
                isEqualTo(8)
            }
    }
}
