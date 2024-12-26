package me.salzinger.puzzles.puzzle22

object `Monkey Market` {
    fun Long.prune(): Long {
        // 16777216 == 2^24
        return this % 16777216
    }

    fun Long.getNextSecretNumber(): Long {
        // 64 == 2^6
        val first = ((this xor (this * 64))).prune()
        // 32 == 2^5
        val second = ((first xor (first / 32))).prune()
        // 2048 == 2^11
        val third = ((second xor (second * 2048))).prune()
        return third
    }

    fun Sequence<String>.sumOf2000thSecretNumber(): Long {
        return map { it.toLong() }
            .map {
                (0..<2000).fold(it) { acc, _ -> acc.getNextSecretNumber() }
            }
            .sum()
    }
}