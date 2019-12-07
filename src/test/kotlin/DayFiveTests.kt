package me.salzinger

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class IncodeParameterPodeTests {
//    @TestFactory
//    fun `instruction parsing`() =
//        listOf(
//            1101 to Instruction(1, listOf(1, 1)),
//            1101 to Instruction(1, listOf(1, 1)),
//            1111111101 to Instruction(1, listOf(1, 1, 1, 1, 1, 1, 1, 1)),
//            1001111101 to Instruction(1, listOf(1, 1, 1, 1, 1, 0, 0, 1)),
//            1102 to Instruction(2, listOf(1, 1)),
//            1103 to Instruction(3, listOf(1, 1)),
//            301231169 to Instruction(69, listOf(1, 1, 3, 2, 1, 0, 3))
//        ).map {
//            DynamicTest.dynamicTest("Instruction ${it.first} results in ${it.second}") {
//                Assertions.assertEquals(it.second, Instruction.init(it.first))
//            }
//        }

    @TestFactory
    fun `thermal radiator`() =
        listOf(
            "0:3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9" to 0,
            "1:3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9" to 1,
            "3:3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9" to 1,

            "0:3,3,1105,-1,9,1101,0,0,12,4,12,99,1" to 0,
            "1:3,3,1105,-1,9,1101,0,0,12,4,12,99,1" to 1,
            "3:3,3,1105,-1,9,1101,0,0,12,4,12,99,1" to 1,

            "0:3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99" to 999,
            "7:3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99" to 999,
            "8:3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99" to 1000,
            "9:3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99" to 1001,
            "25:3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99" to 1001

        ).map {
            val (input, memory) = it.first.split(":")
            DynamicTest.dynamicTest("Diagnostic code ${it.first} results in ${it.second}") {
                val result = IntcodeProgramInterpreter(
                    memory.convertIntcodeInput(),
                    inputs = listOf(input.toInt()).asInputProvider()
                ).evaluate()

                Assertions.assertEquals(it.second, result.executionContext.output.getOutput().last())
            }
        }
}