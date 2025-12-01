package aoc

interface KtJoinedDay : KtDay {
    fun run(input: List<String>, part1: Boolean): Long

    override fun part1(lines: List<String>): Long {
        return run(lines, true)
    }

    override fun part2(lines: List<String>): Long {
        return run(lines, false)
    }
}