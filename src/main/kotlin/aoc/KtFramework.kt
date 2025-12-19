package aoc

import java.io.File
import java.util.*

const val DAY = 5
const val YEAR = 2026

const val useCurrentDay = false
const val useCurrentYear = true

fun main() {
    val currentDay = (if (useCurrentDay) getCurrentDay() else DAY).let { if (it < 10) "0$it" else it.toString() }
    val currentYear = (if (useCurrentYear) getCurrentYear() else YEAR).toString().substring(2, 4)

    val dayClass = Class.forName("aoc.year$currentYear.Day$currentDay")
    val day = dayClass.getConstructors()[0].newInstance()

    println("Running year $currentYear day $currentDay")

    var startTime = System.nanoTime()

    val input = File("src/main/resources/aoc/year25/day${currentDay}_input.txt").readLines()

    val day1Result = if (day is KtDay) day.part1(input) else (day as Day).part1(input.stream())
    println("Part1: $day1Result")
    System.out.printf("Time: %.2fms\n", (System.nanoTime() - startTime) / 1e6)
    println()

    startTime = System.nanoTime()
    val day2Result = if (day is KtDay) day.part2(input) else (day as Day).part2(input.stream())
    println("Part2: $day2Result")
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