package aoc.year25

import aoc.KtJoinedDay

class Day07 : KtJoinedDay {

    override fun run(input: List<String>, part1: Boolean): Long {

        State.splitCount = 0L

        val startState = State(LongArray(input[0].length) { i -> if (input[0][i] == 'S') 1 else 0 })
        val lines = input.drop(1).map { Line.fromString(it) }

        val finalState = lines.fold(startState) { state, line -> state.apply(line) }

        return if (part1) State.splitCount else finalState.state.sum()
    }

    private class Line(val content: BooleanArray) {

        fun isSplitter(index: Int): Boolean {
            return content[index]
        }

        companion object {
            fun fromString(s: String): Line {
                val content = BooleanArray(s.length) { s[it] == '^' }
                return Line(content)
            }
        }

    }

    private class State(val state: LongArray) {

        fun apply(line: Line): State {
            val newState = State(LongArray(state.size) { 0 })

            for (i in state.indices) {
                if (isRay(i)) {
                    if (line.isSplitter(i)) {
                        newState.addRay(i - 1, state[i])
                        newState.addRay(i + 1, state[i])
                        splitCount++
                    } else {
                        newState.addRay(i, state[i])
                    }
                }
            }

            return newState
        }

        fun addRay(index: Int, count: Long) {
            state[index] += count
        }

        fun isRay(index: Int): Boolean {
            return state[index] > 0
        }

        companion object {
            var splitCount = 0L
        }
    }

}
