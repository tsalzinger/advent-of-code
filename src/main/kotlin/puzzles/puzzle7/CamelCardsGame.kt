package puzzles.puzzle7

val cardStrength = "AKQJT98765432".toList()
val jokerCardStrength = "AKQT98765432J".toList()

class CamelCardsGame(
    val handsWithBids: Sequence<Pair<Hand, Long>>,
    val gameMode: GameMode = GameMode.WITHOUT_JOKER,
) {
    enum class GameMode {
        WITHOUT_JOKER,
        WITH_JOKER,
    }

    fun withJoker(): CamelCardsGame {
        return CamelCardsGame(handsWithBids, GameMode.WITH_JOKER)
    }

    fun getTotalWinnings(): Long {
        val comparator = when (gameMode) {
            GameMode.WITHOUT_JOKER -> HandComparator()
            GameMode.WITH_JOKER -> HandComparatorWithJoker()
        }

        return handsWithBids
            .sortedWith { o1, o2 -> comparator.compare(o1?.first, o2?.first) }
            .foldIndexed(0L) { index, sum, (_, rank) ->
                sum + (index + 1) * rank

            }
    }

    data class Hand(val cards: String) : Comparable<Hand> {
        enum class Type {
            FIFE_OF_A_KIND,
            FOUR_OF_A_KIND,
            FULL_HOUSE,
            THREE_OF_A_KIND,
            TWO_PAIR,
            ONE_PAIR,
            HIGH_CARD,
        }

        val type = cards.handType()

        val maxCard = cards.filterNot { it == 'J' }.groupBy { it }.maxByOrNull { it.value.count() }?.key ?: 'J'

        val jokerType = cards.replace('J', maxCard).handType()

        fun String.handType(): Type {
            return groupBy { it }
                .mapValues { (_, cards) -> cards.count() }
                .run {
                    when (count()) {
                        1 -> Type.FIFE_OF_A_KIND
                        2 -> {
                            if (any { it.value == 1 }) {
                                Type.FOUR_OF_A_KIND
                            } else {
                                Type.FULL_HOUSE
                            }
                        }

                        3 -> {
                            if (any { it.value == 2 }) {
                                Type.TWO_PAIR
                            } else {
                                Type.THREE_OF_A_KIND
                            }
                        }

                        4 -> Type.ONE_PAIR

                        else -> Type.HIGH_CARD
                    }
                }
        }

        override fun compareTo(other: Hand): Int {
            val typeComparison = other.type.compareTo(type)

            return if (typeComparison != 0) {
                typeComparison
            } else {
                cards.zip(other.cards)
                    .firstNotNullOfOrNull { (c1, c2) ->
                        val comparison = cardStrength.indexOf(c2).compareTo(cardStrength.indexOf(c1))

                        if (comparison == 0) {
                            null
                        } else {
                            comparison
                        }
                    } ?: 0
            }
        }
    }

    class HandComparator : Comparator<Hand> {
        override fun compare(o1: Hand?, o2: Hand?): Int {
            return when {
                o1 == o2 -> 0
                o1 == null -> -1
                o2 == null -> 1
                else -> o1.compareTo(o2)
            }
        }
    }

    class HandComparatorWithJoker : Comparator<Hand> {
        override fun compare(o1: Hand?, o2: Hand?): Int {
            return when {
                o1 == o2 -> 0
                o1 == null -> -1
                o2 == null -> 1
                else -> {
                    val typeComparison = o2.jokerType.compareTo(o1.jokerType)

                    if (typeComparison != 0) {
                        typeComparison
                    } else {
                        o1.cards.zip(o2.cards)
                            .firstNotNullOfOrNull { (c1, c2) ->
                                val comparison = jokerCardStrength.indexOf(c2).compareTo(jokerCardStrength.indexOf(c1))

                                if (comparison == 0) {
                                    null
                                } else {
                                    comparison
                                }
                            } ?: 0
                    }
                }
            }
        }
    }
}
