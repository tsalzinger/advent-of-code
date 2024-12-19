package me.salzinger.puzzles.puz

import me.salzinger.common.extensions.ChunkConditions
import me.salzinger.common.extensions.chunkedBy

typealias Pattern = String
typealias Remainder = String

object `Linen Layout` {
    val cache = mutableMapOf<String, Int>()

    fun Pattern.advance(patternMap: Map<Char, List<String>>): Int {
        if (isEmpty()) {
            return 1
        }

        return cache[this] ?: (patternMap[first()]?.mapNotNull {
            removePrefix(it).takeIf { it != this }?.advance(patternMap)
        }?.sum() ?: 0).also { cache[this] = it }
    }

    fun Sequence<String>.countPossibleDesigns(): Int {
        val (patterns, designs) = toList()
            .chunkedBy(ChunkConditions.ON_EMPTY_STRING)

        val patternMap = patterns.single().split(", ").groupBy { it.first() }
            .mapValues { it.value.sortedByDescending(String::count) }

        return designs
            .count {
                it.advance(patternMap) != 0
            }
    }
}