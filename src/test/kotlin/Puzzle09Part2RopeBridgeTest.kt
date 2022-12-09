import me.salzinger.common.Grid2D
import me.salzinger.common.streamInput
import org.junit.jupiter.api.Test
import puzzles.Puzzle09RopeBridge.Part1.simulateTailMovement
import puzzles.Puzzle09RopeBridge.Part1.visitedCoordinates
import puzzles.Puzzle09RopeBridge.Part2.solve

class Puzzle09Part2RopeBridgeTest {
    @Test
    fun countTailPositions() {
        "puzzle-9-example-1.in"
            .streamInput()
            .solve()
            .assertThat {
                isEqualTo(1)
            }
    }

    @Test
    fun countTailPositions2() {
        "puzzle-9-example-2.in"
            .streamInput()
            .solve()
            .assertThat {
                isEqualTo(36)
            }
    }

    @Test
    fun diagonalHeadMovement() {
        sequenceOf<Grid2D.Coordinate.() -> Grid2D.Coordinate>(
            { up().right() },
            { up().left() },
        ).simulateTailMovement()
            .visitedCoordinates()
            .last()
            .assertThat {
                isEqualTo(Grid2D.Coordinate(-1, 0))
            }
    }
}

