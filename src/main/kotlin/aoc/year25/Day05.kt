package aoc.year25

import aoc.KtDay
import aoc.util.Range

class Day05 : KtDay {

    override fun part1(input: List<String>): Long {
        val range = input.subList(0, input.indexOf("")).fold(Range.empty<Long>()) { acc, line ->
            val parts = line.split("-").map { it.toLong() }
            acc.union(Range.inclusive(parts[0], parts[1]))
        }

        return input.subList(input.indexOf("") + 1, input.size).map { it.toLong() }.count { range.contains(it) }.toLong()
    }

    override fun part2(input: List<String>): Long {
        return input.subList(0, input.indexOf("")).fold(Range.empty<Long>()) { acc, line ->
            val parts = line.split("-").map { it.toLong() }
            acc.union(Range.inclusive(parts[0], parts[1]))
        }.sumOf { it.upperBound - it.lowerBound + 1 }
    }
}
