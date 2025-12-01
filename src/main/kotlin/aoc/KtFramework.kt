package aoc

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

const val DAY = 1
const val YEAR = 2026

const val useCurrentDay = true
const val useCurrentYear = true

fun main() {
    val currentDay = (if (useCurrentDay) getCurrentDay() else DAY).let { if (it < 10) "0$it" else it.toString() }
    val currentYear = (if (useCurrentYear) getCurrentYear() else YEAR).toString().substring(2, 4)

    val dayClass = Class.forName("aoc.year$currentYear.Day$currentDay")
    val day = dayClass.getConstructors()[0].newInstance() as Day

    println("Running year $currentYear day $currentDay")

    var startTime = System.nanoTime()
    BufferedReader(InputStreamReader(Objects.requireNonNull(dayClass.getResourceAsStream("day${currentDay}_input.txt")))).use { reader ->
        println("Part1: " + day.part1(reader.lines()))
    }
    System.out.printf("Time: %.2fms\n", (System.nanoTime() - startTime) / 1e6)
    println()

    startTime = System.nanoTime()
    BufferedReader(InputStreamReader(Objects.requireNonNull(dayClass.getResourceAsStream("day${currentDay}_input.txt")))).use { reader ->
        println("Part2: " + day.part2(reader.lines()))
    }
    System.out.printf("Time: %.2fms\n", (System.nanoTime() - startTime) / 1e6)
}

private fun getCurrentDay(): Int {
    val cal = Calendar.getInstance()
    cal.setTime(Date())
    return cal.get(Calendar.DAY_OF_MONTH)
}

private fun getCurrentYear(): Int {
    val cal = Calendar.getInstance()
    cal.setTime(Date())
    return cal.get(Calendar.YEAR)
}