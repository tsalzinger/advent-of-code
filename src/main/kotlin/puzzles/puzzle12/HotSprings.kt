package me.salzinger.puzzles.puzzle12

import me.salzinger.common.extensions.toIntList
import me.salzinger.puzzles.puzzle12.HotSprings.Spring.Companion.toSpring
import me.salzinger.puzzles.puzzle12.HotSprings.SpringGroup.Companion.toSpringGroup

object HotSprings {
    sealed class Spring(val representation: Char) {
        object Operational : Spring('.')
        object Damaged : Spring('#')
        object Unknown : Spring('?')

        override fun toString() = "$representation"

        companion object {
            fun Char.toSpring(): Spring {
                return when (this) {
                    '#' -> Damaged
                    '?' -> Unknown
                    '.' -> Operational
                    else -> throw RuntimeException("Unknown spring representation $this")
                }
            }
        }
    }

    data class SpringGroup(val springs: List<Spring>) {

        fun getLatestStartIndex(): Int {
            return springs.indexOfFirst {
                it is Spring.Damaged
            }.takeUnless { it == -1 } ?: (springs.count() - 1)
        }

        fun getRemainingPossibleGroups(size: Int): List<SpringGroup> {
            return (0..((springs.count() - size).coerceAtMost(getLatestStartIndex())))
                .filter { groupStartIndex ->
                    val groupEndIndex = groupStartIndex + size

                    // the next spring after this group cannot be a damaged spring
                    groupEndIndex == springs.count() || springs[groupEndIndex] is Spring.Unknown
                }.map { groupStartIndex ->
                    springs.drop(groupStartIndex + size + 1)
                }.map(::SpringGroup)
        }

        companion object {
            fun String.toSpringGroup(): SpringGroup {
                return SpringGroup(
                    map {
                        it.toSpring()
                    }
                )
            }
        }

        override fun toString(): String {
            return springs.joinToString("")
        }
    }

    fun List<SpringGroup>.fit(groupSize: Int): List<List<SpringGroup>> {
        // select until first group contains damages spring
        var damagedSpringSelected = false
        val springGroups = mutableListOf<SpringGroup>()
        var currentIndex = 0
        while (!damagedSpringSelected && currentIndex < count()) {
            val currentGroup = get(currentIndex)
            springGroups.add(currentGroup)
            currentIndex++
            damagedSpringSelected = Spring.Damaged in currentGroup.springs
        }

        return springGroups.flatMapIndexed { index, springGroup ->

            val remainder = springGroup
                .getRemainingPossibleGroups(groupSize)

            val untouched = drop(index + 1)

            remainder
                .map {
                    listOf(it) + untouched
                }
        }
    }

    fun List<List<SpringGroup>>.fitAll(groupSizes: List<Int>): List<List<SpringGroup>> {
        return groupSizes.fold(this) { currentPossibilities, groupSize ->
            currentPossibilities
                .flatMap {
                    it.fit(groupSize)
                }
        }
    }

    fun String.toSpringGroupsAndGroupSizes(): Pair<List<SpringGroup>, List<Int>> {
        val (records, contiguousGroupSizesInput) = split(" ")
        val contiguousGroupSizes = contiguousGroupSizesInput.toIntList(",")

        val possibleDamagedSpringGroups = records.trim('.').split(".")
            .map { it.toSpringGroup() }

        return possibleDamagedSpringGroups to contiguousGroupSizes
    }

    fun String.getPossibleSpringArrangements(): Int {
        val (springGroups, groupSizes) = toSpringGroupsAndGroupSizes()

        return listOf(springGroups)
            .fitAll(groupSizes)
            .filter { springGroups ->
                springGroups
                    .all {
                        Spring.Damaged !in it.springs
                    }
            }.count {
                it.isNotEmpty()
            }
    }


    fun Sequence<String>.mapToNumberOfPossibleSpringArrangements() = map {
        it.getPossibleSpringArrangements()
    }

    fun Sequence<String>.getSumOfPossibleSpringArrangements(): Long {
        return mapToNumberOfPossibleSpringArrangements()
            .sumOf {
                it.toLong()
            }
    }
}
