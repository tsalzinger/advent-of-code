package puzzles.puzzle24

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle24.NeverTellMeTheOdds
import me.salzinger.puzzles.puzzle24.NeverTellMeTheOdds.getNumberOfFutureXYIntersectionsWithin
import org.junit.jupiter.api.Test

class NeverTellMeTheOddsTest {
    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle24/puzzle-24-example-1.in"
            .streamInput()
            .getNumberOfFutureXYIntersectionsWithin(
                7L,
                27L,
            )
            .assertThat {
                isEqualTo(2)
            }
    }

    @Test
    fun intersect() {
        val a =
            NeverTellMeTheOdds.Line2D(
                NeverTellMeTheOdds.Point2D(
                    "234633262682615.0".toBigDecimal(),
                    "303415790107031.0".toBigDecimal(),
                ),
                NeverTellMeTheOdds.Point2D(
                    "-26.0".toBigDecimal(),
                    "-65.0".toBigDecimal(),
                ),
            )
        val b =
            NeverTellMeTheOdds.Line2D(
                NeverTellMeTheOdds.Point2D(
                    "150110943303191.0".toBigDecimal(),
                    "250617751235851.0".toBigDecimal(),
                ),
                NeverTellMeTheOdds.Point2D(
                    "-32.0".toBigDecimal(),
                    "-81.0".toBigDecimal(),
                ),
            )

        a.getIntersectionWith(b)
            .assertThat {
                isNotNull
            }

//        NeverTellMeTheOdds.Point2D(, 303415790107031.0 @ -26.0, -65.0
//        Hailstone B: 150110943303191.0, 250617751235851.0 @ -32.0, -81.0
//        Hailstones' paths are parallel; they never intersect.
    }
}
