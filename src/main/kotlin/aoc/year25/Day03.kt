package aoc.year25

import aoc.KtJoinedDay

class Day03 : KtJoinedDay {

    override fun run(input: List<String>, part1: Boolean): Long {
        val batteryCount = if (part1) 2 else 12
        var result = 0L

        input.forEach { line ->

            val voltage = StringBuilder()
            var startIndex = 0

            for (i in batteryCount downTo 1) {
                val max = line.substring(startIndex, line.length - i + 1).maxOf { it.digitToInt() }
                voltage.append(max)
                startIndex = line.indexOf(max.digitToChar(), startIndex) + 1
            }

            result += voltage.toString().toLong()
        }

        return result
    }
}
