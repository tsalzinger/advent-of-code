package puzzles.puzzle6

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle6.WaitForIt.getProductOfNumberOfWaysToBeatRecordDistance
import org.junit.jupiter.api.Test

class WaitForItTest {

    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle6/puzzle-6-example-1.in"
            .streamInput()
            .getProductOfNumberOfWaysToBeatRecordDistance()
            .assertThat {
                isEqualTo(288L)
            }
    }
}
