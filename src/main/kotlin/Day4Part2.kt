package me.salzinger

fun main() {
    4.solve(2) {
        val initialGame = BingoParser.parse(this)

        BingoLoosingPlayer(initialGame).getLoosingScore().toString()
    }
}
