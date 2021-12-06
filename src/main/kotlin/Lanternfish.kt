package me.salzinger

object Lanternfish {
    private const val SPAWN_DURATION = 7
    private const val INITIAL_SPAWN_DELAY = 8

    fun doesSpawnOffspring(spawnDelay: Int, day: Int): Boolean {
        return day > spawnDelay && (day - (spawnDelay + 1)) % SPAWN_DURATION == 0
    }

    fun getNumberOfOffspringAfterDays(spawnDelay: Int, days: Int): Int {
        return (1..days).sumOf { day ->
            if (doesSpawnOffspring(spawnDelay, day)) {
                1 + getNumberOfOffspringAfterDays(INITIAL_SPAWN_DELAY, days - day)
            } else {
                0
            }
        }
    }
}
