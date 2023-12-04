package me.salzinger.common.math

import kotlin.math.abs

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
