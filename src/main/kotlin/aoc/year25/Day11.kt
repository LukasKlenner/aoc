package aoc.year25

import aoc.KtDay
import aoc.util.graph.Graph
import aoc.util.graph.Node

class Day11 : KtDay {
    override fun part1(lines: List<String>): Long {
        val graph = Graph<String>()

        lines.forEach { line ->
            val start = line.substringBefore(":")
            line.substringAfter(": ").split(" ").forEach {
                graph.addEdgeAndNodes(start, it)
            }
        }

        return getPathsTo(graph, "you", "out")
    }

    private fun getPathsTo(graph: Graph<String>, from: String, to: String): Long {
        val pathsTo = mutableMapOf<Node<String>, Long>()
        pathsTo[graph.getNode(to)] = 1L

        graph.dfs(
            graph.getNode(from),
            { node -> true },
            { node ->
                pathsTo[node] =
                    if (node.value != to)
                        node.outgoingEdges.sumOf { edge -> pathsTo[edge.to]!! }
                    else 1
            }
        )

        return pathsTo[graph.getNode(from)] ?: 0L
    }

    override fun part2(lines: List<String>): Long {
        val graph = Graph<String>()

        lines.forEach { line ->
            val start = line.substringBefore(":")
            line.substringAfter(": ").split(" ").forEach {
                graph.addEdgeAndNodes(start, it)
            }
        }

        val first = getPathsTo(graph, "svr", "fft") * getPathsTo(graph, "fft", "dac") * getPathsTo(graph, "dac", "out")
        val second = getPathsTo(graph, "svr", "dac") * getPathsTo(graph, "dac", "fft") * getPathsTo(graph, "fft", "out")

        return first + second
    }


}
