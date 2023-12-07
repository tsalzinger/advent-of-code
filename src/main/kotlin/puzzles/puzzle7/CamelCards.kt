package me.salzinger.puzzles.puzzle6

import puzzles.puzzle7.CamelCardsGame

object CamelCards {

    fun Sequence<String>.toCamelCardGame(): CamelCardsGame {
        return map {
            val (cards, rank) = it.split(" ")
            CamelCardsGame.Hand(cards) to rank.toLong()
        }
            .run {
                CamelCardsGame(this)
            }
    }
}
