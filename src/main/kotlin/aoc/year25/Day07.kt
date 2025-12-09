package aoc.year25

import aoc.KtJoinedDay

class Day07 : KtJoinedDay {

    override fun run(input: List<String>, part1: Boolean): Long {

        var splitCount = 0L

        val startState = LongArray(input[0].length) { i -> if (input[0][i] == 'S') 1 else 0 }
        val lines = input.drop(1).map {line -> BooleanArray(line.length) { line[it] == '^' } }

        val finalState = lines.fold(startState) { state, line ->
            val newState = LongArray(state.size) { 0 }
            state.forEachIndexed { i, count ->
                    if (line[i]) {
                        newState[i - 1] += count
                        newState[i + 1] += count
                        if (count > 0) splitCount++
                    } else {
                        newState[i] += count
                    }
            }
            newState
        }

        return if (part1) splitCount else finalState.sum()
    }

}
