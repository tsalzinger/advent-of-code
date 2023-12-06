package me.salzinger.puzzles.puzzle6

import common.extensions.product
import me.salzinger.common.extensions.toLongList


// (tp^2 - 2 * tp * (tt / 2) + (d^1/2)^2) = 0
// (tp - d^(1/2))^1/2 = (tp - tt/2)^1/2
// tp - d^(1/2) = tp - tt/2
// d = (tt^2)/4
object WaitForIt {
    data class RaceStats(
        val durationInMilliseconds: Long,
        val recordDistanceInMillimeter: Long,
    ) {
        val numberOfWaysToBeatRecordDistance: Long by lazy {
            var count = 0L

            for (buttonPressTime in 0L..durationInMilliseconds) {
                val distance = (durationInMilliseconds - buttonPressTime) * buttonPressTime
                if (distance > recordDistanceInMillimeter) {
                    count++
                }
            }

            count
        }
    }

    fun Sequence<String>.toRacesStats(): List<RaceStats> {
        return chunked(2) { (time, distance) ->
            val times = time.substringAfter("Time:").toLongList(Regex("\\s+"))
            val distances = distance.substringAfter("Distance:").toLongList(Regex("\\s+"))

            times.zip(distances)
                .map {
                    RaceStats(it.first, it.second)
                }
        }.single()
    }

    fun List<RaceStats>.getNumberOfWaysToBeatRecordDistance(): List<Long> {
        return map {
            it.numberOfWaysToBeatRecordDistance
        }
    }

    fun Sequence<String>.getProductOfNumberOfWaysToBeatRecordDistance(): Long {
        return toRacesStats()
            .getNumberOfWaysToBeatRecordDistance()
            .product()
    }
}
