package me.salzinger

import java.math.BigDecimal

object Lanternfish {
    private const val SPAWN_DURATION = 7
    private const val INITIAL_SPAWN_DELAY = 8

    private val cache = mutableMapOf<Pair<Int, Int>, BigDecimal>()

    fun doesSpawnOffspring(spawnDelay: Int, day: Int): Boolean {
        return day > spawnDelay && (day - (spawnDelay + 1)) % SPAWN_DURATION == 0
    }

    fun getNumberOfOffspringAfterDays(spawnDelay: Int, days: Int): BigDecimal {
        return cache[spawnDelay to days] ?: (1..days).sumOf { day ->
            if (doesSpawnOffspring(spawnDelay, day)) {
                BigDecimal.ONE + getNumberOfOffspringAfterDays(INITIAL_SPAWN_DELAY, days - day)
            } else {
                BigDecimal.ZERO
            }
        }.also { cache[spawnDelay to days] = it }
    }
}
