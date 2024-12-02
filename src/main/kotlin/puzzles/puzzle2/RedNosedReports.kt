package me.salzinger.puzzles.puzzle2

import me.salzinger.common.extensions.toIntList
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.absoluteValue
import kotlin.math.sign

object RedNosedReports {
    fun List<Int>.isSafeReport(): Boolean {
        val (first, second) = this
        val direction = (second - first).sign

        return windowed(2).all { (first, second) ->
            val difference = second - first
            (difference.absoluteValue in setOf(1, 2, 3)) && difference.sign == direction
        }
    }

    fun List<Int>.isSafeReportWithProblemDampener(): Boolean {
        return isSafeReport() || indices.any {
            without(it).isSafeReport()
        }
    }

    fun List<Int>.without(indexToSkip: Int): List<Int> {
        return buildList {
            this@without.forEachIndexed { index, i ->
                if (index != indexToSkip) {
                    add(i)
                }
            }
        }
    }

    fun Sequence<String>.countSafeReports(): Int {
        return map {
            it.toIntList(" ")
        }
            .count {
                it.isSafeReport()
            }
    }

    fun Sequence<String>.countSafeReportsWithProblemDampener(): Int {
        return map {
            it.toIntList(" ")
        }
            .count {
                it.isSafeReportWithProblemDampener()
            }
    }
}