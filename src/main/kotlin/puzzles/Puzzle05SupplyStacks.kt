package puzzles

import me.salzinger.common.FileType
import me.salzinger.common.getFile
import me.salzinger.common.solve
import me.salzinger.common.solveExample
import puzzles.Puzzle04CampCleanup.Part1.toRangePair
import puzzles.Puzzle05SupplyStacks.Part1.getInitialStacksAndActions
import java.util.*

object Puzzle05SupplyStacks {

    data class Crate(
        val identifier: String,
    )

    data class MoveAction(
        val numberOfCrates: Int,
        val sourceStackIndex: Int,
        val targetStackIndex: Int,
    )

    private const val STACK_INPUT_CHARACTER_COUNT = 3

    operator fun List<String>.component6(): String = get(5)

    object Part1 {

        fun List<String>.getInitialStacksAndActions(): Pair<List<Stack<Crate>>, List<MoveAction>> {
            val initialStacksInput = takeWhile { it.isNotEmpty() }

            val numberOfStacks = initialStacksInput
                .last()
                .split(" ")
                .count { it.isNotEmpty() }

            val stacks = List<Stack<Crate>>(numberOfStacks) {
                Stack()
            }

            initialStacksInput
                .reversed()
                .drop(1)
                .forEach { cratesAtLevel ->
                    cratesAtLevel
                        .chunked(STACK_INPUT_CHARACTER_COUNT + 1)
                        .map {
                            if (it.isNotBlank()) {
                                Crate(it.trim().drop(1).dropLast(1))
                            } else {
                                null
                            }
                        }
                        .forEachIndexed { index, crate ->
                            if (crate != null) {
                                stacks[index].push(crate)
                            }
                        }
                }

            val movesInput = drop(initialStacksInput.count() + 1)

            val actions = movesInput.map {
                val (actionType, numberOfCrates, _, sourceStackNumber, _, targetStackNumber) = it.split(" ")

                MoveAction(
                    numberOfCrates = numberOfCrates.toInt(),
                    sourceStackIndex = sourceStackNumber.toInt() - 1,
                    targetStackIndex = targetStackNumber.toInt() - 1
                )
            }

            return stacks to actions
        }

        object Example1 {
            @JvmStatic
            fun main(args: Array<String>) {
                5.solveExample(
                    exampleNumber = 1, expectedSolution = getFile(
                        level = 5, part = "1-example-1", fileType = FileType.SOLUTION
                    ).readLines().first()
                ) {
                    getInitialStacksAndActions()
                        .let { (stacks, actions) ->
                            stacks.applyActions(actions)
                        }
                        .map { it.last() }
                        .joinToString("") { it.identifier }
                }
            }
        }

        private fun List<Stack<Crate>>.applyActions(
            actions: List<MoveAction>,
        ): List<Stack<Crate>> {
            actions.forEach {
                val sourceStack = this[it.sourceStackIndex]
                val targetStack = this[it.targetStackIndex]
                repeat(it.numberOfCrates) {
                    targetStack.push(sourceStack.pop())
                }
            }

            return this
        }

        @JvmStatic
        fun main(args: Array<String>) {
            5.solve(1) {
                getInitialStacksAndActions()
                    .let { (stacks, actions) ->
                        stacks.applyActions(actions)
                    }
                    .map { it.last() }
                    .joinToString("") { it.identifier }
            }
        }
    }

    object Part2 {

        fun IntRange.overlaps(range: IntRange): Boolean {
            return first <= range.last && last >= range.first
        }

        fun List<String>.countOverlappingRanges(): Int {
            return map {
                it.toRangePair()
            }.count { (firstRange, secondRange) ->
                firstRange.overlaps(secondRange)
            }
        }

        private fun List<Stack<Crate>>.applyActions(
            actions: List<MoveAction>,
        ): List<Stack<Crate>> {
            actions.forEach {
                val sourceStack = this[it.sourceStackIndex]
                val targetStack = this[it.targetStackIndex]

                targetStack.addAll(sourceStack.takeLast(it.numberOfCrates))
                repeat(it.numberOfCrates) {
                    sourceStack.pop()
                }
            }

            return this
        }

        object Example1 {
            @JvmStatic
            fun main(args: Array<String>) {
                5.solveExample(
                    exampleNumber = 1, expectedSolution = getFile(
                        level = 5, part = "2-example-1", fileType = FileType.SOLUTION
                    ).readLines().first()
                ) {
                    getInitialStacksAndActions()
                        .let { (stacks, actions) ->
                            stacks.applyActions(actions)
                        }
                        .map { it.last() }
                        .joinToString("") { it.identifier }
                }
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            5.solve(2) {
                getInitialStacksAndActions()
                    .let { (stacks, actions) ->
                        stacks.applyActions(actions)
                    }
                    .map { it.last() }
                    .joinToString("") { it.identifier }
            }
        }
    }
}
