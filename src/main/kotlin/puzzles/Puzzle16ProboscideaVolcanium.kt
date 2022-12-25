package me.salzinger.puzzles

import me.salzinger.common.streamInput

class Graph<N, E>(val edges: Set<Edge<N, E>>) {
    val nodes: Set<Node<N>> = edges.flatMap { setOf(it.node1, it.node2) }.toSet()

    val nodeEdges: Map<Node<N>, List<Edge<N, E>>> = edges.flatMap { edge ->
        when (edge) {
            is Edge.UndirectedEdge -> setOf(edge, edge.flip())
        }
    }.groupBy { edge -> edge.node1 }

    val adjacentNodes: Map<Node<N>, List<Node<N>>> = nodeEdges.mapValues { (_, edges) ->
        edges.map { it.node2 }
    }
}

data class Node<T>(val value: T)

sealed interface Edge<N, E> {
    val node1: Node<N>
    val node2: Node<N>
    val value: E

    fun flip(): Edge<N, E>

    data class UndirectedEdge<N, E>(
        override val node1: Node<N>,
        override val node2: Node<N>,
        override val value: E,
    ) : Edge<N, E> {
        override fun flip(): UndirectedEdge<N, E> = copy(node1 = node2, node2 = node1, value = value)
    }
}

object Puzzle16ProboscideaVolcanium {

    data class ValveData(
        val name: String,
        val flowRate: Int,
    )

    data class EdgeCost(
        val traversalCost: Int,
    )

    object Part1 {

        fun Sequence<Pair<Node<ValveData>, List<String>>>.toGraph(): Graph<ValveData, EdgeCost> {
            val edgeCost = EdgeCost(traversalCost = 1)

            return toList().run {
                val nodes = associate { it.first.value.name to it.first }

                flatMap { pair ->
                    pair.second.map {
                        Edge.UndirectedEdge(
                            nodes.getValue(pair.first.value.name), nodes.getValue(it), edgeCost
                        )
                    }
                }.toSet().let(::Graph)
            }
        }

        fun String.parseNodeAndEdges(): Pair<Node<ValveData>, List<String>> {
            // Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
            val (valveInput, tunnelInput) = split("; ")
            val (valveName, flowRate) = valveInput.removePrefix("Valve ").split(" has flow rate=")
            val reachableValveNames = tunnelInput
                .removePrefix("tunnels lead to valves ")
                .removePrefix("tunnel leads to valve ")
                .split(", ")

            return Node(ValveData(valveName, flowRate.toInt())) to reachableValveNames
        }


        fun Sequence<String>.solve(): Int {
            map { it.parseNodeAndEdges() }
                .toGraph()
                .let { graph ->
                    val startingNode = graph.nodes.single { it.value.name == "AA" }

                    // weightFunction: (edgeCosts = List<EdgeCost>, timeLeft: Int) -> { (timeLeft - edgeCosts.sumOf { it.traversalCost }) * flowRate }

                    println(graph.adjacentNodes.getValue(startingNode))
                }
            TODO()
        }

        @JvmStatic
        fun main(args: Array<String>) {
            "puzzle-16.in".streamInput().solve().let(::println)
        }
    }
}
