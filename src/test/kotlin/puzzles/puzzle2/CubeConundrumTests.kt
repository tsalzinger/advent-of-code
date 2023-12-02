package puzzles.puzzle2

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle2.CubeConundrum
import me.salzinger.puzzles.puzzle2.CubeConundrum.getSumOfGamePowers
import me.salzinger.puzzles.puzzle2.CubeConundrum.getSumOfPossibleGameIds
import org.junit.jupiter.api.Test

class CubeConundrumTests {
    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle2/puzzle-2-example-1.in"
            .streamInput()
            .getSumOfPossibleGameIds(
                mapOf(
                    CubeConundrum.Color.RED to 12,
                    CubeConundrum.Color.GREEN to 13,
                    CubeConundrum.Color.BLUE to 14,
                )
            )
            .assertThat {
                isEqualTo(8)
            }
    }

    @Test
    fun `example 1 - part 2`() {
        "puzzles/puzzle2/puzzle-2-example-1.in"
            .streamInput()
            .getSumOfGamePowers()
            .assertThat {
                isEqualTo(2286)
            }
    }

}
