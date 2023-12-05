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
    }

    data class ConverterMap(
        private val name: String,
        private val categoryMappings: List<CategoryMapping>,
    ) {
        fun convert(value: Long): Long {
            return categoryMappings
                .firstNotNullOfOrNull {
                    it.mapOrNull(value)
                } ?: value
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
        return substringAfter("seeds: ")
            .toLongList(" ")
    }

    fun Sequence<String>.toAlmanac(): Almanac {
        val chunks = toList()
            .chunkedBy {
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
