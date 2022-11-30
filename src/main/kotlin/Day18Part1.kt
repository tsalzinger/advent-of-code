package me.salzinger

import java.util.*
import kotlin.math.ceil
import kotlin.math.floor

sealed interface SnailNumberToken {
    object PairStart : SnailNumberToken {
        override fun toString(): String {
            return "["
        }
    }

    object PairEnd : SnailNumberToken {
        override fun toString(): String {
            return "]"
        }
    }

    object Separator : SnailNumberToken {
        override fun toString(): String {
            return ","
        }
    }

    data class Number(
        val value: Long
    ) : SnailNumberToken {
        override fun toString(): String {
            return "$value"
        }

        operator fun plus(value: Int): Number {
            return Number(this.value + value)
        }

        operator fun plus(value: Number): Number {
            return Number(this.value + value.value)
        }
    }
}


typealias SnailNumber = List<SnailNumberToken>

operator fun SnailNumber.plus(snailNumber: SnailNumber): SnailNumber {
    return buildList {
        add(SnailNumberToken.PairStart)
        addAll(this@plus)
        add(SnailNumberToken.Separator)
        addAll(snailNumber)
        add(SnailNumberToken.PairEnd)
    }
}

fun SnailNumber.sum(snailNumber: SnailNumber): SnailNumber {
    return plus(snailNumber)
        .run {
            var previous: SnailNumber
            var current = this

            do {
                do {
                    previous = current
                    current = current.explode()
                } while (previous != current)

                previous = current
                current = current.split()
            } while (previous != current)

            current
        }
}

fun <T> MutableList<T>.replaceFirst(
    condition: (T) -> Boolean,
    replace: (T) -> T
): List<T> {
    val index = indexOfFirst(condition)
    return replaceAt(index, replace)
}

fun <T> MutableList<T>.replaceLast(
    condition: (T) -> Boolean,
    replace: (T) -> T
): List<T> {
    val index = indexOfLast(condition)
    return replaceAt(index, replace)
}

fun <T> MutableList<T>.replaceAt(
    index: Int,
    replace: (T) -> T
): List<T> {
    if (index != -1) {
        set(index, replace(get(index)))
    }

    return this
}

inline fun <T, reified S : T> MutableList<T>.replaceFirstOf(
    crossinline replace: (S) -> S
): List<T> {
    return replaceFirst({ it is S }, { replace.invoke((it as S)) })
}

inline fun <T, reified S : T> MutableList<T>.replaceLastOf(
    crossinline replace: (S) -> T
): List<T> {
    return replaceFirst({ it is S }, { replace(it as S) })
}

fun SnailNumber.explode(): SnailNumber {
    var index = 0
    var depth = 0
    val originalList = this

    return buildList<SnailNumberToken> {
        val listBuilder = this

        while (index < originalList.count()) {
            when (val snailNumberToken = originalList[index]) {
                is SnailNumberToken.Number -> {
                    if (depth == 5) {
                        if (listBuilder.last() == SnailNumberToken.PairStart) {
                            listBuilder.replaceLastOf<SnailNumberToken, SnailNumberToken.Number> { it + snailNumberToken }
                        } else {
                            listBuilder.removeLast()
                            listBuilder.removeLast()
                            listBuilder.add(SnailNumberToken.Number(0))
                            listBuilder.addAll(
                                originalList.slice((index + 2) until originalList.count())
                                    .toMutableList()
                                    .replaceFirstOf<SnailNumberToken, SnailNumberToken.Number> { it + snailNumberToken }
                            )
                            return this@buildList
                        }
                    } else {
                        listBuilder.add(snailNumberToken)
                    }
                }

                SnailNumberToken.PairEnd -> {
                    depth--
                    listBuilder.add(snailNumberToken)
                }

                SnailNumberToken.PairStart -> {
                    depth++
                    listBuilder.add(snailNumberToken)
                }

                SnailNumberToken.Separator -> {
                    listBuilder.add(snailNumberToken)
                }
            }
            index++
        }
    }
}

fun SnailNumber.split(): SnailNumber {
    val index = this.indexOfFirst { it is SnailNumberToken.Number && it.value >= 10 }

    return if (index != -1) {
        val originalList = this
        buildList {
            addAll(originalList.slice(0 until index))
            val numberToken = originalList.get(index) as SnailNumberToken.Number
            add(SnailNumberToken.PairStart)
            val halvedValue = numberToken.value.toDouble() / 2
            add(SnailNumberToken.Number(floor(halvedValue).toLong()))
            add(SnailNumberToken.Separator)
            add(SnailNumberToken.Number(ceil(halvedValue).toLong()))
            add(SnailNumberToken.PairEnd)
            addAll(originalList.slice((index + 1) until originalList.count()))
        }
    } else {
        this
    }
}

fun String.toSnailNumber(): SnailNumber {
    val numberBuilder = StringBuilder()

    return flatMap {
        when (it) {
            '[' -> listOf(SnailNumberToken.PairStart)
            ']' -> {
                buildList {
                    if (numberBuilder.isNotEmpty()) {
                        add(SnailNumberToken.Number(numberBuilder.toString().toLong()))
                        numberBuilder.clear()
                    }
                    add(SnailNumberToken.PairEnd)
                }
            }

            ',' -> {
                buildList {
                    if (numberBuilder.isNotEmpty()) {
                        add(
                            SnailNumberToken.Number(numberBuilder.toString().toLong())
                        )
                        numberBuilder.clear()
                    }
                    add(SnailNumberToken.Separator)
                }
            }

            ' ' -> listOf(SnailNumberToken.Separator)
            else -> {
                numberBuilder.append(it)
                emptyList()
            }
        }
    }
}

private fun List<String>.toSnailNumbers(): List<SnailNumber> {
    return map(String::toSnailNumber)
}

private fun List<String>.getSum(): String {
    return toSnailNumbers()
        .reduce(SnailNumber::sum)
        .joinToString("")
}

//private fun List<String>.getMagnitudeOfSum(): String {
//    toSnailNumbers()
//    TODO()
//}

fun main() {
    18.solveExamples(
        expectedSolutions = listOf(
            "[[[[1,1],[2,2]],[3,3]],[4,4]]",
            "[[[[3,0],[5,3]],[4,4]],[5,5]]",
            "[[[[5,0],[7,4]],[5,5]],[6,6]]",
            "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]",
            "[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]",
        ),
        solver = List<String>::getSum
    )

//    18.solveExample(
//        exampleNumber = 5,
//        expectedSolution = "4140",
//        solver = List<String>::getMagnitudeOfSum
//    )
//
//    18.solve(1, List<String>::getMagnitudeOfSum)
}

private fun String.inPairs(): List<String> {
    return (0..(count() - 2)).map {
        substring(it, it + 2)
    }
}
