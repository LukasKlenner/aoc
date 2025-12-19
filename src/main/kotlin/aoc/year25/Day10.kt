package aoc.year25

import aoc.KtDay
import aoc.util.Pos
import aoc.util.graph.Dijkstra
import aoc.util.graph.Graph
import aoc.util.graph.Node

class Day10 : KtDay {

    override fun part1(input: List<String>): Long {

        var result = 0L
        input.forEach {

            val targetState = State(it
                .substringBefore("]")
                .substringAfter("[")
                .map { s -> s == '#' }
                .toBooleanArray())

            val transitions: List<State> = it
                .substringAfter("] ")
                .substringBefore(" {")
                .split(" ")
                .map { t ->
                    val res = BooleanArray(targetState.bits.size) { false }
                    t.substringAfter("(")
                        .substringBefore(")")
                        .split(",")
                        .map { s -> s.toInt() }
                        .forEach { i -> res[i] = true }
                    State(res)
                }

            val graph = Graph<State>()

            val states = mutableMapOf<State, Node<State>>()
            val newStates = ArrayDeque<State>()
            val init = State(BooleanArray(targetState.bits.size) { false })
            newStates.add(init)
            states.put(init, graph.addNode(init, Pos(0, 0)))

            while (newStates.isNotEmpty()) {
                val currentNode = newStates.removeFirst()

                transitions.forEach { transition ->
                    val newState = State(BooleanArray(targetState.bits.size) { i -> currentNode.bits[i] xor transition.bits[i] })
                    if (!states.containsKey(newState)) {
                        val newNode = graph.addNode(newState, Pos(0, 0))
                        states.put(newState, newNode)
                        newStates.add(newState)
                    }
                    graph.addEdge(states[currentNode]!!, states[newState]!!)
                }
            }

            val dijkstra = Dijkstra(
                states.keys,
                { n -> states[n] },
                { n1, n2 -> 1 },
                {s, n -> n.value}
            )

            dijkstra.run(init)
            result += dijkstra.reconstructPath(init, targetState).size - 1

        }

        return result
    }

    override fun part2(input: List<String>): Long {
        var result = 0L
        input.forEach {

            val targetState = IntState(it
                .substringBefore("}")
                .substringAfter("{")
                .split(",")
                .map { s -> s.toInt() }
                .toIntArray())

            val transitions: List<State> = it
                .substringAfter("] ")
                .substringBefore(" {")
                .split(" ")
                .map { t ->
                    val res = BooleanArray(targetState.voltage.size) { false }
                    t.substringAfter("(")
                        .substringBefore(")")
                        .split(",")
                        .map { s -> s.toInt() }
                        .forEach { i -> res[i] = true }
                    State(res)
                }

            val graph = Graph<IntState>()

            val states = mutableMapOf<IntState, Node<IntState>>()
            val newStates = ArrayDeque<IntState>()
            val init = IntState(IntArray(targetState.voltage.size) { 0 })
            newStates.add(init)
            states.put(init, graph.addNode(init, Pos(0, 0)))

            while (newStates.isNotEmpty()) {
                val currentNode = newStates.removeFirst()

                transitions.forEach { transition ->
                    val newState = IntState(IntArray(targetState.voltage.size) { i -> currentNode.voltage[i] + if (transition.bits[i]) 1 else 0 })

                    if (newState.voltage.any { v -> v > targetState.voltage[ newState.voltage.indexOf(v)] }) {
                        // Invalid state, skip
                        return@forEach
                    }

                    if (!states.containsKey(newState)) {
                        val newNode = graph.addNode(newState, Pos(0, 0))
                        states.put(newState, newNode)
                        newStates.add(newState)
                    }
                    graph.addEdge(states[currentNode]!!, states[newState]!!)
                }
            }

            val dijkstra = Dijkstra(
                states.keys,
                { n -> states[n] },
                { n1, n2 -> 1 },
                {s, n -> n.value}
            )

            dijkstra.run(init)
            result += dijkstra.reconstructPath(init, targetState).size - 1
            println("done")
        }

        return result
    }

    private class State(val bits: BooleanArray) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is State) return false
            return bits.contentEquals(other.bits)
        }

        override fun hashCode(): Int {
            return bits.contentHashCode()
        }

        override fun toString(): String {
            return bits.joinToString("") { if (it) "#" else "." }
        }
    }

    private class IntState(val voltage: IntArray) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is IntState) return false
            return voltage.contentEquals(other.voltage)
        }

        override fun hashCode(): Int {
            return voltage.contentHashCode()
        }

        override fun toString(): String {
            return voltage.joinToString(",")
        }
    }
}
