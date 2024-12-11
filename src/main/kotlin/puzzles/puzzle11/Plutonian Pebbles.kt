package me.salzinger.puzzles.puzzle11

import me.salzinger.common.extensions.toLongList

object `Plutonian Pebbles` {
    interface Rule {
        fun isApplicable(stone: Long): Boolean

        fun apply(stone: Long): List<Long>
    }

    val rules = listOf<Rule>(
        object : Rule {
            override fun isApplicable(stone: Long) = stone == 0L

            override fun apply(stone: Long): List<Long> = listOf(1)
        },
        object : Rule {
            override fun isApplicable(stone: Long) = "$stone".length % 2 == 0

            override fun apply(stone: Long): List<Long> {
                val stoneString = "$stone"
                val digitsPerHalf = stoneString.length / 2

                return listOf(
                    stoneString.slice(0..<digitsPerHalf),
                    stoneString.slice(digitsPerHalf..<stoneString.length),
                ).map(String::toLong)
            }
        },
        object : Rule {
            override fun isApplicable(stone: Long) = true

            override fun apply(stone: Long): List<Long> = listOf(stone * 2024)
        },
    )

    fun Sequence<String>.countStonesAfterBlinking25Times(): Int {
        var stones = single()
            .toLongList(" ")
            .map { listOf(it) }
            .asSequence()

        repeat(25) {
            stones = stones.map { stones ->
                stones.flatMap { stone ->
                    rules.first { it.isApplicable(stone) }.apply(stone)
                }
            }
        }

        return stones.flatten().count()
    }

    val cache = mutableMapOf<Int, MutableMap<Long, Long>>()

    fun Long.getNumberOfStones(blinks: Int): Long {
        return if (blinks == 0) {
            1L
        } else {
            cache[blinks]?.get(this) ?: rules
                .first { it.isApplicable(this) }
                .apply(this)
                .sumOf {
                    it.getNumberOfStones(blinks - 1)
                }
                .also {
                    cache.getOrPut(blinks) {
                        mutableMapOf<Long, Long>()
                    }.put(this, it)
                }
        }
    }

    fun Sequence<String>.countStonesAfterBlinking75Times(): Long {
        return single()
            .toLongList(" ")
            .sumOf { it.getNumberOfStones(75) }
    }
}