package me.salzinger.common.extensions

fun <T> List<T>.countOccurrences(value: T) = this.count { it == value }
fun <T> List<T>.toPairs(): List<Pair<T, T>> = chunked(2).map { Pair(it[0], it[1]) }


enum class ChunkEvaluation {
    APPEND_TO_CHUNK,
    APPEND_TO_NEW_CHUNK,
    END_CHUNK_AND_DISCARD
}

fun <T> List<T>.chunkedBy(chunkCondition: (T) -> ChunkEvaluation): List<List<T>> {
    return if (isEmpty()) {
        emptyList()
    } else {
        foldIndexed(mutableListOf(mutableListOf())) { index: Int, acc: MutableList<MutableList<T>>, item: T ->
            when (chunkCondition(item)) {
                ChunkEvaluation.APPEND_TO_CHUNK -> {
                    if (acc.isEmpty()) {
                        acc.add(mutableListOf(item))
                    } else {
                        acc.last().add(item)
                    }
                }

                ChunkEvaluation.APPEND_TO_NEW_CHUNK -> {
                    acc.add(mutableListOf(item))
                }

                ChunkEvaluation.END_CHUNK_AND_DISCARD -> {
                    if (index != lastIndex) {
                        acc.add(mutableListOf())
                    }
                }
            }

            acc
        }.map { it.toList() }
    }
}
