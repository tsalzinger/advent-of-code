import me.salzinger.common.streamInput
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import puzzles.Puzzle14RegolithReservoir.Part2.solve

class Puzzle14Part2RegolithReservoirTest {
    @TestFactory
    fun exampleTests(): Iterable<DynamicNode> {
        return listOf(
            "puzzle-14-example-1.in" to 93
        ).map { (exampleInputFileName, expectedSolution) ->
            DynamicTest.dynamicTest(exampleInputFileName) {
                exampleInputFileName
                    .streamInput()
                    .solve()
                    .assertThat {
                        isEqualTo(expectedSolution)
                    }
            }
        }
    }
}
