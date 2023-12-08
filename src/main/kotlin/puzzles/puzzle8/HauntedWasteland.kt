package me.salzinger.puzzles.puzzle8

import me.salzinger.common.math.leastCommonMultiple

object HauntedWasteland {

    object Instructions {
        const val LEFT = 'L'
        const val RIGHT = 'R'
    }

    data class LeftRightInstructions(
        val instructions: List<Char>,
    ) : Sequence<Char> {
        override fun iterator(): Iterator<Char> {
            return iterator {
                while (true) {
                    yieldAll(instructions)
                }
            }
        }
    }

    data class LeftRightNode(
        val name: String,
        val left: String,
        val right: String,
    ) {
        operator fun get(instruction: Char): String {
            return when (instruction) {
                Instructions.LEFT -> left
                Instructions.RIGHT -> right
                else -> throw RuntimeException("Unsupported instruction $instruction encountered")
            }
        }
    }

    val leftRightNodePattern = Regex("^(?<name>\\w+) = \\((?<left>\\w+), (?<right>\\w+)\\)$")

    fun String.toLeftRightNode(): LeftRightNode {
        val (name, left, right) = leftRightNodePattern.matchEntire(this)
            ?.destructured ?: throw RuntimeException("Failed to parse $this as LeftRightNode")

        return LeftRightNode(name, left, right)
    }

    fun String.toLeftRightInstructions(): LeftRightInstructions {
        return LeftRightInstructions(toList())
    }

    data class LeftRightNetwork(
        val nodeMap: Map<String, LeftRightNode>,
    ) {
        operator fun get(name: String): LeftRightNode {
            return nodeMap.getValue(name)
        }
    }

    fun Iterable<LeftRightNode>.toLeftRightNetwork(): LeftRightNetwork {
        return LeftRightNetwork(
            associateBy {
                it.name
            }
        )
    }

    fun Sequence<String>.getStepCountWithLeftRightInstructions(): Int {
        val list = toList()

        val leftRightInstructions = list.first().toLeftRightInstructions()

        val leftRightNetwork = list.drop(1)
            .filterNot { it.isEmpty() }
            .map {
                it.toLeftRightNode()
            }
            .toLeftRightNetwork()

        val startingNode = leftRightNetwork["AAA"]

        return startingNode
            .getStepCountToTargetNode(
                leftRightNetwork,
                leftRightInstructions
            ) {
                name == "ZZZ"
            }
    }

    fun LeftRightNode.getStepCountToTargetNode(
        network: LeftRightNetwork,
        instructions: LeftRightInstructions,
        isTarget: LeftRightNode.() -> Boolean,
    ): Int {
        instructions
            .foldIndexed(this) { index, currentNode, instruction ->
                val nextNode = network[currentNode[instruction]]
                if (nextNode.isTarget()) {
                    return index + 1
                }

                nextNode
            }

        var currentNode = this
        var stepCount = 0
        val instructionsIterator = instructions.iterator()

        while (!currentNode.isTarget()) {
            stepCount++

            currentNode = network[currentNode[instructionsIterator.next()]]
        }

        return stepCount
    }

    fun Sequence<String>.getParallelStepCountWithLeftRightInstructions(): Long {
        val list = toList()

        val leftRightInstructions = list.first().toLeftRightInstructions()

        val leftRightNetwork = list.drop(1)
            .filterNot { it.isEmpty() }
            .map {
                it.toLeftRightNode()
            }
            .toLeftRightNetwork()

        val startingNodes = leftRightNetwork.nodeMap.values.filter { it.name.endsWith('A') }

        val stepCounts = startingNodes
            .map {
                it.getStepCountToTargetNode(
                    leftRightNetwork,
                    leftRightInstructions,
                ) {
                    name.endsWith("Z")
                }
            }

        return stepCounts
            .map { it.toLong() }
            .leastCommonMultiple()
    }
}
