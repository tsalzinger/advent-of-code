import me.salzinger.common.streamInput
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import puzzles.Puzzle14RegolithReservoir.Part1.solve

class Puzzle14Part1RegolithReservoirTest {
    @TestFactory
    fun exampleTests(): Iterable<DynamicNode> {
        return listOf(
            "puzzle-14-example-1.in" to 24
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
