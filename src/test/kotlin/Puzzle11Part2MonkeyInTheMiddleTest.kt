import me.salzinger.common.streamInput
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import puzzles.Puzzle11MonkeyInTheMiddle.Part2.solve
import java.math.BigInteger

class Puzzle11Part2MonkeyInTheMiddleTest {
    @TestFactory
    fun examples(): Iterable<DynamicNode> {
        return listOf(
            "1" to BigInteger("2713310158")
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


