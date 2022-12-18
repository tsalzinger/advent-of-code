package puzzles

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.Puzzle15BeaconExclusionZone.Part2.solve
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class Puzzle15Part2BeaconExclusionZoneTest {
    @TestFactory
    fun exampleTests(): Iterable<DynamicNode> {
        return listOf(
//            Triple("puzzle-15-example-1.in", 0..20, 56000011.toBigInteger())
            Triple("puzzle-15.in", 0..4_000_000, 56000011.toBigInteger())
        ).map { (exampleInputFileName, searchRange, expectedSolution) ->
            DynamicTest.dynamicTest(exampleInputFileName) {
                exampleInputFileName
                    .streamInput()
                    .solve(searchRange)
                    .assertThat {
                        isEqualTo(expectedSolution)
                    }
            }
        }
    }
}
