package me.salzinger

class BingoPlayer(
    private val initialGame: Bingo
) {
    fun getWinningScore(): Long {
        var currentGame = initialGame
        while (currentGame.remainingDraws.isNotEmpty()) {
            val lastDraw = currentGame.nextDraw
            currentGame = currentGame.advance()

            if (currentGame.bingo) {
                return currentGame.winningBoard.getScore(lastDraw)
            }
        }

        throw IllegalStateException("No winning board available")
    }
}

data class Bingo(
    val remainingDraws: List<Int>,
    val boards: List<BingoBoard>
) {
    val bingo: Boolean by lazy {
        boards.any {
            it.bingo
        }
    }

    val winningBoard by lazy {
        boards.single {
            it.bingo
        }
    }

    val nextDraw by lazy {
        remainingDraws.first()
    }

    fun advance(): Bingo {
        return Bingo(
            remainingDraws = remainingDraws.drop(1),
            boards.map {
                it.mark(nextDraw)
            }
        )
    }

    override fun toString(): String {
        return "${remainingDraws.joinToString()}\n\n${boards.joinToString("\n\n")}"
    }
}

data class BingoBoard(val cells: List<List<BingoCell>>, val boardSize: Int) {
    val bingo: Boolean by lazy {
        (0 until boardSize)
            .any {
                isRowMarked(it) || isColumnMarked(it)
            }
    }

    fun getScore(scoreMultiplier: Int): Long {
        return cells
            .flatten()
            .fold(0L) { sum, cell -> sum + cell.score } * scoreMultiplier
    }

    fun mark(value: Int): BingoBoard {
        return copy(
            cells = cells.map {
                it.map { bingoCell ->
                    if (bingoCell.value == value) {
                        bingoCell.mark()
                    } else {
                        bingoCell
                    }
                }
            }
        )
    }

    private fun isRowMarked(row: Int): Boolean {
        return cells[row].all { it.marked }
    }

    private fun isColumnMarked(column: Int): Boolean {
        return cells.all { it[column].marked }
    }

    override fun toString(): String {
        return cells.joinToString("\n") {
            it.joinToString("\t")
        }
    }
}

data class BingoCell(val value: Int, val marked: Boolean = false) {
    val score: Int
        get() = if (marked) {
            0
        } else {
            value
        }

    fun mark(): BingoCell {
        return copy(marked = true)
    }

    override fun toString(): String {
        return if (marked) {
            "[$value]".padStart(5)
        } else {
            "$value".padStart(5)
        }
    }
}

object BingoParser {
    fun parse(input: List<String>): Bingo {
        return Bingo(
            input.first().toIntList(),
            input.drop(1).chunked(5, BingoParser::parseBoard),
        )
    }

    fun parseBoard(input: List<String>): BingoBoard {
        return BingoBoard(
            cells = input.map {
                it.toIntList(Regex("\\s+")).map(::BingoCell)
            },
            boardSize = input.size,
        )
    }
}
