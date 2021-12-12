package me.salzinger

private sealed interface Cave {
    val id: String
    val connectedCaves: MutableSet<Cave>

    class Big(
        override val id: String, override val connectedCaves: MutableSet<Cave> = mutableSetOf()
    ) : Cave

    class Small(
        override val id: String, override val connectedCaves: MutableSet<Cave> = mutableSetOf()
    ) : Cave
}

private class Path(
    val caves: List<Cave>,
) {
    private val end = caves.last()
    private val smallCaves = caves.filterIsInstance<Cave.Small>().toSet()

    override fun toString(): String {
        return caves.joinToString("-") { it.id }
    }

    fun extendTo(cave: Cave): Path {
        require(canExtendTo(cave))

        return Path(
            caves + cave
        )
    }

    fun canExtendTo(cave: Cave): Boolean {
        return cave in end.connectedCaves && cave !in smallCaves
    }

    fun extendUntil(targetCave: Cave): Set<Path> {
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
        Path(listOf(startCave, it)).extendUntil(endCave)
    }

    return paths.count().toString()
}

fun main() {
    12.solveExample(1, "10", List<String>::solve)
    12.solveExample(2, "19", List<String>::solve)
    12.solveExample(3, "226", List<String>::solve)

    12.solve(1, List<String>::solve)
}

