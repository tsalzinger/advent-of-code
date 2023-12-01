package puzzles.puzzle1

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle1.Trebuchet.getCalibrationValue
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import me.salzinger.puzzles.puzzle1.Trebuchet.Part1.solve as solvePart1

class TrebuchetTests {
    @TestFactory
    fun `get calibration values`(): Iterable<DynamicNode> {
        return listOf(
            "1abc2" to 12,
            "pqr3stu8vwx" to 38,
            "a1b2c3d4e5f" to 15,
            "treb7uchet" to 77,
        ).map { (input, expectedCalibrationValue) ->
            DynamicTest.dynamicTest("get calibaration value of $input") {
                input
                    .getCalibrationValue()
                    .assertThat {
                        isEqualTo(expectedCalibrationValue)
                    }
            }
        }
    }

    @Test
    fun `example 1`() {
        "puzzles/puzzle1/puzzle-1-example-1.in"
            .streamInput()
            .solvePart1()
            .assertThat {
                isEqualTo(142)
            }
    }
}
