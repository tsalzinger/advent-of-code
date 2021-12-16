package me.salzinger

object OperatorTypes {
    val SUM = 0
    val PRODUCT = 1
    val MINIMUM = 2
    val MAXIMUM = 3
    val GREATER_THEN = 5
    val LESS_THEN = 6
    val EQUAL_TO = 7
}

private val BITSPacket.longValue: Long
    get() {
        return when (value) {
            is BITSPacketValue.Operator -> {
                val packetValues = value.subPackets.map { it.longValue }
                when (type) {
                    OperatorTypes.SUM -> packetValues.sum()
                    OperatorTypes.PRODUCT -> packetValues.reduce { product, value -> product * value }
                    OperatorTypes.MINIMUM -> packetValues.minOrNull()!!
                    OperatorTypes.MAXIMUM -> packetValues.maxOrNull()!!
                    OperatorTypes.GREATER_THEN -> if (packetValues.first() > packetValues.last()) {
                        1
                    } else {
                        0
                    }
                    OperatorTypes.LESS_THEN -> if (packetValues.first() < packetValues.last()) {
                        1
                    } else {
                        0
                    }
                    OperatorTypes.EQUAL_TO -> if (packetValues.first() == packetValues.last()) {
                        1
                    } else {
                        0
                    }
                    else -> throw IllegalStateException("Unknown operator type $type")
                }
            }
            is BITSPacketValue.Literal -> {
                value.bits
                    .chunked(SegmentLength.LITERAL)
                    .flatMap { it.drop(1) }
                    .joinToString("")
                    .toLong(radix = 2)
            }
        }
    }

private fun List<String>.solve(): String {
    return single().toHexString()
        .getBits()
        .parsePacket()
        .first
        .longValue
        .toString()
}

fun main() {
    16.solve(2, List<String>::solve)
}

private fun String.inPairs(): List<String> {
    return (0..(count() - 2)).map {
        substring(it, it + 2)
    }
}
