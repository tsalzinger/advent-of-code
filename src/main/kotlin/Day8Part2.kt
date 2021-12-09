package me.salzinger

enum class Segment {
    TOP, TOP_LEFT, TOP_RIGHT, MIDDLE, BOTTOM_LEFT, BOTTOM_RIGHT, BOTTOM,
}

private val digitSegments = mapOf(
    0 to setOf(
        Segment.TOP,
        Segment.TOP_LEFT,
        Segment.TOP_RIGHT,
        Segment.BOTTOM_LEFT,
        Segment.BOTTOM_RIGHT,
        Segment.BOTTOM,
    ),
    1 to setOf(
        Segment.TOP_RIGHT,
        Segment.BOTTOM_RIGHT,
    ),
    2 to setOf(
        Segment.TOP,
        Segment.TOP_RIGHT,
        Segment.MIDDLE,
        Segment.BOTTOM_LEFT,
        Segment.BOTTOM,
    ),
    3 to setOf(
        Segment.TOP,
        Segment.TOP_RIGHT,
        Segment.MIDDLE,
        Segment.BOTTOM_RIGHT,
        Segment.BOTTOM,
    ),
    4 to setOf(
        Segment.TOP_LEFT,
        Segment.TOP_RIGHT,
        Segment.MIDDLE,
        Segment.BOTTOM_RIGHT,
    ),
    5 to setOf(
        Segment.TOP,
        Segment.TOP_LEFT,
        Segment.MIDDLE,
        Segment.BOTTOM_RIGHT,
        Segment.BOTTOM,
    ),
    6 to setOf(
        Segment.TOP,
        Segment.TOP_LEFT,
        Segment.MIDDLE,
        Segment.BOTTOM_RIGHT,
        Segment.BOTTOM,
        Segment.BOTTOM_LEFT,
    ),
    7 to setOf(
        Segment.TOP,
        Segment.TOP_RIGHT,
        Segment.BOTTOM_RIGHT,
    ),
    8 to setOf(
        Segment.TOP,
        Segment.TOP_LEFT,
        Segment.TOP_RIGHT,
        Segment.MIDDLE,
        Segment.BOTTOM_RIGHT,
        Segment.BOTTOM,
        Segment.BOTTOM_LEFT,
    ),
    9 to setOf(
        Segment.TOP,
        Segment.TOP_LEFT,
        Segment.TOP_RIGHT,
        Segment.MIDDLE,
        Segment.BOTTOM_RIGHT,
        Segment.BOTTOM,
    ),
)

fun main() {
    8.solve(2) {
        take(1).map {
            it.split("|").map(String::trim)
        }.map { (testInput, output) ->
            val wireToSegment = mutableMapOf<Char, Segment>()

            var wiresToSegments = testInput
                .split(" ")
                .associate {
                    it.toSet() to digitSegments.filter { (_, segments) -> segments.count() == it.count() }.values.flatten()
                        .toSet()
                }
                .toMap()
//                .sortedBy { it.second.count() }

            while (wiresToSegments.isNotEmpty()) {
                val newMap = mutableMapOf<Set<Char>, Set<Segment>>()

                val uniquePairs = wiresToSegments
                    .filter { it.value.count() == it.key.count() }

                val smallestSegmentCount = uniquePairs.minOf { it.value.count() }

                val filterPairs = uniquePairs
                    .filter { it.value.count() == smallestSegmentCount }

                // reduce
                wiresToSegments
                    .map { (wires, segments) ->
                        filterPairs
                            .entries
                            .fold(wires to segments) { remaining, current ->
                                if (remaining.first.containsAll(current.key))
                            }
                    }

//                wiresToSegments
//                    .entries
//                    .filter { it.value.count() == it.key.count() }
//                    .sortedBy { it.value.count() + Random.nextDouble(0.0, 0.5) }
//                    .first()
//                    .also { (wires, possibleSegments) ->
//                        println("Checking: $wires=$possibleSegments")
//
//                        wiresToSegments.forEach { (w, s) ->
//                            val unsolvedWires = w - wireToSegment.keys
//                            val unsolvedSegments = s - wireToSegment.values.toSet()
//
//                            println("$unsolvedWires=$unsolvedSegments of $w=$s are unsolved")
//
//                            if (wires != unsolvedWires && unsolvedWires.containsAll(wires) && unsolvedSegments.containsAll(
//                                    possibleSegments
//                                )
//                            ) {
//                                val remainingSegments = unsolvedSegments - possibleSegments
//                                val remainingWires = unsolvedWires - wires
//                                println("remaining are $remainingWires=$remainingSegments")
//                                if (remainingSegments.count() == 1) {
//                                    wireToSegment[remainingWires.single()] = remainingSegments.single()
//                                } else {
//                                    newMap[remainingWires] = remainingSegments
//                                }
//                            } else {
//                                println("no overlap")
//                                newMap[unsolvedWires] = unsolvedSegments
//                            }
//                        }
//
//                        wiresToSegments = newMap
//                        println("Solved")
//                        println(wireToSegment.entries.sortedBy { it.key }.joinToString("\n"))
//                        println("Unsolved")
//                        println(wiresToSegments.entries.sortedBy { it.key.count() }.joinToString("\n"))
//                    }
            }
//            wiresToSegments
//                .map { (wires, possibleSegments) ->
//
//                    wires.forEach { wire ->
//                        if (wire in wireMappings) {
//                            wireMappings[wire].int
//                        } else {
//                            wireMappings[wire] = possibleSegments.toMutableSet()
//                        }
//                    }
//
//                    ""
//
//                }
//                .joinToString("\n")
//                .map { (input, ) }
            wiresToSegments

        }
            .joinToString("\n")
    }
}
