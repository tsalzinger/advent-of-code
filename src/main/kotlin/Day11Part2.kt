package me.salzinger

private fun List<String>.solve(): String {
    var grid = Grid(map { row -> row.map { it.digitToInt() } }, Grid.Coordinate.NeighborModes.RING)
    val numberOfOctopuses = grid.rows * grid.columns

    var sumOfFlashes = 0
    var steps = 0

    while (sumOfFlashes != numberOfOctopuses) {
        steps++
        grid = grid.transformValues { it.value + 1 }
        val flashed = mutableSetOf<Grid.Coordinate>()

        var endFlashes = 0
        var startFlashes: Int

        do {
            startFlashes = endFlashes
            grid.filter { it.value > 9 && it.coordinate !in flashed }
                .forEach { highEnergyCell ->
                    if (flashed.add(highEnergyCell.coordinate)) {
                        endFlashes++
                        val neighbors = grid.getNeighborsOf(highEnergyCell)

                        grid = grid.transformValues { gridCell ->
                            if (gridCell in neighbors) {
                                gridCell.value + 1
                            } else {
                                gridCell.value
                            }
                        }
                    }
                }

        } while (startFlashes != endFlashes)

        grid = grid.transformValues {
            if (it.value > 9) {
                0
            } else {
                it.value
            }
        }

        sumOfFlashes = endFlashes
    }

    return "$steps"
}

fun main() {
    val expected = "195"
    val exampleResult = getNextInput(11, 0).solve()
    check(exampleResult == expected) {
        "Expected $expected got $exampleResult"
    }

    11.solve(2, List<String>::solve)
}

