package puzzles.puzzle13

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle13.PointOfIncidence.getSummaryOfNotes
import me.salzinger.puzzles.puzzle13.PointOfIncidence.getSummaryOfNotesAfterCorrectingTheSmudge
import me.salzinger.puzzles.puzzle13.PointOfIncidence.transpose
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class PointOfIncidenceTest {
    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle13/puzzle-13-example-1.in"
            .streamInput()
            .getSummaryOfNotes()
            .assertThat {
                isEqualTo(405)
            }
    }

    @Test
    fun `example 2 - part 1`() {
        "puzzles/puzzle13/puzzle-13-example-2.in"
            .streamInput()
            .getSummaryOfNotes()
            .assertThat {
                isEqualTo(5)
            }
    }

    @Test
    fun `example 3 - part 1`() {
        "puzzles/puzzle13/puzzle-13-example-3.in"
            .streamInput()
            .getSummaryOfNotes()
            .assertThat {
                isEqualTo(400)
            }
    }

    @Test
    fun `example 1 - part 2`() {
        "puzzles/puzzle13/puzzle-13-example-1.in"
            .streamInput()
            .getSummaryOfNotesAfterCorrectingTheSmudge()
            .assertThat {
                isEqualTo(400)
            }
    }

    @Test
    fun `example 2 - part 2`() {
        "puzzles/puzzle13/puzzle-13-example-2.in"
            .streamInput()
            .getSummaryOfNotesAfterCorrectingTheSmudge()
            .assertThat {
                isEqualTo(300)
            }
    }

    @Test
    fun `example 3 - part 2`() {
        "puzzles/puzzle13/puzzle-13-example-3.in"
            .streamInput()
            .getSummaryOfNotesAfterCorrectingTheSmudge()
            .assertThat {
                isEqualTo(100)
            }
    }

    @TestFactory
    fun transpose(): Iterable<DynamicNode> {
        return listOf(
            listOf("AB") to listOf("A", "B"),
            listOf("ABC", "DEF", "GHI") to listOf("ADG", "BEH", "CFI")
        ).map { (original, transposed) ->
            DynamicTest.dynamicTest("transpose $original") {
                original
                    .transpose()
                    .assertThat {
                        isEqualTo(transposed)
                    }
            }
        }
    }
}
