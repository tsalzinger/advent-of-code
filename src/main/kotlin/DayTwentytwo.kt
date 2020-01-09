package me.salzinger

import kotlin.math.absoluteValue

fun main() {
    22.solve(1) {
        applyTechniquesTo(getFactoryOrderDeck())
            .indexOf(2019)
            .toString()
    }
}

fun getFactoryOrderDeck(size: Int = 10_007): Deck = List(size) { it }

fun List<String>.applyTechniquesTo(deck: Deck): Deck {
    return toDeckTechniques()
        .fold(
            deck,
            { currentDeck, (deckTechnique, n) -> deckTechnique(currentDeck, n) })
}

fun List<String>.toDeckTechniques(): List<Pair<DeckTechnique, Int?>> {
    return this.map(String::toDeckTechnique)
}

fun String.toDeckTechnique(): Pair<DeckTechnique, Int?> {
    return TECHNIQUES.keys.mapNotNull {
        if (this.startsWith(it)) {
            TECHNIQUES.getValue(it) to this.takeLast(this.length - it.length).trim().toIntOrNull()
        } else {
            null
        }
    }.first()
}

typealias Deck = List<Int>
typealias DeckTechnique = (Deck, Int?) -> Deck

val DEAL_INTO_NEW_STACK: DeckTechnique = { deck, _ -> deck.reversed() }
val CUT: DeckTechnique = { deck, n ->
    if ((n ?: throw NullPointerException()) > 0) {
        deck.takeLast(deck.size - n) + deck.take(n.absoluteValue)
    } else {
        deck.takeLast(n.absoluteValue) + deck.take(deck.size + n)
    }
}

val DEAL_WITH_INCREMENT: DeckTechnique = { deck, n ->
    var nextPosition = 0
    val array = Array(deck.size) { -1 }

    deck.forEach {
        array[nextPosition] = it
        nextPosition = (nextPosition + n!!) % deck.size
    }

    array.toList()
}

val TECHNIQUES = mapOf(
    "deal into new stack" to DEAL_INTO_NEW_STACK,
    "cut" to CUT,
    "deal with increment" to DEAL_WITH_INCREMENT
)