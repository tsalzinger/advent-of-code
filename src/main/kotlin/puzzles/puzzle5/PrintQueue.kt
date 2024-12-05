package me.salzinger.puzzles.puzzle5

import me.salzinger.common.extensions.ChunkEvaluation
import me.salzinger.common.extensions.chunkedBy
import me.salzinger.common.extensions.toIntList
import me.salzinger.common.extensions.toPairs

object PrintQueue {
    class OrderingRuleComparator(orderingRulePairs: List<Pair<Int, Int>>) : Comparator<Int> {
        private val lowerThenOrderingRules = orderingRulePairs.groupBy(Pair<Int, Int>::first, Pair<Int, Int>::second)
        private val greaterThenOrderingRules = orderingRulePairs.groupBy(Pair<Int, Int>::second, Pair<Int, Int>::first)

        override fun compare(o1: Int?, o2: Int?): Int {
            return when {
                o1 == null && o2 == null -> 0
                o1 == null -> -1
                o2 == null -> 1
                o2 in (lowerThenOrderingRules[o1] ?: emptyList<Int>()) -> -1
                o2 in (greaterThenOrderingRules[o1] ?: emptyList<Int>()) -> 1
                else -> 0
            }
        }

    }

    fun Sequence<String>.sumOfMiddlePageOfSortedUpdates(): Int {
        val (orderingRulesSpec, updatesSpec) = getSpecs()

        val orderingRulePairs = orderingRulesSpec
            .flatMap { it.toIntList("|") }
            .toPairs()

        val comparator = OrderingRuleComparator(orderingRulePairs)

        return updatesSpec
            .map { it.toIntList(",") }
            .filter { updatePages -> updatePages.sortedWith(comparator) == updatePages }
            .sumOf { sortedUpdatePages -> sortedUpdatePages.get(sortedUpdatePages.size / 2) }
    }

    private fun Sequence<String>.getSpecs(): List<List<String>> = toList()
        .chunkedBy {
            if (it.isNotEmpty()) {
                ChunkEvaluation.APPEND_TO_CHUNK
            } else {
                ChunkEvaluation.END_CHUNK_AND_DISCARD
            }
        }
}