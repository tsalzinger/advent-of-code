package me.salzinger

import java.util.*

sealed interface SnailNumber {
    val magnitude: Long

    fun sum(snailNumber: SnailNumber): SnailNumber
    fun explode(): SnailNumber.Pair
    fun reduce(): SnailNumber.Pair
    fun split(): SnailNumber.Pair

    @JvmInline
    value class Regular(val number: Long) : SnailNumber {
        override val magnitude: Long
            get() = number

        override fun sum(snailNumber: SnailNumber): SnailNumber {
            TODO("Regular number cannot be summed up")
        }

        override fun explode(): SnailNumber.Pair {
            TODO("Regular number cannot be summed up")
        }

        override fun reduce(): SnailNumber.Pair {
            TODO("Regular number cannot be summed up")
        }

        override fun split(): SnailNumber.Pair {
            TODO("Regular number cannot be summed up")
        }

        override fun toString(): String {
            return "$number"
        }
    }

    data class Pair(val left: SnailNumber, val right: SnailNumber) : SnailNumber {
        override val magnitude: Long
            get() = (3 * left.magnitude + 2 * right.magnitude)

        operator fun plus(snailNumber: SnailNumber): SnailNumber {
            return SnailNumber.Pair(this, snailNumber)
        }

        override fun sum(snailNumber: SnailNumber): SnailNumber {
            return plus(snailNumber)
                .reduce()
        }

        override fun explode(): SnailNumber.Pair {
            explodeInternal()

            TODO()
        }

        fun explodeInternal(depth: Int = 1): SnailNumber.Pair {

            if (depth == 4) {
                if (left is Regular) {

                }
            }



            TODO()
        }

        override fun split(): SnailNumber.Pair {
            TODO()
        }

        override fun reduce(): SnailNumber.Pair {
            var previous: SnailNumber
            var current = this

            do {
                previous = current
                current = current.explode()

            } while (previous != current)

            TODO()
        }

        override fun toString(): String {
            return "[$left, $right]"
        }
    }
}

class PairBuilder {
    lateinit var left: SnailNumber
    lateinit var right: SnailNumber

    fun build(): SnailNumber.Pair {
        return SnailNumber.Pair(left, right)
    }
}

private fun String.parseSnailNumber(): Pair<SnailNumber, String> {
    return when (val startChar = first()) {
        '[' -> {
            val (left, rightRemainder) = drop(1).parseSnailNumber()
            val (right, remainder) = rightRemainder.parseSnailNumber()

            SnailNumber.Pair(
                left,
                right
            ) to remainder.drop(1)
        }
        in '0'..'9' -> {
            val (number, rightRemainder) = split(",", "]", limit = 2)

            println(number to rightRemainder)

            SnailNumber.Regular(number.toLong()) to rightRemainder
        }
        else -> {
            TODO("This branch should never be reached - first character of $this is unexpected")
        }

    }
}

private fun String.toSnailNumber(initialDepth: Int = 0): SnailNumber {
    return parseSnailNumber().let { (snailNumber, remainder) ->
        println("Number: $snailNumber")
        println("Remainder: $remainder")

        snailNumber
    }

//    println(this)
//    var depth = 0
//    var stack = Stack<PairBuilder>()
//    var idx = 0
//
//    while (true) {
//        when (val startChar = get(idx)) {
//            '[' -> stack.push(PairBuilder())
//            ']' -> stack.peek().build() // TODO - is this pair left or right of the above one?
//            in '0'..'9' -> {
//                val (number, remainder) = split(",", limit = 2)
//
//                if (remainder.first() == '[') {
//                    // pair right
//                } else {
//                    // regular number right
//                }
//
//                stack.peek().left = SnailNumber.Regular(number.toLong())
//            }
//
//        }
//
//        if (get(idx) == '[') {
//            stack.push(PairBuilder())
//        }
//    }
//
//    val remainder = dropWhile {
//        if (it == '[') {
//            depth++
//            true
//        } else {
//            false
//        }
//    }
//
//    var (left, right) = remainder.split(',', limit = 2)
//
//    println(left)
//    println(right)
//
//    var leftRegular = left.toInt()
//
////    if (right.)
//
//    TODO()
////    return SnailNumber.Pair(left, right)
}

private fun List<String>.toSnailNumbers(): List<SnailNumber> {
    return map(String::toSnailNumber)
}

private fun List<String>.getSum(): String {
    return toSnailNumbers()
        .reduce(SnailNumber::sum)
        .toString()
}

private fun List<String>.getMagnitudeOfSum(): String {
    toSnailNumbers()
    TODO()
}

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


    18.solveExample(
        exampleNumber = 5,
        expectedSolution = "4140",
        solver = List<String>::getMagnitudeOfSum
    )

    18.solve(1, List<String>::getMagnitudeOfSum)
}

private fun String.inPairs(): List<String> {
    return (0..(count() - 2)).map {
        substring(it, it + 2)
    }
}
