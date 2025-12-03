package aoc.year25

import aoc.KtJoinedDay

class Day02 : KtJoinedDay {

    override fun run(input: List<String>, part1: Boolean): Long {
        var result = 0L
        input[0].split(",").forEach { range ->
            val start = range.split("-")[0].toLong()
            val end = range.split("-")[1].toLong()

            for (id in start..end) {

                val idStr = id.toString()

                if (part1) {
                    if (idStr.length % 2 != 0) continue
                    if (idStr.substring(0, idStr.length / 2) == idStr.substring(idStr.length / 2)) {
                        result += id
                    }
                } else {
                    for (div in 1 until idStr.length) {
                        if (idStr.length % div != 0) continue

                        val part = idStr.substring(0, div)
                        var valid = true
                        idStr.chunked(div).forEach { chunk ->
                            if (chunk != part) {
                                valid = false
                                return@forEach
                            }
                        }
                        if (valid) {
                            result += id
                            break
                        }
                    }
                }
            }
        }

        return result
    }
}
