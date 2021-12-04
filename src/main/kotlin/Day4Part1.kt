package me.salzinger

fun main() {
    4.solve(1) {
        val initialGame = BingoParser.parse(this)

        BingoPlayer(initialGame).getWinningScore().toString()
    }
}
