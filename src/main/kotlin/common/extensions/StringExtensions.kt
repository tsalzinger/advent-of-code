package me.salzinger.common.extensions

import java.math.BigDecimal

fun String.toIntList(vararg delimiters: String = arrayOf(",")): List<Int> = trim().split(delimiters = delimiters).map { it.trim().toInt() }

fun String.toIntList(regex: Regex): List<Int> = trim().split(regex = regex).map { it.trim().toInt() }

fun String.toLongList(vararg delimiters: String = arrayOf(",")): List<Long> =
    trim().split(delimiters = delimiters).map {
        it.trim().toLong()
    }

fun String.toLongList(regex: Regex): List<Long> = trim().split(regex = regex).map { it.trim().toLong() }

fun String.toBigDecimalList(vararg delimiters: String = arrayOf(",")): List<BigDecimal> =
    trim().split(delimiters = delimiters).map {
        it.trim().toBigDecimal()
    }
