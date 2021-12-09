package me.salzinger

fun main() {
    9.solve(1) {
        val heightMap = Grid(
            values = map { inputLine ->
                inputLine.map { it.digitToInt() }
            }
        )

        heightMap.mapNotNull { cell ->
            val neighbors = heightMap.getNeighborsOf(cell)

            if (neighbors.all { it.value > cell.value }) {
                1 + cell.value
            } else {
                null
            }
        }.sum().toString()
    }
}
