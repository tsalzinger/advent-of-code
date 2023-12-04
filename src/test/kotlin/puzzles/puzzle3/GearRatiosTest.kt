package puzzles.puzzle3

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle3.GearRatios.getSumOfAllNumbersAdjacentToASymbol
import me.salzinger.puzzles.puzzle3.GearRatios.parseAsGameGrid
import org.junit.jupiter.api.Test

class GearRatiosTest {
    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle3/puzzle-3-example-1.in"
            .streamInput()
            .parseAsGameGrid()
            .getSumOfAllNumbersAdjacentToASymbol()
            .assertThat {
                isEqualTo(4361)
            }
    }
}
