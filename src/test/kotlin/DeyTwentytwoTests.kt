package me.salzinger

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class CardShuffleTests {

    @TestFactory
    fun `deal into new stack`() =
        listOf(
            listOf(0, 1, 2) to listOf(2, 1, 0),
            listOf(0, 1, 2, 8, 8, 7, 3, 9, 1) to listOf(1, 9, 3, 7, 8, 8, 2, 1, 0)
        ).map { (input, result) ->
            DynamicTest.dynamicTest("'deal into new stack' of $input yields $result") {
                assertEquals(result, DEAL_INTO_NEW_STACK(input, null))
            }
        }

    @TestFactory
    fun `cut`() =
        listOf(
            (1 to listOf(0, 1, 2)) to listOf(1, 2, 0),
            (-1 to listOf(0, 1, 2)) to listOf(2, 0, 1),
            (4 to listOf(0, 1, 2, 8, 8, 7, 3, 9, 1)) to listOf(8, 7, 3, 9, 1, 0, 1, 2, 8),
            (-6 to listOf(0, 1, 2, 8, 8, 7, 3, 9, 1)) to listOf(8, 8, 7, 3, 9, 1, 0, 1, 2)
        ).map { (input, result) ->
            DynamicTest.dynamicTest("'cut ${input.first}' of $input yields $result") {
                assertEquals(result, CUT(input.second, input.first))
            }
        }

    @TestFactory
    fun `deal with increment`() =
        listOf(
            (1 to listOf(0, 1, 2)) to listOf(0, 1, 2),
            (3 to listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)) to listOf(0, 7, 4, 1, 8, 5, 2, 9, 6, 3)
        ).map { (input, result) ->
            DynamicTest.dynamicTest("'deal with increment ${input.first}' of $input yields $result") {
                assertEquals(result, DEAL_WITH_INCREMENT(input.second, input.first))
            }
        }

    @TestFactory
    fun `input parsing`() =
        listOf(
            "deal with increment 7" to (DEAL_WITH_INCREMENT to 7),
            "deal into new stack" to (DEAL_INTO_NEW_STACK to null),
            "cut 8" to (CUT to 8)
        ).map { (input, result) ->
            DynamicTest.dynamicTest("parse '${input}' into $result") {
                assertEquals(result, input.toDeckTechnique())
            }
        }

    @Test
    fun `full example 1`() {
        assertEquals(
            listOf(0, 3, 6, 9, 2, 5, 8, 1, 4, 7),
            """
                deal with increment 7
                deal into new stack
                deal into new stack
            """.trimIndent()
                .split("\n")
                .applyTechniquesTo(getFactoryOrderDeck(10))
        )
    }

    @Test
    fun `full example 2`() {
        assertEquals(
            listOf(3, 0, 7, 4, 1, 8, 5, 2, 9, 6),
            """
                cut 6
                deal with increment 7
                deal into new stack
            """.trimIndent()
                .split("\n")
                .applyTechniquesTo(getFactoryOrderDeck(10))
        )
    }

    @Test
    fun `full example 3`() {
        assertEquals(
            listOf(6, 3, 0, 7, 4, 1, 8, 5, 2, 9),
            """
                deal with increment 7
                deal with increment 9
                cut -2
            """.trimIndent()
                .split("\n")
                .applyTechniquesTo(getFactoryOrderDeck(10))
        )
    }

    @Test
    fun `full example 4`() {
        assertEquals(
            listOf(9, 2, 5, 8, 1, 4, 7, 0, 3, 6),
            """
                deal into new stack
                cut -2
                deal with increment 7
                cut 8
                cut -4
                deal with increment 7
                cut 3
                deal with increment 9
                deal with increment 3
                cut -1
            """.trimIndent()
                .split("\n")
                .applyTechniquesTo(getFactoryOrderDeck(10))
        )
    }
}