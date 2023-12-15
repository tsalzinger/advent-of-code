package me.salzinger.puzzles.puzzle15

object LensLibrary {

    fun String.getHash(): Int {
        return fold(0) { currentHash, currentCharacter ->
            ((currentHash + currentCharacter.code) * 17) % 256
        }
    }

    fun Sequence<String>.getSumOfHashes(): Long {
        return single()
            .split(",")
            .map {
                it.getHash()
            }.sumOf {
                it.toLong()
            }
    }

    class Box(
        val boxNumber: Int,
        staringLenses: List<Lens> = emptyList(),
    ) {
        private val lenses = staringLenses.toMutableList()

        fun insertOrReplaceLens(lens: Lens) {
            if (lenses.any { it.label == lens.label }) {
                lenses.replaceAll {
                    if (it.label == lens.label) {
                        lens
                    } else {
                        it
                    }
                }
            } else {
                lenses += lens
            }

        }

        fun removeLensWithLabel(lensLabel: String) {
            lenses.removeIf {
                it.label == lensLabel
            }
        }

        fun getFocusPower(): Int {
            return lenses
                .mapIndexed { index, lens ->
                    (boxNumber + 1) * (index + 1) * lens.focalLength
                }
                .sum()
        }

        override fun toString(): String {
            return "Box $boxNumber: ${lenses.joinToString(" ") { "[${it.label} ${it.focalLength}]" }}"
        }
    }

    sealed interface Operation {
        val label: String

        data class InsertOrReplace(
            override val label: String,
            val focalLength: Int,
        ) : Operation

        data class Remove(
            override val label: String,
        ) : Operation
    }

    fun Sequence<String>.getSumOfFocusPowerPerLens(): Long {
        return single()
            .split(",")
            .map { operation ->
                val parts = operation.trimEnd { it == '-' }.split("=")

                when (parts.count()) {
                    1 -> Operation.Remove(parts.first())
                    2 -> Operation.InsertOrReplace(parts.first(), parts[1].toInt())
                    else -> throw RuntimeException("Failed to get operation from $operation")
                }
            }
            .fold(mutableMapOf<Int, Box>()) { boxes, operation ->
                val boxNumber = operation.label.getHash()

                boxes.compute(boxNumber) { _, boxOrNull ->
                    val box = boxOrNull ?: Box(boxNumber)

                    when (operation) {
                        is Operation.InsertOrReplace -> box.insertOrReplaceLens(
                            Lens(
                                operation.label,
                                operation.focalLength
                            )
                        )

                        is Operation.Remove -> box.removeLensWithLabel(operation.label)
                    }

                    box
                }

                boxes
            }
            .values
            .sumOf {
                it.getFocusPower().toLong()
            }
    }

    data class Lens(
        val label: String,
        val focalLength: Int,
    )
}
