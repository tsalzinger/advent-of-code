import me.salzinger.common.streamInput
import org.junit.jupiter.api.Test
import puzzles.Puzzle09RopeBridge.Part1.solve

class Puzzle09Part1RopeBridgeTest {
    @Test
    fun countTailPositions() {
        "puzzle-9-example-1.in"
            .streamInput()
            .solve()
            .assertThat {
                isEqualTo(13)
            }
    }
}

