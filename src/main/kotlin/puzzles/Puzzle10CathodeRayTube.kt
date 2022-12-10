package puzzles

import me.salzinger.common.streamInput
import puzzles.Puzzle10CathodeRayTube.Part1.instructionToOperators

object Puzzle10CathodeRayTube {

    interface State<T> {
        val value: T
    }

    data class SingleRegisterState(override val value: Int) : State<Int>

    object Part1 {

        fun String.instructionToOperators(): Sequence<SingleRegisterState.() -> SingleRegisterState> {
            val instructionParts = split(" ")
            return when (instructionParts.first()) {
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
        private const val CRT_SCREEN_WIDTH = 40

        fun Sequence<String>.solve(): String {
            return flatMap { it.instructionToOperators() }
                .foldIndexed(SingleRegisterState(1) to listOf<Char>()) { index, (registerState, screenOutput), operator ->
                    val currentPixel =
                        if (index % CRT_SCREEN_WIDTH in (registerState.value - 1)..(registerState.value + 1)) {
                            '#'
                        } else {
                            '.'
                        }

                    println("\t${index + 1}: $currentPixel (${registerState.value})")

                    operator(registerState) to (screenOutput + currentPixel)

                }.second
                .chunked(CRT_SCREEN_WIDTH)
                .joinToString("\n") { it.joinToString("") }
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
