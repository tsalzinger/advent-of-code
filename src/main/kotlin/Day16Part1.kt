package me.salzinger

@JvmInline
value class HexString(val value: String) {
    fun getBits(): List<Bit> {
        return value.flatMap { hexDigit ->
            when (hexDigit) {
                in '0'..'9' -> hexDigit.digitToInt()
                in 'A'..'F' -> (hexDigit.code - 'A'.code + 10)
                else -> throw IllegalArgumentException("Unsupported hex digit $hexDigit")
            }.toString(radix = 2).padStart(4, '0').map { Bit(it.digitToInt(radix = 2) == 1) }
        }
    }
}

@JvmInline
value class Bit(val value: Boolean) {
    val isOne: Boolean
        get() = value
    val isZero: Boolean
        get() = !value

    override fun toString(): String {
        return if (value) {
            "1"
        } else {
            "0"
        }
    }
}

fun String.toHexString() = HexString(this)

private fun List<Bit>.toInt(): Int {
    return joinToString("").toInt(radix = 2)
}

private fun List<Bit>.rangeToInt(from: Int = 0, length: Int): Int {
    return subList(from, from + length).joinToString("").toInt(radix = 2)
}

object SegmentLength {
    val SUB_PACKET_LENGTH = 15
    val SUB_PACKET_COUNT = 11
    val VERSION = 3
    val TYPE = 3
    val HEADER = VERSION + TYPE
    val LITERAL = 5
    val OPERATOR_LENGTH_TYPE_ID = 1
}

private object PacketType {
    val LITERAL_VALUE = 4
}

private object OperatorLengthTypeID {
    val TOTAL_LENGTH = 0
    val NUMBER_OF_SUB_PACKETS = 1
}

fun List<Bit>.parsePacket(): Pair<BITSPacket, List<Bit>> {
    val packetVersion = rangeToInt(0, SegmentLength.VERSION)
    val packetType = rangeToInt(SegmentLength.VERSION, SegmentLength.TYPE)
    val toParse = drop(SegmentLength.HEADER)

    return when (packetType) {
        PacketType.LITERAL_VALUE -> {
            val endSegmentIndex = toParse.chunked(SegmentLength.LITERAL)
                .indexOfFirst { bits -> bits.first().isZero }
            val literalBytesCount = (endSegmentIndex + 1) * SegmentLength.LITERAL

            BITSPacket(
                packetVersion,
                packetType,
                BITSPacketValue.Literal(toParse.take(literalBytesCount))
            ) to toParse.drop(literalBytesCount)
        }
        else -> {
            // operator
            when (toParse.rangeToInt(0, SegmentLength.OPERATOR_LENGTH_TYPE_ID)) {
                OperatorLengthTypeID.TOTAL_LENGTH -> {
                    val subPacketBits = toParse
                        .drop(SegmentLength.OPERATOR_LENGTH_TYPE_ID)
                        .rangeToInt(length = SegmentLength.SUB_PACKET_LENGTH)

                    val subPacketStart = SegmentLength.OPERATOR_LENGTH_TYPE_ID + SegmentLength.SUB_PACKET_LENGTH
                    val subPacketEnd = subPacketStart + subPacketBits

                    var remainder = toParse.slice(subPacketStart until subPacketEnd)
                    val subPackets = buildList {
                        while (remainder.isNotEmpty() && remainder.any(Bit::isOne)) {
                            val parseResult = remainder.parsePacket()
                            add(parseResult.first)
                            remainder = parseResult.second
                        }
                    }

                    BITSPacket(
                        packetVersion,
                        packetType,
                        BITSPacketValue.Operator(subPackets)
                    ) to toParse.slice(subPacketEnd until toParse.count())
                }
                else -> {
                    val subPacketCount = toParse
                        .drop(SegmentLength.OPERATOR_LENGTH_TYPE_ID)
                        .rangeToInt(length = SegmentLength.SUB_PACKET_COUNT)

                    var remaining =
                        toParse.drop(SegmentLength.OPERATOR_LENGTH_TYPE_ID + SegmentLength.SUB_PACKET_COUNT)
                    val subPackets = buildList {
                        repeat(subPacketCount) {
                            val parseResult = remaining.parsePacket()
                            add(parseResult.first)
                            remaining = parseResult.second
                        }
                    }

                    BITSPacket(
                        packetVersion,
                        packetType,
                        BITSPacketValue.Operator(subPackets)
                    ) to remaining
                }
            }
        }
    }
}

class BITSPacket(
    val version: Int,
    val type: Int,
    val value: BITSPacketValue,
) {
    fun flatten(): List<BITSPacket> {
        return when (value) {
            is BITSPacketValue.Operator -> listOf(this) + value.subPackets.flatMap(BITSPacket::flatten)
            is BITSPacketValue.Literal -> listOf(this)
        }
    }
}

sealed interface BITSPacketValue {
    data class Literal(
        val bits: List<Bit>
    ) : BITSPacketValue {
        val value by lazy {
            bits
                .chunked(SegmentLength.LITERAL)
                .flatMap { it.drop(1) }
                .toInt()
        }
    }

    data class Operator(
        val subPackets: List<BITSPacket>
    ) : BITSPacketValue
}

private fun List<String>.solve(): String {
    return single().toHexString()
        .getBits()
        .parsePacket()
        .first
        .flatten()
        .sumOf { it.version }
        .toString()
}

fun main() {
    16.solveExamples(
        expectedSolutions = listOf("16", "12", "23", "31"),
        solver = List<String>::solve
    )

    16.solve(1, List<String>::solve)
}

private fun String.inPairs(): List<String> {
    return (0..(count() - 2)).map {
        substring(it, it + 2)
    }
}
