package puzzles.puzzle14

import assertIterable
import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle14.ParabolicReflectorDish.getTotalLoadOnNorthBeamAfterATiltToNorth
import me.salzinger.puzzles.puzzle14.ParabolicReflectorDish.tiltNorth
import org.junit.jupiter.api.Test

class ParabolicReflectorDishTest {

    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle14/puzzle-14-example-1.in"
            .streamInput()
            .getTotalLoadOnNorthBeamAfterATiltToNorth()
            .assertThat {
                isEqualTo(136)
            }
    }

    @Test
    fun `example 2 - part 1`() {
        "puzzles/puzzle14/puzzle-14-example-2.in"
            .streamInput()
            .getTotalLoadOnNorthBeamAfterATiltToNorth()
            .assertThat {
                isEqualTo(136)
            }
    }

    @Test
    fun `example 1 - tilt north`() {
        "puzzles/puzzle14/puzzle-14-example-1.in"
            .streamInput()
            .tiltNorth()
            .assertIterable {
                containsExactlyElementsOf(
                    "puzzles/puzzle14/puzzle-14-example-2.in"
                        .streamInput()
                        .toList()
                )
            }
    }

    @Test
    fun `example 2 - tilt north`() {
        "puzzles/puzzle14/puzzle-14-example-2.in"
            .streamInput()
            .tiltNorth()
            .assertIterable {
                containsExactlyElementsOf(
                    "puzzles/puzzle14/puzzle-14-example-2.in"
                        .streamInput()
                        .toList()
                )
            }
    }
}
