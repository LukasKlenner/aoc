package aoc.year25

import aoc.util.JoinedGridTask
import aoc.util.Pos

class Day04 : JoinedGridTask<Boolean>(
    { i -> i == '@' },
    { i -> Array(i) { false } },
    { i -> Array(i) { Array(0) { false } } }) {

    override fun run(part1: Boolean): Long {

        val accessible = mutableSetOf<Pos>()
        val workingList = ArrayDeque<Pos>()

        fun checkPos(pos: Pos) {
            if (getValue(pos)) {
                getSurroundingOf(pos).count { getValue(it) }.takeIf { it < 4 }?.let {
                    accessible.add(pos)
                    workingList.addAll(getSurroundingOf(pos))
                    if (!part1) putValue(pos, false)
                }
            }
        }

        foreachCell { pos, isRole -> checkPos(pos) }

        if (part1) {
            return accessible.size.toLong()
        }

        while (!workingList.isEmpty()) {
            val pos = workingList.removeFirst()
            checkPos(pos)
        }

        return accessible.size.toLong()
    }
}

