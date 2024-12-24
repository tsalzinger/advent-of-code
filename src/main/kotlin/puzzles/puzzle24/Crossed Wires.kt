package me.salzinger.puzzles.puzzle24

import me.salzinger.common.extensions.ChunkConditions
import me.salzinger.common.extensions.chunkedBy

object `Crossed Wires` {
    val GATE_REGEX = Regex("(...) (...?) (...) -> (...)")

    fun Sequence<String>.getNumberOutputOnZWires(): Long {
        val (startSignals, logicGates) = toList()
            .chunkedBy(ChunkConditions.ON_EMPTY_STRING)

        val signalsOnWire = startSignals
            .associate {
                val (wireName, wireValue) = it.split(": ")
                wireName to (wireValue == "1")
            }
            .toMutableMap()

        val gates = mutableMapOf<String, () -> Boolean>()

        fun getSignal(wire: String): Boolean {
            return signalsOnWire[wire] ?: gates.getValue(wire)().also { signalsOnWire[wire] = it }
        }

        logicGates
            .forEach {
                val (wire1, operator, wire2, outputWire) = GATE_REGEX.matchEntire(it)!!.destructured

                gates[outputWire] = {
                    val signal1 = getSignal(wire1)
                    val signal2 = getSignal(wire2)

                    when (operator) {
                        "AND" -> {
                            signal1 and signal2
                        }

                        "OR" -> {
                            signal1 or signal2
                        }

                        "XOR" -> {
                            signal1 xor signal2
                        }

                        else -> error("Unknown operator: $operator")
                    }
                }
            }

        return gates
            .filter { it.key.startsWith("z") }
            .toList()
            .sortedByDescending { it.first }
            .fold(0L) { acc, value -> acc.run { acc.shl(1) or value.second().toLong() } }
    }

    fun Boolean.toLong() = if (this) 1L else 0L
}