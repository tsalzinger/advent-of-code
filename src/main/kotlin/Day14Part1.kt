package me.salzinger

private fun List<String>.solve(): String {
    val polymerTemplate = first()

    val pairInsertionRules: Map<String, String> = drop(1)
        .flatMap {
            it.split(" -> ").toPairs()
        }
        .toMap()
        .mapValues { (key, value) -> "${key[0]}$value${key[1]}" }

    var polymer = polymerTemplate

    repeat(10) {
        polymer = polymer
            .inPairs()
            .map { pairInsertionRules.getValue(it) }
            .reduce { acc, s -> "$acc${s.substring(1)}" }
    }

    val elementFrequency = polymer.toList()
        .groupBy { it }
        .mapValues { it.value.count() }

    return elementFrequency.values.toSet().run {
        maxOf { it } - minOf { it }
    }.toString()
}

fun main() {
    14.solveExample(1, "1588", List<String>::solve)

    14.solve(1, List<String>::solve)
}

private fun String.inPairs(): List<String> {
    return (0..(count() - 2)).map {
        substring(it, it + 2)
    }
}
