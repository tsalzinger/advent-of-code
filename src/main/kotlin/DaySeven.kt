package me.salzinger

fun amplifierChain(phaseSettings: List<Int>, memory: Memory): Int {
    var output = 0

    for (phaseSetting in phaseSettings) {
        output = IntcodeProgramInterpreter(
            memory,
            inputs = listOf(phaseSetting, output)
        ).evaluate().output.first()
    }

    return output
}

fun Memory.getMaxOutput(phases: Int): Int {
    return (0 until phases).toList().permutate()
        .map {
            amplifierChain(it, this)
        }.max()!!
}

fun main() {
    13.solve {
        convertIntcodeInput()
            .first()
            .getMaxOutput(5)
            .toString()
    }
}

fun <T> List<T>.permutate(): List<List<T>> {
    if (size == 1) {
        return listOf(listOf(first()))
    }

    return flatMap { element ->
        toMutableList()
            .apply { remove(element) }
            .permutate()
            .map {
                listOf(element) + it
            }
    }
}


//01234
//01243
//01324
//01342
//01423
//01432