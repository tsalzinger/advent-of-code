package puzzles.puzzle11

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle11.CosmicExpansion.getSumOfGalaxyPairDistances
import org.junit.jupiter.api.Test

class CosmicExpansionTest {
    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle11/puzzle-11-example-1.in"
            .streamInput()
            .getSumOfGalaxyPairDistances()
            .assertThat {
                isEqualTo(374L)
            }
    }
}
