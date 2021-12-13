package me.salzinger

sealed interface Fold {
    data class Horizontal(val x: Int) : Fold
    data class Vertical(val y: Int) : Fold
}

fun Grid<Boolean>.fold(fold: Fold): Grid<Boolean> {
    return when (fold) {
        is Fold.Horizontal -> {
            val values = mapNotNull {
                if (it.coordinate.column < fold.x) {
                    it.value || getCellAt(Grid.Coordinate(it.coordinate.row, 2 * fold.x - it.coordinate.column)).value
                } else {
                    null
                }
            }

            Grid(
                values.chunked(fold.x)
            )
        }
        is Fold.Vertical -> {
            val values = mapNotNull {
                if (it.coordinate.row < fold.y) {
                    it.value || getCellAt(Grid.Coordinate(2 * fold.y - it.coordinate.row, it.coordinate.column)).value
                } else {
                    null
                }
            }


            Grid(
                values.chunked(columns)
            )
        }
    }
}

fun List<String>.parseOrigami(): Pair<Grid<Boolean>, List<Fold>> {
    var maxX = 0
    var maxY = 0

    val dots = takeWhile { it.contains(",") }
        .map {
            val (x, y) = it.toIntList()

            maxX = maxOf(x, maxX)
            maxY = maxOf(y, maxY)

            Grid.Coordinate(row = y, column = x)
        }

    val paper = Grid(List(maxY + 1) { row ->
        List(maxX + 1) { column ->
            Grid.Coordinate(row, column) in dots
        }
    })

    val foldInstructions = takeLastWhile { it.startsWith("fold along") }
        .map {
            val offset = it.takeLastWhile { char -> char != '=' }.toInt()
            when {
                it.contains("x=") -> Fold.Horizontal(x = offset)
                it.contains("y=") -> Fold.Vertical(y = offset)
                else -> TODO("Unsupported fold direction given in $it")
            }
        }

    return paper to foldInstructions
}

private fun List<String>.solve(): String {
    val (paper, foldInstructions) = parseOrigami()

    return paper.fold(foldInstructions.first()).count { it.value }.toString()
}

fun main() {
    13.solveExample(1, "17", List<String>::solve)

    13.solve(1, List<String>::solve)
}

