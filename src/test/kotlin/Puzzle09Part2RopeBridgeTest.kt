import me.salzinger.common.streamInput
import org.junit.jupiter.api.Test
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
}

