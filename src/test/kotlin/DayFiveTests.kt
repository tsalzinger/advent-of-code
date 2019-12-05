package me.salzinger

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class IncodeParameterPodeTests {
    @TestFactory
    fun `instruction parsing`() =
        listOf(
            1101 to Instruction(1, listOf(1, 1)),
            1101 to Instruction(1, listOf(1, 1)),
            1111111101 to Instruction(1, listOf(1, 1, 1, 1, 1, 1, 1, 1)),
            1001111101 to Instruction(1, listOf(1, 1, 1, 1, 1, 0, 0, 1)),
            1102 to Instruction(2, listOf(1, 1)),
            1103 to Instruction(3, listOf(1, 1)),
            301231169 to Instruction(69, listOf(1, 1, 3, 2, 1, 0, 3))
        ).map {
            DynamicTest.dynamicTest("Instruction ${it.first} results in ${it.second}") {
                Assertions.assertEquals(it.second, Instruction.init(it.first))
            }
        }
}