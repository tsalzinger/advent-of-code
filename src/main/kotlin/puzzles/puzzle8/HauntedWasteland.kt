package me.salzinger.puzzles.puzzle8

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

        leftRightInstructions
            .foldIndexed(leftRightNetwork["AAA"]) { index, currentNode, instruction ->
                val nextNode = leftRightNetwork[currentNode[instruction]]
                if (nextNode.name == "ZZZ") {
                    return index + 1
                }

                nextNode
            }

        throw RuntimeException("Reached block after endless iteration")
    }
}
