package me.salzinger

private class CavePath(
    val caves: List<Cave>,
) {
    private val end = caves.last()
    private val smallCaves = caves.filterIsInstance<Cave.Small>()
    private val hasSmallCaveCircle = smallCaves
        .groupBy { it }
        .mapValues { it.value.count() }
        .values
        .any { it == 2 }

    override fun toString(): String {
        return caves.joinToString("-") { it.id }
    }

    fun extendTo(cave: Cave): CavePath {
        require(canExtendTo(cave))

        return CavePath(
            caves + cave
        )
    }

    fun canExtendTo(cave: Cave): Boolean {
        return cave.id != "start" &&
            cave in end.connectedCaves
            && (cave !in smallCaves || !hasSmallCaveCircle)
    }

    fun extendUntil(targetCave: Cave): Set<CavePath> {
        if (end == targetCave) {
            return setOf(this)
        }

        return end.connectedCaves.filter { canExtendTo(it) }.flatMap {
            extendTo(it).extendUntil(targetCave)
        }.toSet()
    }
}

private fun List<String>.solve(): String {
    val caves: MutableMap<String, Cave> = mutableMapOf()

    forEach { connection ->
        val (start, end) = connection.split("-").map { caveId ->
            caves.computeIfAbsent(caveId) { _ ->
                if (caveId == caveId.uppercase()) {
                    Cave.Big(caveId)
                } else {
                    Cave.Small(caveId)
                }
            }
        }

        start.connectedCaves += end
        end.connectedCaves += start
    }

    val startCave = caves.getValue("start")
    val endCave = caves.getValue("end")

    val paths = startCave.connectedCaves.flatMap {
        CavePath(listOf(startCave, it)).extendUntil(endCave)
    }

    return paths.count().toString()
}

fun main() {
    12.solveExample(1, "36", List<String>::solve)
    12.solveExample(2, "103", List<String>::solve)
    12.solveExample(3, "3509", List<String>::solve)

    12.solve(2, List<String>::solve)
}

