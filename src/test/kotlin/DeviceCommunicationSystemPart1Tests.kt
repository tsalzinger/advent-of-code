import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import puzzles.Puzzle06TuningTrouble.findEndOfStartOfPacketMarker

class DeviceCommunicationSystemPart1Tests {
    @TestFactory
    fun testStartOfPacketMarkerDetection(): List<DynamicNode> {
        return listOf(
            "mjqjpqmgbljsphdztnvjfqwrcgsmlb" to 7,
            "bvwbjplbgvbhsrlpgdmjqwftvncz" to 5,
            "nppdvjthqldpwncqszvftbrmjlhg" to 6,
            "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg" to 10,
            "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw" to 11,
        ).map { (dataStreamBuffer, expectedSolution) ->
            DynamicTest.dynamicTest("StartOfPacket marker detection: $dataStreamBuffer") {
                Assertions.assertThat(
                    dataStreamBuffer
                        .findEndOfStartOfPacketMarker()
                ).isEqualTo(expectedSolution)
            }
        }
    }
}
