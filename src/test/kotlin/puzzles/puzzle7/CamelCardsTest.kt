package puzzles.puzzle7

import assertIterable
import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle6.CamelCards.toCamelCardGame
import org.junit.jupiter.api.Test

class CamelCardsTest {
    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle7/puzzle-7-example-1.in"
            .streamInput()
            .toCamelCardGame()
            .getTotalWinnings()
            .assertThat {
                isEqualTo(6440L)
            }
    }

    @Test
    fun `comparsion`() {
        listOf(
            "32T3K",
            "T55J5",
            "KK677",
            "KTJJT",
            "QQQJA",
        )
            .map {
                CamelCardsGame.Hand(it)
            }
            .sorted()
            .map {
                it.cards
            }
            .assertIterable {
                containsExactly(
                    "32T3K",
                    "KTJJT",
                    "KK677",
                    "T55J5",
                    "QQQJA",
                )
            }
    }


    @Test
    fun `example 1 - part 2`() {
        "puzzles/puzzle7/puzzle-7-example-1.in"
            .streamInput()
            .toCamelCardGame()
            .withJoker()
            .getTotalWinnings()
            .assertThat {
                isEqualTo(5905L)
            }
    }


    @Test
    fun `comparsion with joker`() {
        listOf(
            "32T3K",
            "T55J5",
            "KK677",
            "KTJJT",
            "QQQJA",
        )
            .map {
                CamelCardsGame.Hand(it)
            }
            .sortedWith(CamelCardsGame.HandComparatorWithJoker())
            .map {
                it.cards
            }
            .assertIterable {
                containsExactly(
                    "32T3K",
                    "KK677",
                    "T55J5",
                    "QQQJA",
                    "KTJJT",
                )
            }
    }

    @Test
    fun `type`() {
        CamelCardsGame.Hand("J28TQ")
            .run {
                type.assertThat {
                    isEqualTo(CamelCardsGame.Hand.Type.HIGH_CARD)
                }
            }
    }

    @Test
    fun `joker type`() {
        CamelCardsGame.Hand("J28TQ")
            .run {
                jokerType.assertThat {
                    isEqualTo(CamelCardsGame.Hand.Type.ONE_PAIR)
                }
            }
    }
}
