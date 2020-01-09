package me.salzinger

import java.math.BigInteger

fun main() {
    19.solve(1) {
        first()
            .convertIntcodeInput()
            .run {
                val map = mutableMapOf<Point, BigInteger>()
                for (y in 0..49) {
                    for (x in 0..49) {
                        IntcodeProgramInterpreter(
                            this,
                            inputs = ListInputProvider(listOf(x.toBigInteger(), y.toBigInteger())),
                            outputRecorder = object : OutputRecorder {
                                override fun addValue(value: BigInteger) {
                                    map[Point(x, y)] = value
                                }

                                override fun getOutput(): List<BigInteger> {
                                    TODO("not implemented")
                                }
                            }
                        ).evaluate()
                    }
                }

                map.count { it.value == BigInteger.ONE }.toString()
            }
    }
}
