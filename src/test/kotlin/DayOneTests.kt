package me.salzinger

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class FuelTests {
    @TestFactory
    fun `get fuel from mass`() =
        listOf(
            12 to 2,
            14 to 2,
            1969 to 654,
            100756 to 33583
        ).map {
            DynamicTest.dynamicTest("Mass ${it.first} requires ${it.second} fuel") {
                Assertions.assertEquals(it.second, it.first.calculateRequiredFuel())
            }
        }
}