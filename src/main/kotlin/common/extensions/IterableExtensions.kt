package common.extensions

import java.math.BigInteger

fun Iterable<Int>.product(): Int = fold(1) { product, item -> product * item }

fun <T> Iterable<T>.productBy(transform: (T) -> Int): Int = fold(1) { product, item -> product * transform(item) }

fun Iterable<Long>.product(): Long = fold(1L) { product, item -> product * item }

fun Iterable<BigInteger>.product(): BigInteger = fold(BigInteger.ONE) { product, item -> product * item }
