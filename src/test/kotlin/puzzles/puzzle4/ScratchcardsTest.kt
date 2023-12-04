package puzzles.puzzle4

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle4.Scratchcards.sumOfPoints
import me.salzinger.puzzles.puzzle4.Scratchcards.sumOfScratchcards
import me.salzinger.puzzles.puzzle4.Scratchcards.toScratchcards
import org.junit.jupiter.api.Test

class ScratchcardsTest {
    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle4/puzzle-4-example-1.in"
            .streamInput()
            .toScratchcards()
            .sumOfPoints()
            .assertThat {
                isEqualTo(13L)
            }
    }

    @Test
    fun `example 1 - part 2`() {
        "puzzles/puzzle4/puzzle-4-example-1.in"
            .streamInput()
            .toScratchcards()
            .sumOfScratchcards()
            .assertThat {
                isEqualTo(30L)
            }
    }
}
