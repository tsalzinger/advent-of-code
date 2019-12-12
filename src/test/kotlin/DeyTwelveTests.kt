package me.salzinger

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertAll

class MoonMovementTests {

    @TestFactory
    fun `coordinate parsing`() =
        listOf(
            "<x=-5, y=6, z=-11>" to listOf(-5, 6, -11),
            "<x=-8, y=-4, z=-2>" to listOf(-8, -4, -2),
            "<x=1, y=16, z=4>" to listOf(1, 16, 4),
            "<x=11, y=11, z=-4>" to listOf(11, 11, -4)
        ).map { (input, result) ->
            DynamicTest.dynamicTest("Input $input matches list $result") {
                assertEquals(result, input.toCoordinatesList())
            }
        }

    @Test
    fun `simple example movment`() {
        val moonSystem = listOf(
            Location(x = -1, y = 0, z = 2),
            Location(x = 2, y = -10, z = -7),
            Location(x = 4, y = -8, z = 8),
            Location(x = 3, y = 5, z = -1)
        ).map {
            Moon(
                it,
                Velocity(0, 0, 0)
            )
        }.run(
            ::MoonSystem
        )

        moonSystem
            .progress().also {
                val (m1, m2, m3, m4) = it.moons
                m1.assertLocationAndVelocity(Location(x = 2, y = -1, z = 1), Velocity(dx = 3, dy = -1, dz = -1))
                m2.assertLocationAndVelocity(Location(x = 3, y = -7, z = -4), Velocity(dx = 1, dy = 3, dz = 3))
                m3.assertLocationAndVelocity(Location(x = 1, y = -7, z = 5), Velocity(dx = -3, dy = 1, dz = -3))
                m4.assertLocationAndVelocity(Location(x = 2, y = 2, z = 0), Velocity(dx = -1, dy = -3, dz = 1))
            }
    }

    private fun Moon.assertLocationAndVelocity(
        location: Location,
        velocity: Velocity
    ) {
        assertAll(
            { assertEquals(location, this.location) },
            { assertEquals(velocity, this.velocity) }
        )
    }
}