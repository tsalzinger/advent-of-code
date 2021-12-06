package me.salzinger

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

internal class LanternfishTest {

    @TestFactory
    fun doesSpawnOffspring(): Iterable<DynamicNode> {
        return listOf(
            Triple(0, 0, false),
            Triple(0, 1, true),
            Triple(0, 2, false),
            Triple(0, 7, false),
            Triple(0, 8, true),
            Triple(0, 15, true),
            Triple(8, 0, false),
            Triple(8, 1, false),
            Triple(8, 2, false),
            Triple(8, 7, false),
            Triple(8, 8, false),
            Triple(8, 9, true),
        ).map { (startTimer, day, spawnsOffspring) ->
            dynamicTest("$startTimer on $day does spawn? $spawnsOffspring") {
                assertThat(Lanternfish.doesSpawnOffspring(startTimer, day)).isEqualTo(spawnsOffspring)
            }
        }
    }

    @TestFactory
    fun getNumberOfOffspringAfterDays(): Iterable<DynamicNode> {
        return listOf(
            Triple(0, 0, 0),
            Triple(0, 1, 1),
            Triple(0, 2, 1),
            Triple(0, 7, 1),
            Triple(0, 8, 2),
            Triple(0, 9, 2),
            Triple(0, 10, 3),
            Triple(0, 14, 3),
            Triple(0, 15, 4),
            Triple(0, 17, 6),
            Triple(8, 1, 0),
            Triple(8, 2, 0),
            Triple(8, 8, 0),
            Triple(8, 9, 1),
        ).map { (timer, days, expectedNumberOfOffspring) ->
            dynamicTest("With a timer of $timer, after $days days $expectedNumberOfOffspring offspring have spawned") {
                assertThat(Lanternfish.getNumberOfOffspringAfterDays(timer, days)).isEqualTo(expectedNumberOfOffspring)
            }
        }
    }
}
