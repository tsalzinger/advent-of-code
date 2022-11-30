package me.salzinger.common.extensions

fun ByteArray.toInt(): Int {
    return joinToString("").toInt(2)
}
