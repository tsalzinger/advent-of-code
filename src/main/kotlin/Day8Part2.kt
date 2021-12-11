package me.salzinger

enum class Segment {
    TOP, TOP_LEFT, TOP_RIGHT, MIDDLE, BOTTOM_LEFT, BOTTOM_RIGHT, BOTTOM,
}

private val segmentsToDigits = mapOf(
    setOf(
        Segment.TOP,
        Segment.TOP_LEFT,
        Segment.TOP_RIGHT,
        Segment.BOTTOM_LEFT,
        Segment.BOTTOM_RIGHT,
        Segment.BOTTOM,
    ) to 0,
    setOf(
        Segment.TOP_RIGHT,
        Segment.BOTTOM_RIGHT,
    ) to 1,
    setOf(
        Segment.TOP,
        Segment.TOP_RIGHT,
        Segment.MIDDLE,
        Segment.BOTTOM_LEFT,
        Segment.BOTTOM,
    ) to 2,
    setOf(
        Segment.TOP,
        Segment.TOP_RIGHT,
        Segment.MIDDLE,
        Segment.BOTTOM_RIGHT,
        Segment.BOTTOM,
    ) to 3,
    setOf(
        Segment.TOP_LEFT,
        Segment.TOP_RIGHT,
        Segment.MIDDLE,
        Segment.BOTTOM_RIGHT,
    ) to 4,
    setOf(
        Segment.TOP,
        Segment.TOP_LEFT,
        Segment.MIDDLE,
        Segment.BOTTOM_RIGHT,
        Segment.BOTTOM,
    ) to 5,
    setOf(
        Segment.TOP,
        Segment.TOP_LEFT,
        Segment.MIDDLE,
        Segment.BOTTOM_RIGHT,
        Segment.BOTTOM,
        Segment.BOTTOM_LEFT,
    ) to 6,
    setOf(
        Segment.TOP,
        Segment.TOP_RIGHT,
        Segment.BOTTOM_RIGHT,
    ) to 7,
    setOf(
        Segment.TOP,
        Segment.TOP_LEFT,
        Segment.TOP_RIGHT,
        Segment.MIDDLE,
        Segment.BOTTOM_RIGHT,
        Segment.BOTTOM,
        Segment.BOTTOM_LEFT,
    ) to 8,
    setOf(
        Segment.TOP,
        Segment.TOP_LEFT,
        Segment.TOP_RIGHT,
        Segment.MIDDLE,
        Segment.BOTTOM_RIGHT,
        Segment.BOTTOM,
    ) to 9,
)
private val digitsToSegments = segmentsToDigits
    .entries
    .associate { it.value to it.key }

fun main() {
    8.solveExample("61229", List<String>::solve)

    8.solve(2, List<String>::solve)
}

private fun List<String>.solve(): String {
    return map { row ->
        row.split("|").map(String::trim)
    }.map { (testDigits, outputDigits) ->
        testDigits.toDigitChars() to outputDigits.toDigitChars()
    }.map { (testDigits, outputDigits) ->
        val digitMappings = getDigitMappings(testDigits)

        outputDigits.map {
            digitMappings.getValue(it)
        }.fold(0) { sum, digit -> (sum * 10) + digit }
    }.sum().toString()
}

private fun String.toDigitChars(): List<Set<Char>> {
    return split(" ").map { it.toSet() }
}

private fun getDigitMappings(testDigits: List<Set<Char>>): Map<Set<Char>, Int> {
    val segmentMappings = getSegmentMapping(testDigits)

    return testDigits.associateWith { segmentSet ->
        val segments = segmentSet.map {
            segmentMappings.getValue(it)
        }.toSet()

        segmentsToDigits.getValue(segments)
    }
}

private fun getSegmentMapping(testDigits: List<Set<Char>>): Map<Char, Segment> {
    val mappings = mutableMapOf<Char, Segment>()
    val oneSegments = testDigits.single { it.count() == 2 }
    val fourSegments = testDigits.single { it.count() == 4 }
    val sevenSegments = testDigits.single { it.count() == 3 }
    val eightSegments = testDigits.single { it.count() == 7 }
    val zeroSixNineSegmentOptions = testDigits.filter { it.count() == 6 }
    val twoThreeFiveSegmentOptions = testDigits.filter { it.count() == 5 }

    val zeroSixNineCounts = zeroSixNineSegmentOptions.getCharCounts().entries
    val twoThreeFiveCounts = twoThreeFiveSegmentOptions.getCharCounts().entries

    val top = (sevenSegments - oneSegments).single()
    val topLeft = twoThreeFiveCounts.single { it.value == 1 && it.key in fourSegments }.key
    val bottomLeft = twoThreeFiveCounts.single { it.value == 1 && it.key !in fourSegments }.key
    val middle = (fourSegments - topLeft - oneSegments).single()
    val bottom = (eightSegments - fourSegments - top - bottomLeft).single()
    val topRight = zeroSixNineCounts.single { it.value == 2 && it.key in oneSegments }.key
    val bottomRight = zeroSixNineCounts.single { it.value == 3 && it.key in oneSegments }.key

    mappings[top] = Segment.TOP
    mappings[topRight] = Segment.TOP_RIGHT
    mappings[topLeft] = Segment.TOP_LEFT
    mappings[bottomLeft] = Segment.BOTTOM_LEFT
    mappings[middle] = Segment.MIDDLE
    mappings[bottom] = Segment.BOTTOM
    mappings[bottomRight] = Segment.BOTTOM_RIGHT

    return mappings
}

private fun List<Set<Char>>.getCharCounts(): Map<Char, Int> {
    return fold(mutableMapOf()) { map, charSet ->
        charSet.forEach {
            map[it] = (map[it] ?: 0) + 1
        }
        map
    }
}

private fun List<Set<Char>>.getCharsForDigit(digit: Int): Set<Char> {
    return single { it.count() == digitsToSegments.getValue(digit).count() }
}
