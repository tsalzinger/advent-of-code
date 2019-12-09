package me.salzinger

import java.math.BigInteger


private fun String.convertIntcodeInterpreter(inputProvider: InputProvider) = IntcodeProgramInterpreter(
    this.convertIntcodeInput(),
    inputs = inputProvider
)


fun main() {
    17.solve {
        first()
            .convertIntcodeInterpreter(ListInputProvider(listOf(BigInteger.ONE)))
            .evaluate()
            .executionContext
            .output
            .getOutput()
            .joinToString("\n")
    }

    18.solve {
        first()
            .convertIntcodeInterpreter(ListInputProvider(listOf(2.toBigInteger())))
            .evaluate()
            .executionContext
            .output
            .getOutput()
            .joinToString("\n")
    }
}