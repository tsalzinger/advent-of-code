package puzzles

import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.Puzzle25FullOfHotAir.Part1.solve
import me.salzinger.puzzles.Puzzle25FullOfHotAir.toDecimal
import me.salzinger.puzzles.Puzzle25FullOfHotAir.toSnafuNumber
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class Puzzle25Part1FullOfHotAirTest {

    @TestFactory
    fun snafuNumberToDecimalTest(): Iterable<DynamicNode> {
        return listOf(
            "1=-0-2" to 1747.toBigInteger(),
            "12111" to 906.toBigInteger(),
            "2=0=" to 198.toBigInteger(),
            "21" to 11.toBigInteger(),
            "2=01" to 201.toBigInteger(),
            "111" to 31.toBigInteger(),
            "20012" to 1257.toBigInteger(),
            "112" to 32.toBigInteger(),
            "1=-1=" to 353.toBigInteger(),
            "1-12" to 107.toBigInteger(),
            "12" to 7.toBigInteger(),
            "1=" to 3.toBigInteger(),
            "122" to 37.toBigInteger(),

            "1" to "1".toBigInteger(),
            "2" to "2".toBigInteger(),
            "1=" to "3".toBigInteger(),
            "1-" to "4".toBigInteger(),
            "10" to "5".toBigInteger(),
            "11" to "6".toBigInteger(),
            "12" to "7".toBigInteger(),
            "2=" to "8".toBigInteger(),
            "2-" to "9".toBigInteger(),
            "20" to "10".toBigInteger(),
            "1=0" to "15".toBigInteger(),
            "1-0" to "20".toBigInteger(),
            "1=11-2" to "2022".toBigInteger(),
            "1-0---0" to "12345".toBigInteger(),
            "1121-1110-1=0" to "314159265".toBigInteger(),
        ).map { (snafuNumber, expectedDecimalNumber) ->
            DynamicTest.dynamicTest("$snafuNumber == $expectedDecimalNumber") {
                snafuNumber
                    .toDecimal()
                    .assertThat {
                        isEqualTo(expectedDecimalNumber)
                    }
            }
        }
    }

    @TestFactory
    fun decimalToSnafuNumberTest(): Iterable<DynamicNode> {
        return listOf(
            "1747".toBigInteger() to "1=-0-2",
            "906".toBigInteger() to "12111",
            "198".toBigInteger() to "2=0=",
            "11".toBigInteger() to "21",
            "201".toBigInteger() to "2=01",
            "31".toBigInteger() to "111",
            "1257".toBigInteger() to "20012",
            "32".toBigInteger() to "112",
            "353".toBigInteger() to "1=-1=",
            "107".toBigInteger() to "1-12",
            "7".toBigInteger() to "12",
            "3".toBigInteger() to "1=",
            "37".toBigInteger() to "122",

            "1".toBigInteger() to "1",
            "2".toBigInteger() to "2",
            "3".toBigInteger() to "1=",
            "4".toBigInteger() to "1-",
            "5".toBigInteger() to "10",
            "6".toBigInteger() to "11",
            "7".toBigInteger() to "12",
            "8".toBigInteger() to "2=",
            "9".toBigInteger() to "2-",
            "10".toBigInteger() to "20",
            "15".toBigInteger() to "1=0",
            "20".toBigInteger() to "1-0",
            "2022".toBigInteger() to "1=11-2",
            "12345".toBigInteger() to "1-0---0",
            "314159265".toBigInteger() to "1121-1110-1=0",
        ).map { (decimalNumber, expectedSnafuNumber) ->
            DynamicTest.dynamicTest("$decimalNumber == $expectedSnafuNumber") {
                decimalNumber
                    .toSnafuNumber()
                    .assertThat {
                        isEqualTo(expectedSnafuNumber)
                    }
            }
        }
    }

    @TestFactory
    fun exampleTests(): Iterable<DynamicNode> {
        return listOf(
            "puzzle-25-example-1.in" to "2=-1=0"
        ).map { (exampleInputFileName, expectedSolution) ->
            DynamicTest.dynamicTest(exampleInputFileName) {
                exampleInputFileName
                    .streamInput()
                    .solve()
                    .assertThat {
                        isEqualTo(expectedSolution)
                    }
            }
        }
    }
}

