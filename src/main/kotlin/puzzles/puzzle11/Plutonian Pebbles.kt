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
}