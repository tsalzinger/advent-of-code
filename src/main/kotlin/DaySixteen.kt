package me.salzinger

import kotlin.math.absoluteValue

val l = listOf(0, 1, 0, -1).asSequence()

val i = generateSequence { }

class Pattern(private val pattern: List<Int>) {

    fun asSequence(outputPosition: Int): Sequence<Int> {
        var next = 1
        val phasePattern = pattern.flatMap { patternValue -> List(outputPosition) { patternValue } }

        return generateSequence {
            phasePattern[next].also {
                next = (next + 1) % phasePattern.size
            }
        }
    }
}

fun String.toNumberList() = map {
    it.toString().toInt()
}

fun List<Int>.fft(rawPattern: List<Int>, phases: Int, debug: Boolean = false): List<Int> {
    var phaseInput = this

    val pattern = Pattern(rawPattern)

    for (phase in 1..phases) {
        phaseInput = phaseInput.indices.map { outputPosition ->
            val patternSequence = pattern.asSequence(outputPosition + 1).iterator()
            (phaseInput
                .map {
                    val next = patternSequence.next()
                    it to next
                }
                .also {
                    if (debug) {
                        print(it.joinToString(" + ") { "${it.first}*${it.second}".padEnd(4) })
                    }
                }
                .map {
                    it.first * it.second
                }
                .sum()
                .absoluteValue % 10)
                .also {
                    if (debug) {
                        println(" = $it")
                    }
                }
        }
        if (debug) {
            println(phaseInput.joinToString(""))
        }
    }

    return phaseInput
}

fun main() {
    31.solve {
        first()
            .toNumberList()
            .fft(listOf(0, 1, 0, -1), 100)
            .subList(0, 8)
            .joinToString("")
    }
}