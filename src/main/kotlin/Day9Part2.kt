package me.salzinger

fun main() {
    9.solve(2) {
        val heightMap = Grid(values = map { inputLine ->
            inputLine.map { it.digitToInt() }
        })

        val lowSpots = heightMap.filter { cell ->
            val neighbors = heightMap.getNeighborsOf(cell)

            neighbors.all { it.value > cell.value }
        }.toSet()

        val basins = lowSpots.map { cell ->
            getBasins(heightMap, cell).count()
        }

        basins
            .sortedDescending()
            .take(3)
            .reduce { acc, size -> acc * size }
            .toString()
    }
}

private fun getBasins(heightMap: Grid<Int>, cell: Grid.Cell<Int>): Set<Grid.Cell<Int>> {
    if (cell.value == 9) {
        return emptySet()
    }

    return heightMap
        .getNeighborsOf(cell)
        .filter { neighbor -> neighbor.value != 9 }
        .filter { neighbor -> neighbor.value > cell.value }
        .flatMap { getBasins(heightMap, it) }
        .toSet() + cell
}
