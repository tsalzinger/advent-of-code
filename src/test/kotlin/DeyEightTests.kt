package me.salzinger

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class SpaceImageFormatTests {

    class ImageInput(
        val width: Int,
        val height: Int,
        val data: String
    )

    @TestFactory
    fun `decodes`() =
        listOf(
            ImageInput(
                3,
                2,
                "123456789012"
            ) to listOf(
                listOf(
                    listOf(1, 2, 3),
                    listOf(4, 5, 6)
                ),
                listOf(
                    listOf(7, 8, 9),
                    listOf(0, 1, 2)
                )
            )
        ).map { (input, result) ->
            DynamicTest.dynamicTest("Image Data $input decodes to $result") {
                Assertions.assertEquals(
                    result,
                    input.data.decodeFromSpaceImageFormat(input.width, input.height).data
                )
            }
        }

    @Test
    fun `test image as string`() {
        assertEquals(
            "01\n10",
            "0222112222120000".decodeFromSpaceImageFormat(2, 2).image
        )
    }
}