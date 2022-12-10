package puzzles

import me.salzinger.common.streamInput

object Puzzle10CathodeRayTube {

    interface State<T> {
        val value: T
    }

    data class SingleRegisterState(override val value: Int) : State<Int>

    object Part1 {

        fun String.instructionToOperators(): Sequence<SingleRegisterState.() -> SingleRegisterState> {
            val instructionParts = split(" ")
            return when (val instructionOperation = instructionParts.first()) {
                "noop" -> sequenceOf({ this })
                "addx" -> {
                    val valueToAdd = instructionParts[1].toInt()
                    sequenceOf(
                        { this },
                        { copy(value = value + valueToAdd) }
                    )
                }

                else -> throw RuntimeException("Failed to parse instruction: $this")
            }
        }

        fun Sequence<String>.solve(): Int {
            return flatMap { it.instructionToOperators() }
                .foldIndexed(SingleRegisterState(1) to 0) { index, (registerState, signalStrengthSum), operator ->
                    if ((index + 1 - 20) % 40 == 0) {
                        println("\t${index + 1}: ${registerState.value}")
                        val signalStrength = (index + 1) * registerState.value
                        operator(registerState) to (signalStrengthSum + signalStrength)
                    } else {
                        operator(registerState) to signalStrengthSum
                    }

                }.second
        }

        @JvmStatic
        fun main(args: Array<String>) {
            "puzzle-10.in"
                .streamInput()
                .solve()
                .run(::println)
        }
    }

    object Part2 {

        fun Sequence<String>.solve(): Int {
            TODO()
        }

        @JvmStatic
        fun main(args: Array<String>) {
            "puzzle-10.in"
                .streamInput()
                .solve()
                .run(::println)
        }
    }
}
