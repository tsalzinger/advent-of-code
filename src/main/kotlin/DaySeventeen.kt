package me.salzinger

import java.math.BigInteger

val DRONE_VALUES = setOf('^', 'v', '<', '>')

data class Pixel(
    val value: Char
) {
    val hasDrone = DRONE_VALUES.contains(value)
    val isEmpty = '.' == value
    val hasScaffold = !isEmpty
}

class CameraOutput(private val pixels: Map<Point, Pixel>) : Map<Point, Pixel> by pixels {
    val intersections: List<Point> by lazy {
        pixels.filter { (point, pixel) ->
            pixel.hasScaffold && point.countAdjacentScaffolds() > 2
        }.map { (point, _) -> point }
    }

    fun print() = pixels.print { value, _ ->
        value?.value.toString()
    }

    private fun Point.countAdjacentScaffolds(): Int {
        return listOf(
            up(1),
            right(1),
            down(1),
            left(1)
        ).count {
            pixels[it]?.hasScaffold == true
        }
    }
}

fun Memory.getCameraOutput(): CameraOutput {
    return IntcodeProgramInterpreter(
        memory = this
    ).evaluate()
        .executionContext
        .output
        .getOutput()
        .map(Number::toChar)
        .run {
            val scaffoldMap = mutableMapOf<Point, Pixel>()
            var currentPosition = Point(0, 0)

            forEach {
                currentPosition = if (it == '\n') {
                    currentPosition.down(1).copy(x = 0)
                } else {
                    scaffoldMap[currentPosition] = Pixel(
                        it
                    )
                    currentPosition.right(1)
                }
            }

            CameraOutput(scaffoldMap.toMap())
        }
}

fun List<String>.toAsciiInput(): List<BigInteger> {
    return "${joinToString("\n")}\n".toAsciiInput()
}

fun String.toAsciiInput(): List<BigInteger> {
    return map { it.toInt().toBigInteger() }
}

fun main() {
    33.solve {
        first()
            .convertIntcodeInput()
            .getCameraOutput()
            .run {
                val intersectionsCount = intersections
                    .map { (x, y) -> x * -y }
                    .sum()
                "${print()}\n\n$intersectionsCount"
            }
    }

    34.solve {
        first()
            .convertIntcodeInput()
            .run {
                IntcodeProgramInterpreter(
                    memory = this,
                    overrides = mapOf(0 to BigInteger.TWO),
                    inputs = ListInputProvider(
                        listOf(
                            "A,B,B,A,B,C,A,C,B,C",
                            "L,4,L,6,L,8,L,12",
                            "L,8,R,12,L,12",
                            "R,12,L,6,L,6,L,8",
                            "n"
                        ).toAsciiInput()
                    ),
                    outputRecorder = object : ListOutputRecorder() {
                        override fun addValue(value: BigInteger) {
                            super.addValue(value)
                            if (value <= 126.toBigInteger()) {
                                print("${value.toInt().toChar()}")
                            } else {
                                print(value)
                            }
                        }
                    }
                )
            }
            .evaluate()
            .executionContext
            .output
            .getOutput()
            .last()
            .toString()
    }
}
// A,B,B,A,B,C,A,C,B,C
//val m = "L,4,L,6,L,8,L,12,L,8,R,12,L,12,L,8,R,12,L,12,L,4,L,6,L,8,L,12,L,8,R,12,L,12,R,12,L,6,L,6,L,8,L,4,L,6,L,8,L,12,R,12,L,6,L,6,L,8,L,8,R,12,L,12,R,12,L,6,L,6,L,8"
//val A = "L,4,L,6,L,8,L,12"
//val B = "L,8,R,12,L,12"
//val C = "R,12,L,6,L,6,L,8"