package me.salzinger

private fun List<String>.solve(): String {
    val (paper, foldInstructions) = parseOrigami()

    return foldInstructions
        .fold(paper) { grid, fold ->
            grid.fold(fold)
        }
        .run {
            chunked(columns)
                .joinToString("\n") { row ->
                    row.joinToString("") {
                        if (it.value) "#" else " "
                    }
                }
        }
}

fun main() {
    13.solve(2, List<String>::solve)
}

