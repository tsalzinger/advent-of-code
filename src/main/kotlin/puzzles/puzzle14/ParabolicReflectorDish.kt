package me.salzinger.puzzles.puzzle14

import me.salzinger.puzzles.puzzle13.PointOfIncidence.transpose

object ParabolicReflectorDish {
    object RockType {
        const val EMPTY = '.'
        const val CUBE = '#'
        const val ROUNDED = 'O'
    }

    fun Sequence<String>.getTotalLoadOnNorthBeamAfterATiltToNorth(): Int {
        return tiltNorth()
            .getLoadPerRow()
            .sum()
    }

    fun Sequence<String>.getTotalLoadOnNorthBeamAfterSpins(spinCount: Long): Int {
        return spin(spinCount)
            .getLoadPerRow()
            .sum()
    }

    fun Sequence<String>.tiltNorth(): List<String> {
        return toList().tiltNorth()
    }

    fun Sequence<String>.spin(spinCount: Long): List<String> {
        val cache = mutableMapOf<List<String>, Long>()

        return (1..spinCount).fold(toList()) { currentArrangement, index ->
            val cacheIndex = cache[currentArrangement]
            if (cacheIndex != null) {
                val cycleTime = index - cacheIndex
                val endArrangementIndex = cacheIndex + ((spinCount - cacheIndex) % cycleTime) + 1
                return cache.entries.single { it.value == endArrangementIndex }.key
            } else {
                cache[currentArrangement] = index
            }

            currentArrangement
                .tiltNorth()
                .tiltWest()
                .tiltSouth()
                .tiltEast()
        }
    }

    fun List<String>.tiltNorth(): List<String> {
        return transpose()
            .shiftRoundedRocksToLeft()
            .transpose()
    }

    fun List<String>.tiltWest(): List<String> {
        return shiftRoundedRocksToLeft()
    }

    fun List<String>.tiltEast(): List<String> {
        return reverseRows().shiftRoundedRocksToLeft().reverseRows()
    }

    fun List<String>.tiltSouth(): List<String> {
        return transpose().reverseRows().shiftRoundedRocksToLeft().reverseRows().transpose()
    }

    fun List<String>.reverseRows(): List<String> {
        return map {
            it.reversed()
        }
    }

    fun List<String>.shiftRoundedRocksToLeft(): List<String> {
        return map { row ->
            row
                .split(RockType.CUBE)
                .map { it.toList() }
                .map {
                    it.sortedBy {
                        when (it) {
                            RockType.ROUNDED -> 0
                            RockType.EMPTY -> 1
                            else -> throw RuntimeException("Cannot sort rock type $it")
                        }
                    }
                }
                .joinToString(RockType.CUBE.toString()) {
                    it.joinToString("")
                }
        }
    }

    fun List<String>.getLoadPerRow(): List<Int> {
        return mapIndexed { index, row ->
            (count() - index) * row.count { it == RockType.ROUNDED }
        }
    }
}
