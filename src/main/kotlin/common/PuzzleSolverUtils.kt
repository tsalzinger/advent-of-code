package me.salzinger.common

import java.io.File

enum class FileType(val extension: String) {
    IN("in"),
    OUT("out"),
    SOLUTION("solution"),
}

fun getFile(level: Int, part: Int?, fileType: FileType): File =
    File("src/main/resources/puzzle-$level${part?.run { "-$this" } ?: ""}.${fileType.extension}")

fun getFile(level: Int, part: String, fileType: FileType): File =
    File("src/main/resources/puzzle-$level-$part.${fileType.extension}")

fun getNextInput(puzzle: Int, part: Int? = null): List<String> =
    getFile(puzzle, part, FileType.IN)
        .readLines()
        .dropLastWhile { it.isBlank() }

fun getNextInput(puzzle: Int, part: String): List<String> =
    getFile(puzzle, part, FileType.IN)
        .readLines()
        .dropLastWhile { it.isBlank() }

fun Int.solveExample(expectedSolution: String, solver: List<String>.() -> String) {
    getNextInput(this, 0)
        .solver()
        .also { solution ->
            check(solution == expectedSolution) {
                "Expected $expectedSolution but got $solution"
            }
        }
}

fun Int.solveExample(exampleNumber: Int, expectedSolution: String, solver: List<String>.() -> String) {
    getNextInput(this, "example-$exampleNumber")
        .solver()
        .also { solution ->
            check(solution == expectedSolution) {
                "Expected $expectedSolution but got $solution"
            }
        }
}

fun Int.solveExamples(
    expectedSolutions: List<String>,
    solver: List<String>.() -> String,
) {
    expectedSolutions.forEachIndexed { exampleIndex, expectedSolution ->
        val exampleNumber = exampleIndex + 1
        println("Solving example $exampleNumber")
        solveExample(
            exampleNumber = exampleNumber,
            expectedSolution = expectedSolution,
            solver = solver
        )
    }
}

fun Int.solve(part: Int, solver: List<String>.() -> String) {
    getNextInput(this, part)
        .solver()
        .writePuzzleSolution(this, part)
}

fun Int.solve(solver: List<String>.() -> String) {
    getNextInput(this)
        .solver()
        .writePuzzleSolution(this)
}

fun <T> List<T>.writePuzzleSolution(level: Int, part: Int?) {
    getFile(level, part, FileType.OUT).writeText(joinToString("\n"))
}

fun String.writePuzzleSolution(level: Int, part: Int? = null) {
    listOf(this).writePuzzleSolution(level, part)
}

