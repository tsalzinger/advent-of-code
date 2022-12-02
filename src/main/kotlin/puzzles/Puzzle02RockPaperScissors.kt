package puzzles

import me.salzinger.common.extensions.toPairs
import me.salzinger.common.solve

object Puzzle02RockPaperScissors {
    enum class Shape {
        Rock,
        Paper,
        Scissor,
    }

    fun Shape.outcomeAgainst(shape: Shape): Outcome = when (this) {
        Shape.Rock -> when (shape) {
            Shape.Rock -> Outcome.Draw
            Shape.Paper -> Outcome.Loose
            Shape.Scissor -> Outcome.Win
        }

        Shape.Paper -> when (shape) {
            Shape.Rock -> Outcome.Win
            Shape.Paper -> Outcome.Draw
            Shape.Scissor -> Outcome.Loose
        }

        Shape.Scissor -> when (shape) {
            Shape.Rock -> Outcome.Loose
            Shape.Paper -> Outcome.Win
            Shape.Scissor -> Outcome.Draw
        }
    }

    fun Shape.playerShapeForOutcome(outcome: Outcome): Shape = when (this) {
        Shape.Rock -> when (outcome) {
            Outcome.Loose -> Shape.Scissor
            Outcome.Draw -> Shape.Rock
            Outcome.Win -> Shape.Paper
        }

        Shape.Paper -> when (outcome) {
            Outcome.Loose -> Shape.Rock
            Outcome.Draw -> Shape.Paper
            Outcome.Win -> Shape.Scissor
        }

        Shape.Scissor -> when (outcome) {
            Outcome.Loose -> Shape.Paper
            Outcome.Draw -> Shape.Scissor
            Outcome.Win -> Shape.Rock
        }
    }

    enum class Outcome {
        Loose,
        Draw,
        Win,
    }

    data class Game(val opponentShape: Shape, val playerShape: Shape) {
        fun getScore(scoringStrategy: ScoringStrategy): Int {
            return scoringStrategy(playerShape, playerShape.outcomeAgainst(opponentShape))
        }
    }

    fun String.parseGameFromOpponentAndPlayerShape(): Game {
        return split(" ").map { it.toShape() }.toPairs().single().run {
            Game(first, second)
        }
    }

    fun String.parseGameFromOpponentShapeAndOutcome(): Game {
        val (opponentShape, outcome) = split(" ")

        return with(opponentShape.toShape()) {
            Game(this, this.playerShapeForOutcome(outcome.toOutcome()))
        }
    }

    fun String.toShape(): Shape = when (this) {
        in setOf("A", "X") -> Shape.Rock
        in setOf("B", "Y") -> Shape.Paper
        in setOf("C", "Z") -> Shape.Scissor
        else -> throw RuntimeException("Unknown shape encountered: $this")
    }

    fun String.toOutcome(): Outcome = when (this) {
        in setOf("X") -> Outcome.Loose
        in setOf("Y") -> Outcome.Draw
        in setOf("Z") -> Outcome.Win
        else -> throw RuntimeException("Unknown outcome encountered: $this")
    }

    interface ScoringStrategy {
        operator fun invoke(playerShape: Shape, outcome: Outcome): Int
    }

    object Part1ScoringStrategy : ScoringStrategy {
        override fun invoke(playerShape: Shape, outcome: Outcome): Int {
            val shapePoints = when (playerShape) {
                Shape.Rock -> 1
                Shape.Paper -> 2
                Shape.Scissor -> 3
            }

            val outcomePoints = when (outcome) {
                Outcome.Loose -> 0
                Outcome.Draw -> 3
                Outcome.Win -> 6
            }

            return shapePoints + outcomePoints
        }
    }

    object Part1Solver {
        @JvmStatic
        fun main(args: Array<String>) {
            2.solve(part = 1) {
                sumOf {
                    it.parseGameFromOpponentAndPlayerShape().getScore(Part1ScoringStrategy)
                }.toString()
            }
        }
    }

    object Part2Solver {
        @JvmStatic
        fun main(args: Array<String>) {
            2.solve(part = 2) {
                sumOf {
                    it.parseGameFromOpponentShapeAndOutcome().getScore(Part1ScoringStrategy)
                }.toString()
            }
        }
    }
}
