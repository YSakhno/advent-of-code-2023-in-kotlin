/*
 * <PUZZLE-DESCRIPTION-HERE>
 */
package io.ysakhno.adventofcode2023.dayXX

import io.ysakhno.adventofcode2023.util.ProblemInput
import io.ysakhno.adventofcode2023.util.println

private val problemInput = object : ProblemInput {}

private fun part1(input: List<String>): Int {
    return input.size
}

private fun part2(input: List<String>): Int {
    return input.size
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    check(part1(testInput) == 0)
    check(part2(testInput) == 0)

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
