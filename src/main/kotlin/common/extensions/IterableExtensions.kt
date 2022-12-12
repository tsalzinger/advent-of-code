package common.extensions

import java.math.BigInteger

fun Iterable<Int>.product(): Int {
    return reduce { product, item -> product * item }
}

fun Iterable<BigInteger>.product(): BigInteger {
    return reduce { product, item -> product * item }
}
