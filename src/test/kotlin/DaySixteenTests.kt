package me.salzinger

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class DaySixteenTests {
    @TestFactory
    fun `fft 100 phases`() =
        listOf(
            "80871224585914546619083218645595" to "24176176",
            "19617804207202209144916044189917" to "73745418",
            "69317163492948606335995924319873" to "52432133"
        ).map { (input, output) ->
            val pattern = listOf(0, 1, 0, -1)
            val phases = 100
            DynamicTest.dynamicTest("$input.fft($pattern, $phases)[0..8] == $output") {
                assertEquals(output, input.toNumberList().fft(pattern, 100).subList(0, 8).joinToString(""))
            }
        }
}