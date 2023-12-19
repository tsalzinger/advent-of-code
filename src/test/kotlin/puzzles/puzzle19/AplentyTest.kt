package puzzles.puzzle19

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle19.Aplenty.getTotalSumOfPartRatings
import org.junit.jupiter.api.Test

class AplentyTest {
    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle19/puzzle-19-example-1.in"
            .streamInput()
            .getTotalSumOfPartRatings()
            .assertThat {
                isEqualTo(19114L)
            }
    }
}
