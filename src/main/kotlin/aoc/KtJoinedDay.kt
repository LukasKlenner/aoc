package aoc

interface KtJoinedDay : KtDay {
    fun run(input: List<String>, part1: Boolean): Long

    override fun part1(input: List<String>): Long {
        return run(input, true)
    }

    override fun part2(input: List<String>): Long {
        return run(input, false)
    }
}