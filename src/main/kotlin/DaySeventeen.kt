package me.salzinger

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

fun main() {
    33.solve {
        first()
            .convertIntcodeInput()
            .run {
                IntcodeProgramInterpreter(
                    memory = this
                )
            }
            .evaluate()
            .executionContext
            .output
            .getOutput()
            .map(Number::toChar)
            .run {
                val scaffoldMap = mutableMapOf<Point, Pixel>()
                var currentPosition = Point(0, 0)

                forEach {
                    currentPosition = if (it == '\n') {
                        currentPosition.up(1).copy(x = 0)
                    } else {
                        scaffoldMap[currentPosition] = Pixel(
                            it
                        )
                        currentPosition.right(1)
                    }
                }

                CameraOutput(scaffoldMap.toMap())
            }
            .intersections
            .map { (x, y) -> x * y }
            .sum()
            .toString()
    }
}