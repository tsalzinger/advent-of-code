package puzzles

import me.salzinger.common.streamInput

sealed interface PacketData : Comparable<PacketData> {

    data class Packet(val values: List<PacketData>) : PacketData, List<PacketData> by values {
        override fun compareTo(other: PacketData): Int {
            return when (other) {
                is Packet -> zip(other)
                    .firstNotNullOfOrNull { (first, second) ->
                        first.compareTo(second)
                            .let { compareToResult ->
                                if (compareToResult == 0) {
                                    null
                                } else {
                                    compareToResult
                                }
                            }
                    } ?: count().compareTo(other.count())

                is Primitive -> {
                    compareTo(other.asList())
                }
            }
        }
    }

    data class Primitive(val value: Int) : PacketData {
        fun asList(): Packet {
            return Packet(listOf(this))
        }

        override fun compareTo(other: PacketData): Int {
            return when (other) {
                is Packet -> {
                    asList().compareTo(other)
                }

                is Primitive -> {
                    value.compareTo(other.value)
                }
            }
        }
    }
}

object Puzzle13DistressSignal {

    fun Iterator<Char>.toPacketData(): PacketData {
        return buildList {
            val valueBuilder = StringBuilder()

            fun StringBuilder.consume() {
                toString()
                    .toIntOrNull()
                    ?.let(PacketData::Primitive)
                    ?.let(::add)

                valueBuilder.clear()
            }

            while (hasNext()) {
                when (val item = next()) {
                    '[' -> add(toPacketData())
                    ']' -> {
                        valueBuilder.consume()
                        return@buildList
                    }

                    ',' -> valueBuilder.consume()
                    else -> valueBuilder.append(item)
                }
            }
        }.let(PacketData::Packet)
    }

    fun String.toPacketData(): PacketData {
        return drop(1).iterator().toPacketData()
    }

    object Part1 {

        fun Sequence<String>.solve(): Int {
            return filter { it.isNotBlank() }
                .map { it.toPacketData() }
                .chunked(2)
                .foldIndexed(0) { index, sum, (first, second) ->
                    val compareToResult = first.compareTo(second)
                    when {
                        compareToResult < 0 -> {
                            sum + index + 1
                        }

                        else -> {
                            sum
                        }
                    }
                }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            "puzzle-13.in"
                .streamInput()
                .solve()
                .let(::println)
        }
    }

    object Part2 {

        fun Sequence<String>.solve(): Int {
            val firstDividerPacket = PacketData.Packet(listOf(PacketData.Primitive(2)))
            val secondDividerPacket = PacketData.Packet(listOf(PacketData.Primitive(6)))

            return filter { it.isNotBlank() }
                .map { it.toPacketData() }
                .plus(sequenceOf(firstDividerPacket, secondDividerPacket))
                .sorted()
                .foldIndexed(1) { index, acc, packetData ->
                    when (packetData) {
                        firstDividerPacket, secondDividerPacket -> acc * (index + 1)
                        else -> acc
                    }
                }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            "puzzle-13.in"
                .streamInput()
                .solve()
                .let(::println)
        }
    }
}
