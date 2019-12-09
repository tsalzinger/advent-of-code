package me.salzinger

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class IncodeRelativeParameterTests {
    @Test
    fun `output self`() {

        val program = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99"
        val (executionState, executionContext) = IntcodeProgramInterpreter(
            program.convertIntcodeInput()
        ).evaluate()

        assertEquals(ExecutionState.COMPLETED, executionState)
        assertEquals(program, executionContext.output.getOutput().joinToString(","))
    }

    @Test
    fun `16 digit output`() {
        val program = "1102,34915192,34915192,7,4,7,99,0"
        val (executionState, executionContext) = IntcodeProgramInterpreter(
            program.convertIntcodeInput()
        ).evaluate()

        assertEquals(ExecutionState.COMPLETED, executionState)
        assertEquals(16, executionContext.output.getOutput().first().toString().length)
    }

    @Test
    fun `output middle part`() {
        val program = "104,1125899906842624,99"
        val (executionState, executionContext) = IntcodeProgramInterpreter(
            program.convertIntcodeInput()
        ).evaluate()

        assertEquals(ExecutionState.COMPLETED, executionState)
        assertEquals(1125899906842624.toBigInteger(), executionContext.output.getOutput().first())
    }
}