package me.salzinger.common.extensions

fun ByteArray.toInt(): Int = joinToString("").toInt(2)
