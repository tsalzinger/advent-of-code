import me.salzinger.common.streamInput
import org.junit.jupiter.api.Test
import puzzles.Puzzle08TreetopTreeHouse.Part1.solve

class Puzzle08Part1TreetopTreeHouseTest {
    @Test
    fun testTreeVisibility() {
        "puzzle-8-example-1.in"
            .streamInput()
            .solve()
            .toString()
            .assertThat {
                isEqualTo(
                    "puzzle-8-1-example-1.solution".streamInput().first()
                )
            }
    }
}

