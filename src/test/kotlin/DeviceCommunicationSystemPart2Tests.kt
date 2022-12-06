import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import puzzles.Puzzle06TuningTrouble.findEndOfStartOfMessageMarker

class DeviceCommunicationSystemPart2Tests {
    @TestFactory
    fun testStartOfMessageMarkerDetection(): List<DynamicNode> {
        return listOf(
            "mjqjpqmgbljsphdztnvjfqwrcgsmlb" to 19,
            "bvwbjplbgvbhsrlpgdmjqwftvncz" to 23,
            "nppdvjthqldpwncqszvftbrmjlhg" to 23,
            "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg" to 29,
            "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw" to 26,
        ).map { (dataStreamBuffer, expectedSolution) ->
            DynamicTest.dynamicTest("StartOfMessage marker detection: $dataStreamBuffer") {
                Assertions.assertThat(
                    dataStreamBuffer
                        .findEndOfStartOfMessageMarker()
                ).isEqualTo(expectedSolution)
            }
        }
    }
}
