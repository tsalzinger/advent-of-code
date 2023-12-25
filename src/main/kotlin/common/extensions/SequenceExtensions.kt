package me.salzinger.common.extensions

fun <T> Sequence<T>.permutate(): Sequence<Pair<T, T>> {
    val source = toList()
    return source.asSequence().flatMapIndexed { index, element ->
        source.drop(index + 1).map {
            element to it
        }
    }
}
