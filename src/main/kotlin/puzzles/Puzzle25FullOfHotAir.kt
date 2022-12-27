package me.salzinger.puzzles

import me.salzinger.common.streamInput
import java.math.BigInteger

typealias SnafuNumber = String

object Puzzle25FullOfHotAir {

    private val snafuDigitToDecimal = mapOf(
        '2' to 2.toBigInteger(),
        '1' to 1.toBigInteger(),
        '0' to 0.toBigInteger(),
        '-' to (-1).toBigInteger(),
        '=' to (-2).toBigInteger(),
    )

    private val SNAFU_RADIX = 5.toBigInteger()

    fun SnafuNumber.toDecimal(): BigInteger {
        return toCharArray()
            .map(snafuDigitToDecimal::getValue)
            .fold(BigInteger.ZERO) { sum, digit ->
                (sum * SNAFU_RADIX) + digit
            }
    }

    fun BigInteger.toSnafuNumber(): SnafuNumber {
        return toString(SNAFU_RADIX.toInt())
            .run {
                if (contains("[3-4]")) {
                    return this
                } else {
                    this
                }
            }
            .reversed()
            .fold(StringBuilder() to 0) { (result, carryOver), digit ->
                val currentDigit = digit.digitToInt() + carryOver
                when {
                    currentDigit == 0 -> result.append("0") to 0
                    (currentDigit % SNAFU_RADIX.toInt() == 0) -> result.append("0") to (currentDigit / SNAFU_RADIX.toInt())
                    (currentDigit % SNAFU_RADIX.toInt() == 3) -> result.append("=") to (currentDigit / SNAFU_RADIX.toInt() + 1)
                    (currentDigit % SNAFU_RADIX.toInt() == 4) -> result.append("-") to (currentDigit / SNAFU_RADIX.toInt() + 1)
                    else -> result.append("$currentDigit") to (currentDigit / SNAFU_RADIX.toInt())
                }

            }
            .let { (result, carryOver) ->
                when (carryOver) {
                    0 -> {}
                    1 -> result.append("1")
                    else -> TODO()
                }

                result
            }
            .toString()
            .reversed()
    }

    object Part1 {
        fun Sequence<String>.solve(): String {
            return map {
                it.toDecimal()
            }.sumOf { it }
                .toSnafuNumber()
        }

        @JvmStatic
        fun main(args: Array<String>) {
            "puzzle-25.in"
                .streamInput()
                .solve()
                .let(::println)
        }
    }
}
