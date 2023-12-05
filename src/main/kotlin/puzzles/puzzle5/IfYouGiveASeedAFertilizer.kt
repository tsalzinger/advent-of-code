package me.salzinger.puzzles.puzzle5

import me.salzinger.common.extensions.ChunkEvaluation
import me.salzinger.common.extensions.chunkedBy
import me.salzinger.common.extensions.toLongList

object IfYouGiveASeedAFertilizer {

    data class Almanac(
        val seeds: List<Long>,
        val maps: List<ConverterMap>,
    ) {
        fun getLastCategoryNumbers(): List<Long> {
            return seeds.map { seedNumber ->
                maps.fold(seedNumber) { categoryNumber, map ->
                    map.convert(categoryNumber)
                }
            }
        }

        val seedRanges by lazy {
            seeds.chunked(2) { (rangeStart, rangeLength) ->
                rangeStart..<(rangeStart + rangeLength)
            }
        }

        fun getLastCategoryRanges(): List<LongRange> {
            return maps.fold(seedRanges) { categoryRanges, map ->
                map.convert(categoryRanges)
            }
        }

        fun getMinNumberOfLastCategoryRanges(): Long {
            return getLastCategoryRanges()
                .minOf {
                    it.first
                }
        }
    }

    data class CategoryMapping(
        val sourceRange: LongRange,
        val destinationRange: LongRange,
    ) {
        fun canMap(value: Long): Boolean {
            return sourceRange.contains(value)
        }

        fun map(value: Long): Long {
            return destinationRange.first + value - sourceRange.first
        }

        fun mapOrNull(value: Long): Long? {
            return if (canMap(value)) {
                map(value)
            } else {
                null
            }
        }

        fun mapAndSplit(range: LongRange): Pair<LongRange, List<LongRange>> {
            if (range.last < sourceRange.first || range.first > sourceRange.last) {
                return LongRange.EMPTY to listOf(range)
            }

            val first = range.first.coerceAtLeast(sourceRange.first)
            val last = range.last.coerceAtMost(sourceRange.last)

            val mappedFirst = destinationRange.first + first - sourceRange.first
            val mappedLast = destinationRange.first + last - sourceRange.first

            return (mappedFirst..mappedLast) to listOf(
                range.first..<first,
                (last + 1)..range.last,
            ).filterNot(LongRange::isEmpty)
        }
    }

    data class ConverterMap(
        private val name: String,
        val categoryMappings: List<CategoryMapping>,
    ) {
        val sortedCategoryMappings = categoryMappings.sortedBy {
            it.sourceRange.first
        }

        fun convert(value: Long): Long {
            return categoryMappings.firstNotNullOfOrNull {
                it.mapOrNull(value)
            } ?: value
        }

        fun convert(range: LongRange): List<LongRange> {
            val notYetMapped = mutableListOf(range)
            val alreadyMapped = mutableListOf<LongRange>()

            sortedCategoryMappings.forEach { categoryMapping ->
                val currentUnmapped = notYetMapped.toList()
                notYetMapped.clear()
                currentUnmapped.forEach {
                    val (mapped, unmapped) = categoryMapping.mapAndSplit(it)

                    if (!mapped.isEmpty()) {
                        alreadyMapped.add(mapped)
                    }

                    notYetMapped.addAll(unmapped)
                }

            }

            return (alreadyMapped + notYetMapped).sortedBy { it.first }
        }

        fun convert(ranges: List<LongRange>): List<LongRange> {
            return ranges.flatMap { convert(it) }
        }
    }

    fun String.toCategoryMapping(): CategoryMapping {
        val (destinationRangeStart, sourceRangeStart, rangeLength) = toLongList(" ")

        return CategoryMapping(
            sourceRange = sourceRangeStart..<(sourceRangeStart + rangeLength),
            destinationRange = destinationRangeStart..<(destinationRangeStart + rangeLength),
        )
    }

    fun List<String>.toCategoryMappings(): List<CategoryMapping> {
        return map {
            it.toCategoryMapping()
        }
    }

    fun String.toSeedList(): List<Long> {
        return substringAfter("seeds: ").toLongList(" ")
    }

    fun Sequence<String>.toAlmanac(): Almanac {
        val chunks = toList().chunkedBy {
            when {
                it.isEmpty() -> ChunkEvaluation.END_CHUNK_AND_DISCARD
                else -> ChunkEvaluation.APPEND_TO_CHUNK
            }
        }

        val seedList = chunks.first().single().toSeedList()

        val converterMaps = chunks.drop(1).map {
            val name = it.first().substringBefore(" map:")
            val mappings = it.drop(1).toCategoryMappings()

            ConverterMap(
                name = name,
                categoryMappings = mappings,
            )
        }

        return Almanac(
            seedList,
            converterMaps,
        )
    }
}
