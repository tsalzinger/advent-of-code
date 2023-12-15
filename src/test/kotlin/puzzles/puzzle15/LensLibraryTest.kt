package puzzles.puzzle15

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle15.LensLibrary
import me.salzinger.puzzles.puzzle15.LensLibrary.getHash
import me.salzinger.puzzles.puzzle15.LensLibrary.getSumOfFocusPowerPerLens
import me.salzinger.puzzles.puzzle15.LensLibrary.getSumOfHashes
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class LensLibraryTest {

    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle15/puzzle-15-example-1.in"
            .streamInput()
            .getSumOfHashes()
            .assertThat {
                isEqualTo(1320L)
            }
    }


    @Test
    fun `example 1 - part 2`() {
        "puzzles/puzzle15/puzzle-15-example-1.in"
            .streamInput()
            .getSumOfFocusPowerPerLens()
            .assertThat {
                isEqualTo(145L)
            }
    }

    @TestFactory
    fun `get hashes`(): Iterable<DynamicNode> {
        return listOf(
            "rn=1" to 30,
            "cm-" to 253,
            "qp=3" to 97,
            "cm=2" to 47,
            "qp-" to 14,
            "pc=4" to 180,
            "ot=9" to 9,
            "ab=5" to 197,
            "pc-" to 48,
            "pc=6" to 214,
            "ot=7" to 231,
        ).map { (input, expectedHash) ->
            DynamicTest.dynamicTest("Hash of $input") {
                input
                    .getHash()
                    .assertThat {
                        isEqualTo(expectedHash)
                    }
            }
        }
    }

    @TestFactory
    fun `focus power per box`(): Iterable<DynamicNode> {
        return listOf(
            LensLibrary.Box(
                0,
                listOf(
                    LensLibrary.Lens(
                        "rn", 1,
                    ),
                    LensLibrary.Lens(
                        "cm", 2,
                    ),
                )
            ) to 5,
            LensLibrary.Box(
                3,
                listOf(
                    LensLibrary.Lens(
                        "ot", 7,
                    ),
                    LensLibrary.Lens(
                        "ab", 5,
                    ),
                    LensLibrary.Lens(
                        "pc", 6,
                    ),
                )
            ) to 140,
        ).map { (box, expectedHash) ->
            DynamicTest.dynamicTest("Focus power of $box") {
                box
                    .getFocusPower()
                    .assertThat {
                        isEqualTo(expectedHash)
                    }
            }
        }
    }
}
