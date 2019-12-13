package me.salzinger

object ArcadeTile {
    const val EMPTY = 0
    const val WALL = 1
    const val BLOCK = 2
    const val PADDLE = 3
    const val BALL = 4
}

fun Map<Point, Int>.printArcadeGame() =
    print {
        when (it) {
            ArcadeTile.EMPTY -> " "
            ArcadeTile.WALL -> "█"
            ArcadeTile.BLOCK -> "X"
            ArcadeTile.PADDLE -> "_"
            ArcadeTile.BALL -> "0"
            else -> "?"
        }
    }

fun main() {
    25.solve {
        convertIntcodeInput()
            .first()
            .run {
                IntcodeProgramInterpreter(
                    memory = this
                ).evaluate()
                    .run {
                        assert(executionState == ExecutionState.COMPLETED)
                        executionContext.output.getOutput()
                    }

            }.chunked(3)
            .map { (x, y, t) ->
                Point(x.toInt(), y.toInt()) to t.toInt()
            }
            .toMap()
            .count { (_, value) -> value == ArcadeTile.BLOCK }
            .toString()
    }
}