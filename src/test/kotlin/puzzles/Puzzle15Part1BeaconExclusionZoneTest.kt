package puzzles

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.Puzzle15BeaconExclusionZone.Part1.solve
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class Puzzle15Part1BeaconExclusionZoneTest {
    @TestFactory
    fun exampleTests(): Iterable<DynamicNode> {
        return listOf(
            Triple("puzzle-15-example-1.in", 10, 26)
        ).map { (exampleInputFileName, yToScan, expectedSolution) ->
            DynamicTest.dynamicTest(exampleInputFileName) {
                exampleInputFileName
                    .streamInput()
                    .solve(yToScan = yToScan)
                    .assertThat {
                        isEqualTo(expectedSolution)
                    }
            }
        }
    }
}
