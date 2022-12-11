import me.salzinger.common.streamInput
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import puzzles.Puzzle10CathodeRayTube.Part2.solve

class Puzzle11Part1MonkeyInTheMiddleTest {
    @TestFactory
    fun examples(): Iterable<DynamicNode> {
        return listOf(
            "1" to 10605
        )
            .map { (example, expectedSolution) ->
                DynamicTest.dynamicTest("example-$example") {
                    "puzzle-11-example-$example.in"
                        .streamInput()
                        .solve()
                        .assertThat {
                            isEqualTo(expectedSolution)
                        }
                }
            }
    }
}


