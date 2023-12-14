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

    fun Sequence<String>.tiltNorth(): List<String> {
        return toList().tiltNorth()
    }

    fun List<String>.tiltNorth(): List<String> {
        return transpose()
            .shiftRoundedRocksToLeft()
            .transpose()
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
