package puzzles.puzzle5

import assertBoolean
import assertIterable
import assertThat
import me.salzinger.common.streamInput
import me.salzinger.puzzles.puzzle5.IfYouGiveASeedAFertilizer
import me.salzinger.puzzles.puzzle5.IfYouGiveASeedAFertilizer.toAlmanac
import org.junit.jupiter.api.Nested
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

    @Test
    fun `example 1 - part 2`() {
        "puzzles/puzzle5/puzzle-5-example-1.in"
            .streamInput()
            .toAlmanac()
            .getMinNumberOfLastCategoryRanges()
            .assertThat {
                isEqualTo(46L)
            }
    }

    @Nested
    inner class CategoryMapping {
        @Test
        fun `mapAndSplit whole range`() {
            IfYouGiveASeedAFertilizer
                .CategoryMapping(
                    sourceRange = 10L..20L,
                    destinationRange = 40L..50L,
                )
                .mapAndSplit(10L..20L)
                .also { (mappedRange, remainder) ->
                    mappedRange.assertThat {
                        isEqualTo(40L..50L)
                    }

                    remainder.assertIterable {
                        isEmpty()
                    }
                }
        }

        @Test
        fun `mapAndSplit start of range`() {
            IfYouGiveASeedAFertilizer
                .CategoryMapping(
                    sourceRange = 10L..20L,
                    destinationRange = 40L..50L,
                )
                .mapAndSplit(10L..18L)
                .also { (mappedRange, remainder) ->
                    mappedRange.assertThat {
                        isEqualTo(40L..48L)
                    }

                    remainder.assertIterable {
                        isEmpty()
                    }
                }
        }

        @Test
        fun `mapAndSplit before start of range`() {
            IfYouGiveASeedAFertilizer
                .CategoryMapping(
                    sourceRange = 10L..20L,
                    destinationRange = 40L..50L,
                )
                .mapAndSplit(5L..18L)
                .also { (mappedRange, remainder) ->
                    mappedRange.assertThat {
                        isEqualTo(40L..48L)
                    }

                    remainder.assertIterable {
                        containsExactly(
                            5L..9L
                        )
                    }
                }
        }

        @Test
        fun `mapAndSplit end of range`() {
            IfYouGiveASeedAFertilizer
                .CategoryMapping(
                    sourceRange = 10L..20L,
                    destinationRange = 40L..50L,
                )
                .mapAndSplit(17L..20L)
                .also { (mappedRange, remainder) ->
                    mappedRange.assertThat {
                        isEqualTo(47L..50L)
                    }

                    remainder.assertIterable {
                        isEmpty()
                    }
                }
        }

        @Test
        fun `mapAndSplit after end of range`() {
            IfYouGiveASeedAFertilizer
                .CategoryMapping(
                    sourceRange = 10L..20L,
                    destinationRange = 40L..50L,
                )
                .mapAndSplit(17L..33L)
                .also { (mappedRange, remainder) ->
                    mappedRange.assertThat {
                        isEqualTo(47L..50L)
                    }

                    remainder.assertIterable {
                        containsExactly(
                            21L..33L
                        )
                    }
                }
        }

        @Test
        fun `mapAndSplit middle of range`() {
            IfYouGiveASeedAFertilizer
                .CategoryMapping(
                    sourceRange = 10L..20L,
                    destinationRange = 40L..50L,
                )
                .mapAndSplit(14L..16L)
                .also { (mappedRange, remainder) ->
                    mappedRange.assertThat {
                        isEqualTo(44L..46L)
                    }

                    remainder.assertIterable {
                        isEmpty()
                    }
                }
        }

        @Test
        fun `mapAndSplit around range`() {
            IfYouGiveASeedAFertilizer
                .CategoryMapping(
                    sourceRange = 10L..20L,
                    destinationRange = 40L..50L,
                )
                .mapAndSplit(5L..21L)
                .also { (mappedRange, remainder) ->
                    mappedRange.assertThat {
                        isEqualTo(40L..50L)
                    }

                    remainder.assertIterable {
                        containsExactly(
                            5L..9L,
                            21L..21L,
                        )
                    }
                }
        }

        @Test
        fun `mapAndSplit outside of range before`() {
            IfYouGiveASeedAFertilizer
                .CategoryMapping(
                    sourceRange = 10L..20L,
                    destinationRange = 40L..50L,
                )
                .mapAndSplit(1L..6L)
                .also { (mappedRange, remainder) ->
                    mappedRange.isEmpty().assertBoolean {
                        isTrue()
                    }

                    remainder.assertIterable {
                        containsExactly(1L..6L)
                    }
                }
        }

        @Test
        fun `mapAndSplit outside of range after`() {
            IfYouGiveASeedAFertilizer
                .CategoryMapping(
                    sourceRange = 10L..20L,
                    destinationRange = 40L..50L,
                )
                .mapAndSplit(35L..40L)
                .also { (mappedRange, remainder) ->
                    mappedRange.isEmpty().assertBoolean {
                        isTrue()
                    }

                    remainder.assertIterable {
                        containsExactly(35L..40L)
                    }
                }
        }
    }
}
