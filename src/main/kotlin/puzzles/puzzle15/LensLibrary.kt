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
}
