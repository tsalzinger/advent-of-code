package me.salzinger.puzzles.puzzle2

import kotlin.math.max

typealias GameId = Int

object CubeConundrum {
    val colorMapping = Color.entries.associateBy {
        it.name.lowercase()
    }

    enum class Color {
        BLUE,
        RED,
        GREEN,
    }

    data class Game(val draws: List<Map<Color, Int>>) {
        init {
            require(draws.isNotEmpty())
        }

        val maxColorOccurrences: Map<Color, Int> by lazy {
            draws.fold(mutableMapOf()) { acc, map ->
                map.forEach { (color, count) ->
                    acc.compute(color) { _, currentCount ->
                        max(currentCount ?: 0, count)
                    }
                }

                acc
            }
        }

        fun isPossibleWith(availableCubes: Map<Color, Int>): Boolean {
            availableCubes
                .onEach { (color, availableCount) ->
                    if ((maxColorOccurrences[color] ?: 0) > availableCount) {
                        return false
                    }
                }

            return true
        }
    }

    val gameInputPattern = Regex("^Game (?<gameId>\\d+): (?<gameDraws>.+)$")

    fun String.parseAsGame(): Pair<GameId, Game> {
        val matchGroups = gameInputPattern
            .matchEntire(this)
            ?.groups ?: throw RuntimeException("Couldn't parse game from $this")

        val gameId = matchGroups["gameId"]!!.value.toInt()
        val gameDraws = matchGroups["gameDraws"]!!.value
            .splitToSequence(";")
            .map {
                it.split(",").associate { colorDraw ->
                    val (count, color) = colorDraw.trim().split(" ")
                    colorMapping.getValue(color) to count.toInt()
                }
            }
            .toList()

        return gameId to Game(gameDraws)
    }

    fun Sequence<String>.parseAsGames(): Sequence<Pair<Int, Game>> {
        return map {
            it.parseAsGame()
        }
    }

    fun Sequence<Pair<Int, Game>>.filterPossibleGames(availableCubes: Map<Color, Int>): Sequence<Pair<Int, Game>> {
        return filter { (_, game) ->
            game.isPossibleWith(availableCubes)
        }
    }

    fun Sequence<String>.getSumOfPossibleGameIds(availableCubes: Map<Color, Int>): Int {
        return parseAsGames()
            .filterPossibleGames(availableCubes)
            .sumOf { (gameId, _) -> gameId }
    }
}
