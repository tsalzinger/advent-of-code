import me.salzinger.common.streamInput
import org.junit.jupiter.api.Test
import puzzles.Puzzle08TreetopTreeHouse.Part2.solve

class Puzzle08Part2TreetopTreeHouseTest {
    @Test
    fun testTreeScenicScore() {
        "puzzle-8-example-1.in"
            .streamInput()
            .solve()
            .assertThat {
                isEqualTo(8)
            }
    }
}

