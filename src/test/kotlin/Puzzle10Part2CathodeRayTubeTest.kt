import me.salzinger.common.streamInput
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import puzzles.Puzzle10CathodeRayTube.Part2.solve

class Puzzle10Part2CathodeRayTubeTest {
    @TestFactory
    fun examples(): Iterable<DynamicNode> {
        return listOf(
            "1" to """
            ##..##..##..##..##..##..##..##..##..##..
            ###...###...###...###...###...###...###.
            ####....####....####....####....####....
            #####.....#####.....#####.....#####.....
            ######......######......######......####
            #######.......#######.......#######.....
        """.trimIndent()
        )
            .map { (example, expectedSolution) ->
                DynamicTest.dynamicTest("example-$example") {
                    "puzzle-10-example-$example.in"
                        .streamInput()
                        .solve()
                        .assertThat {
                            isEqualTo(expectedSolution)
                        }
                }
            }
    }
}


