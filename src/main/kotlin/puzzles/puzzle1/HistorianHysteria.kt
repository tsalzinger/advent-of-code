package me.salzinger.puzzles.puzzle1

import me.salzinger.common.extensions.toIntList
import me.salzinger.common.extensions.toPairs
import kotlin.math.absoluteValue

object HistorianHysteria {

    fun Sequence<String>.sumOfDifferences(): Int {
        return map {
            it.toIntList("   ").toPairs().single()
        }
            .fold(listOf<Int>() to listOf<Int>()) { (firstList, secondList), (first, second) ->
                (firstList + first) to (secondList + second)
            }
            .let { (firstList, secondList) ->
                firstList.sorted() zip secondList.sorted()
            }.sumOf { (first, second) ->
                (second - first).absoluteValue
            }
    }

    fun Sequence<String>.getSimilarityScore(): Int {
        return map {
            it.toIntList("   ").toPairs().single()
        }
            .fold(listOf<Int>() to listOf<Int>()) { (firstList, secondList), (first, second) ->
                (firstList + first) to (secondList + second)
            }
            .let { (fistList, secondList) ->
                val occurrences = secondList.groupingBy { it }.eachCount()

                fistList.groupingBy { it }
                    .eachCount()
                    .map { it.key * (occurrences[it.key] ?: 0) * it.value }
            }
            .sum()
    }
}