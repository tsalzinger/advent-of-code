package puzzles

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.Puzzle16ProboscideaVolcanium.Part1.solve
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class Puzzle16ProboscideaVolcaniumTest {
    @TestFactory
    fun exampleTests(): Iterable<DynamicNode> {
        return listOf(
            "puzzle-16-example-1.in" to 1651
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
