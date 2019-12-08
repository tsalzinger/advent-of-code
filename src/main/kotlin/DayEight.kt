package me.salzinger

typealias SpaceImageRow = List<Int>
typealias SpaceImageLayer = List<SpaceImageRow>

fun SpaceImageLayer.countOccurrences(value: Int) = this.flatten().countOccurrences(value)

class SpaceImage(
    val data: List<SpaceImageLayer>,
    width: Int,
    height: Int
) {
    val image: String by lazy {
        val result = Array(height) {
            Array(width) {
                " "
            }
        }
        data.asReversed()
            .forEach { layer ->
                layer.forEachIndexed { rowIndex, rowData ->
                    result[rowIndex]

                    rowData.forEachIndexed { colIndex, cellData ->
                        result[rowIndex][colIndex] = when (cellData) {
                            0 -> "0"// "◼"
                            1 -> "1" // "◻"
                            else -> result[rowIndex][colIndex]
                        }
                    }
                }
            }

        result.joinToString("\n") { it.joinToString("") }
    }

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
    }.run { SpaceImage(this, width, height) }
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

    16.solve {
        this.first()
            .decodeFromSpaceImageFormat(25, 6)
            .image
    }
}