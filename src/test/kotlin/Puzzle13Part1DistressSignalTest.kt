import me.salzinger.common.streamInput
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import puzzles.PacketData
import puzzles.Puzzle13DistressSignal.Part1.solve
import puzzles.Puzzle13DistressSignal.toPacketData

class Puzzle13Part1DistressSignalTest {
    @TestFactory
    fun exampleTests(): Iterable<DynamicNode> {
        return listOf(
            "puzzle-13-example-1.in" to 13
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

    @Test
    fun emptyPacketData() {
        "[]".toPacketData()
            .assertThat {
                isEqualTo(packetOf { })
            }
    }

    @Test
    fun packetOfEmptyPacketData() {
        "[[]]".toPacketData()
            .assertThat {
                isEqualTo(
                    packetOf {
                        packetData { }
                    }
                )
            }
    }

    @Test
    fun complexPacket() {
        "[[1,2,[3,4,5],6,[7,8]],9]".toPacketData()
            .assertThat {
                isEqualTo(
                    packetOf {
                        packetData {
                            primitive(1)
                            primitive(2)
                            packetData {
                                primitive(3)
                                primitive(4)
                                primitive(5)
                            }
                            primitive(6)
                            packetData {
                                primitive(7)
                                primitive(8)
                            }
                        }
                        primitive(9)
                    }
                )
            }
    }

    fun packetOf(block: PacketBuilder.() -> Unit): PacketData {
        return PacketBuilder().apply(block).build()
    }

    interface PacketManipulator {
        fun packetData(block: PacketManipulator.() -> Unit)
        fun primitive(value: Int)
    }

    class PacketBuilder : PacketManipulator {
        val packets = mutableListOf<PacketData>()
        override fun packetData(block: PacketManipulator.() -> Unit) {
            packets.add(PacketBuilder().apply(block).build())
        }

        override fun primitive(value: Int) {
            packets.add(PacketData.Primitive(value))
        }

        fun build(): PacketData {
            return PacketData.Packet(packets.toList())
        }
    }

}
