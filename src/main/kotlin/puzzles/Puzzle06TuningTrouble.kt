package puzzles

import me.salzinger.common.solve

object Puzzle06TuningTrouble {
    private const val START_OF_PACKET_MARKER_LENGTH = 4
    private const val START_OF_MESSAGE_MARKER_LENGTH = 14

    fun String.findEndOfStartOfPacketMarker(): Int {
        foldIndexed("") { index, acc, c ->
            if (acc.count() >= START_OF_PACKET_MARKER_LENGTH) {
                if (acc.toSet().count() == START_OF_PACKET_MARKER_LENGTH) {
                    return index
                } else {
                    acc.drop(1) + c
                }
            } else {
                acc + c
            }
        }

        throw RuntimeException("No StartOfPacket marker found in $this")
    }

    fun String.findEndOfStartOfMessageMarker(): Int {
        foldIndexed("") { index, acc, c ->
            if (acc.count() >= START_OF_MESSAGE_MARKER_LENGTH) {
                if (acc.toSet().count() == START_OF_MESSAGE_MARKER_LENGTH) {
                    return index
                } else {
                    acc.drop(1) + c
                }
            } else {
                acc + c
            }
        }

        throw RuntimeException("No StartOfPacket marker found in $this")
    }

    object Part1 {
        @JvmStatic
        fun main(args: Array<String>) {
            6.solve(1) {
                first()
                    .findEndOfStartOfPacketMarker()
                    .toString()
            }
        }
    }

    object Part2 {
        @JvmStatic
        fun main(args: Array<String>) {
            6.solve(2) {
                first()
                    .findEndOfStartOfMessageMarker()
                    .toString()
            }
        }
    }
}
