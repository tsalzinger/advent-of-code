package puzzles.puzzle14

import assertIterable
import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle14.ParabolicReflectorDish.getTotalLoadOnNorthBeamAfterATiltToNorth
import me.salzinger.puzzles.puzzle14.ParabolicReflectorDish.getTotalLoadOnNorthBeamAfterSpins
import me.salzinger.puzzles.puzzle14.ParabolicReflectorDish.spin
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
    fun `example 1 - part 2`() {
        "puzzles/puzzle14/puzzle-14-example-1.in"
            .streamInput()
            .getTotalLoadOnNorthBeamAfterSpins(1_000_000_000)
            .assertThat {
                isEqualTo(64)
            }
    }

    @Test
    fun `example 1 - spin once`() {
        "puzzles/puzzle14/puzzle-14-example-1.in"
            .streamInput()
            .spin(1)
            .joinToString("\n")
            .assertThat {
                isEqualTo(
                    """
                        .....#....
                        ....#...O#
                        ...OO##...
                        .OO#......
                        .....OOO#.
                        .O#...O#.#
                        ....O#....
                        ......OOOO
                        #...O###..
                        #..OO#....
                    """.trimIndent()
                )
            }
    }

    @Test
    fun `example 1 - spin twice`() {
        "puzzles/puzzle14/puzzle-14-example-1.in"
            .streamInput()
            .spin(2)
            .joinToString("\n")
            .assertThat {
                isEqualTo(
                    """
                        .....#....
                        ....#...O#
                        .....##...
                        ..O#......
                        .....OOO#.
                        .O#...O#.#
                        ....O#...O
                        .......OOO
                        #..OO###..
                        #.OOO#...O
                    """.trimIndent()
                )
            }
    }

    @Test
    fun `example 1 - spin three times`() {
        "puzzles/puzzle14/puzzle-14-example-1.in"
            .streamInput()
            .spin(3)
            .joinToString("\n")
            .assertThat {
                isEqualTo(
                    """
                        .....#....
                        ....#...O#
                        .....##...
                        ..O#......
                        .....OOO#.
                        .O#...O#.#
                        ....O#...O
                        .......OOO
                        #...O###.O
                        #.OOO#...O
                    """.trimIndent()
                )
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
