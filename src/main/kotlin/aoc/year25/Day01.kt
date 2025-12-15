package aoc.year25

import aoc.KtJoinedDay

class Day01 : KtJoinedDay {

    override fun run(input: List<String>, part1: Boolean): Long {

        var current = 50
        var result = 0L

        input.forEach { line ->
            val sign = if (line[0] == 'L') -1 else 1
            var value = line.substring(1).toInt()

            if (part1) {
                current = (current + sign * value) % 100
                if (current < 0) current += 100
                if (current == 0) result++
            } else {
                val previous = current

                if (value >= 100) {
                    val fullCycles = value / 100
                    result += fullCycles
                    value = value % 100
                }

                current = current + sign * value

                if (current < 0) {
                    current += 100
                    if (previous != 0)  result++
                } else if (current == 0) result++
                else if (current >= 100) {
                    current -= 100
                    if (previous != 0) result++
                }
            }
        }

        return result
    }
}
