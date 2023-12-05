package puzzles.puzzle5

import assertIterable
import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle5.IfYouGiveASeedAFertilizer.toAlmanac
import org.junit.jupiter.api.Test

class IfYouGiveASeedAFertilizerTest {
    @Test
    fun `example 1 - part 1`() {
        "puzzles/puzzle5/puzzle-5-example-1.in"
            .streamInput()
            .toAlmanac()
            .getLastCategoryNumbers()
            .min()
            .assertThat {
                isEqualTo(35L)
            }
    }

    @Test
    fun `example 1 - get location numbers`() {
        "puzzles/puzzle5/puzzle-5-example-1.in"
            .streamInput()
            .toAlmanac()
            .getLastCategoryNumbers()
            .assertIterable {
                containsExactly(82L, 43L, 86L, 35L)
            }
    }
}
