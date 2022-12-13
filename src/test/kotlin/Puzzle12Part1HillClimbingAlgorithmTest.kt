import me.salzinger.common.streamInput
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import puzzles.Puzzle12HillClimbingAlgorithm.Part1.solve

class Puzzle12Part1HillClimbingAlgorithmTest {
    @TestFactory
    fun exampleTests(): Iterable<DynamicNode> {
        return listOf(
            "puzzle-12-example-1.in" to 31
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
