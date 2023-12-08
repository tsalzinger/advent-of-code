package me.salzinger.common.math

import kotlin.math.abs
import kotlin.math.max

fun Int.pow(exponent: Int): Long {
    return this.toLong().pow(exponent)
}

fun Long.pow(exponent: Int): Long {
    return power(base = this, exponent = exponent)
}

private fun power(base: Long, exponent: Int): Long {
    var result: Long = 1

    repeat(exponent) {
        result *= base
    }

    return result
}

fun ggt(x: Int = 0, y: Int = 0): Int {
    var a: Int = abs(x)
    var b: Int = abs(y)

    if (a == 0) {
        return b
    }

    while (b != 0) {
        if (a > b) {
            a -= b
        } else {
            b -= a
        }
    }

    return a
}

fun leastCommonMultiple(a: Long, b: Long): Long {
    val larger = max(a, b)
    var lcm = larger
    while (true) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
}


fun List<Long>.leastCommonMultiple(): Long {
    return reduce { leastCommonMultiple, number ->
        leastCommonMultiple(leastCommonMultiple, number)
    }
}
