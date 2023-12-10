package puzzles.puzzle10

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle10.PipeMaze.getStepsToFarthestPipe
import me.salzinger.puzzles.puzzle10.PipeMaze.getTilesCountContainedWithinLoop
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

    @Test
    fun `example 1 - part 2`() {
        "puzzles/puzzle10/puzzle-10-example-1.in"
            .streamInput()
            .getTilesCountContainedWithinLoop()
            .assertThat {
                isEqualTo(1)
            }
    }

    @Test
    fun `example 2 - part 2`() {
        "puzzles/puzzle10/puzzle-10-example-2.in"
            .streamInput()
            .getTilesCountContainedWithinLoop()
            .assertThat {
                isEqualTo(1)
            }
    }

    @Test
    fun `example 3 - part 2`() {
        "puzzles/puzzle10/puzzle-10-example-3.in"
            .streamInput()
            .getTilesCountContainedWithinLoop()
            .assertThat {
                isEqualTo(4)
            }
    }

    @Test
    fun `example 4 - part 2`() {
        "puzzles/puzzle10/puzzle-10-example-4.in"
            .streamInput()
            .getTilesCountContainedWithinLoop()
            .assertThat {
                isEqualTo(4)
            }
    }

    @Test
    fun `example 5 - part 2`() {
        "puzzles/puzzle10/puzzle-10-example-5.in"
            .streamInput()
            .getTilesCountContainedWithinLoop()
            .assertThat {
                isEqualTo(8)
            }
    }

    @Test
    fun `example 6 - part 2`() {
        "puzzles/puzzle10/puzzle-10-example-6.in"
            .streamInput()
            .getTilesCountContainedWithinLoop()
            .assertThat {
                isEqualTo(10)
            }
    }
}
