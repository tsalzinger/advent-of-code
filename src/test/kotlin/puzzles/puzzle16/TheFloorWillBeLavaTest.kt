package puzzles.puzzle16

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle16.TheFloorWillBeLava.getCountOfEnergizedTiles
import me.salzinger.puzzles.puzzle16.TheFloorWillBeLava.getMaxCountOfEnergizedTiles
import org.junit.jupiter.api.Test

class TheFloorWillBeLavaTest {
    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle16/puzzle-16-example-1.in"
            .streamInput()
            .getCountOfEnergizedTiles()
            .assertThat {
                isEqualTo(46)
            }
    }

    @Test
    fun `example 1 - part 2`() {
        "puzzles/puzzle16/puzzle-16-example-1.in"
            .streamInput()
            .getMaxCountOfEnergizedTiles()
            .assertThat {
                isEqualTo(51)
            }
    }
}
