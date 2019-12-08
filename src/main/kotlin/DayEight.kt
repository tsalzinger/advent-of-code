package me.salzinger

typealias SpaceImageRow = List<Int>
typealias SpaceImageLayer = List<SpaceImageRow>

fun SpaceImageLayer.countOccurrences(value: Int) = this.flatten().countOccurrences(value)

inline class SpaceImage(val data: List<SpaceImageLayer>) {
    override fun toString(): String {
        return data.joinToString(",\n") {
            """
            |[
            |${it.joinToString(",\n") { row -> "\t$row" }}
            |]
            """.trimMargin()
        }
    }
}

fun List<Int>.decodeFromSpaceImageFormat(width: Int, height: Int): SpaceImage {
    return this.chunked(width * height) { layer ->
        layer.chunked(width)
    }.run { SpaceImage(this) }
}

fun String.decodeFromSpaceImageFormat(width: Int, height: Int): SpaceImage {
    return this.map { it.toString().toInt() }
        .decodeFromSpaceImageFormat(width, height)
}

fun main() {
    15.solve {
        this.first()
            .decodeFromSpaceImageFormat(25, 6)
            .data
            .sortedBy { it.countOccurrences(0) }
            .first()
            .run { countOccurrences(1) * countOccurrences(2) }
            .toString()
    }
}