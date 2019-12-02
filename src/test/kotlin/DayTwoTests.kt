package me.salzinger

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class IntcodeTests {
    @Test
    fun `Intcode program interpret`() {

        val intcodeProgram: IntcodeProgram = listOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50)

        val result = interpret(intcodeProgram, emptyMap())

        assertEquals(listOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50), result)

    }

    @TestFactory
    fun `intcode tests`() =
        listOf(
            listOf(1,0,0,0,99) to listOf(2,0,0,0,99),
            listOf(2,3,0,3,99) to listOf(2,3,0,6,99),
            listOf(2,4,4,5,99,0) to listOf(2,4,4,5,99,9801),
            listOf(1,1,1,4,99,5,6,0,99) to listOf(30,1,1,4,2,5,6,0,99)
        ).map {
            DynamicTest.dynamicTest("Intocde program ${it.first} results in ${it.second}") {
                Assertions.assertEquals(it.second, interpret(it.first, emptyMap()))
            }
        }

}