package me.salzinger

private fun List<String>.solve(): String {
    val grid = Grid(
        values = map { row ->
            row.toList().map { riskLevel ->
                DirectionalRiskLevel(
                    riskLevel = riskLevel.digitToInt()
                )
            }.run {
                (0 until 5).flatMap { repetition ->
                    map {
                        println(
                            "mapping ${it.riskLevel} at $repetition to ${
                                maxOf(
                                    (it.riskLevel + repetition) % 10,
                                    1
                                )
                            }"
                        )
                        DirectionalRiskLevel(
                            (it.riskLevel + repetition).run {
                                if (this > 9) {
                                    this % 9
                                } else {
                                    this
                                }
                            }
                        )
                    }
                }
            }
        }.run {
            (0 until 5).flatMap { repetition ->
                map { row ->
                    row.map {
                        DirectionalRiskLevel(
                            (it.riskLevel + repetition).run {
                                if (this > 9) {
                                    this % 9
                                } else {
                                    this
                                }
                            }
                        )
                    }
                }
            }
        },
        neighborProvider = Grid.Coordinate.NeighborModes.CROSS,
    )

    println(grid.toConsoleString {
        "${it.value.riskLevel}"
    })

    println("====")

    val targetCoordinate = Grid.Coordinate(grid.rows - 1, grid.columns - 1)

    val startingCell = grid.getCellAt(Grid.Coordinate(0, 0))
    startingCell.value.minRiskLevelSum = 0 to startingCell.coordinate

    return grid.advance(startingCell).run {
        println(toConsoleString {
            "${it.value.minRiskLevelSum?.first}".padStart(5, ' ')
        })
        getCellAt(targetCoordinate).value.minRiskLevelSum?.first
    }.toString()
}

private fun Grid<DirectionalRiskLevel>.advance(startingCell: Grid.Cell<DirectionalRiskLevel>): Grid<DirectionalRiskLevel> {
    var checkCells = listOf(startingCell to getNeighborsOf(startingCell))

    while (checkCells.isNotEmpty()) {
        checkCells = checkCells
            .flatMap { (startCell, targetCells) ->
                targetCells.mapNotNull { targetCell ->
                    val targetRiskLevel = targetCell.value.riskLevel
                    val currentRiskLevelSum = startCell.value.minRiskLevelSum!!.first
                    val targetRiskLevelSum = targetCell.value.minRiskLevelSum?.first
                    val potentialRiskLevelSum = currentRiskLevelSum + targetRiskLevel

                    if (targetRiskLevelSum == null || targetRiskLevelSum > potentialRiskLevelSum) {
                        targetCell.value.minRiskLevelSum = potentialRiskLevelSum to startingCell.coordinate
                        targetCell to getNeighborsOf(targetCell)
                    } else {
                        null
                    }
                }
            }
    }

    return this
}

fun main() {
    15.solveExample(1, "315", List<String>::solve)

    15.solve(2, List<String>::solve)
}

private fun String.inPairs(): List<String> {
    return (0..(count() - 2)).map {
        substring(it, it + 2)
    }
}
