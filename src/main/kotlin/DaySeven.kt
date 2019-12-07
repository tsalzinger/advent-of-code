package me.salzinger

fun amplifierChain(phaseSettings: List<Int>, memory: Memory): Int {
    var output = 0

    for (phaseSetting in phaseSettings) {
        output = IntcodeProgramInterpreter(
            memory,
            inputs = listOf(phaseSetting, output).asInputProvider()
        ).evaluate().executionContext.output.getOutput().first()
    }

    return output
}

fun feedbackLoop(phaseSettings: List<Int>, memory: Memory): Int {
    val inputProviders: List<InputProvider> = phaseSettings.mapIndexed { index, phaseSetting ->
        ListInputProvider(
            if (index == 0) {
                listOf(phaseSetting, 0)
            } else {
                listOf(phaseSetting)
            }
        )
    }
    val outputRecorders: List<OutputRecorder> = phaseSettings.mapIndexed { index, phaseSetting ->
        object : ListOutputRecorder() {
            override fun addValue(value: Int) {
                super.addValue(value)
                val nextAmplifierIndex = (index + 1) % phaseSettings.size
                val nextInputProvider = inputProviders[nextAmplifierIndex]
                nextInputProvider.addValue(value)
            }
        }
    }

    val amplifiers = phaseSettings.mapIndexed { index, phaseSetting ->
        IntcodeProgramInterpreter(
            memory,
            inputs = inputProviders[index],
            outputRecorder = outputRecorders[index]
        )
    }

    var executionStatus: ExecutionStatus? = null
    while (executionStatus?.executionState != ExecutionState.COMPLETED) {
        executionStatus = amplifiers.map {
            val evaluate = it.evaluate()
            evaluate
        }.last()
    }

    return executionStatus.executionContext.output.getOutput().last()
}

fun Memory.getMaxOutput(phases: Int): Int {
    return (0 until phases).toList().permutate()
        .map {
            amplifierChain(it, this)
        }.max()!!
}

fun Memory.getMaxOutputWithFeedbackLoop(): Int {
    return (5..9).toList().permutate()
        .map {
            feedbackLoop(it, this)
        }.max()!!
}

fun main() {
    13.solve {
        convertIntcodeInput()
            .first()
            .getMaxOutput(5)
            .toString()
    }

    14.solve {
        convertIntcodeInput()
            .first()
            .getMaxOutputWithFeedbackLoop()
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