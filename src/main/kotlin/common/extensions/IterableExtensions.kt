package common.extensions

import java.math.BigInteger

fun Iterable<Int>.product(): Int {
    return fold(1) { product, item -> product * item }
}


fun Iterable<Long>.product(): Long {
    return fold(1L) { product, item -> product * item }
}


fun Iterable<BigInteger>.product(): BigInteger {
    return fold(BigInteger.ONE) { product, item -> product * item }
}
