/*
 * You note down the patterns of ash (.) and rocks (#) that you see as you walk (your puzzle input); perhaps by
 * carefully analyzing these patterns, you can figure out where the mirrors are!
 *
 * For example:
 *
 *     #.##..##.
 *     ..#.##.#.
 *     ##......#
 *     ##......#
 *     ..#.##.#.
 *     ..##..##.
 *     #.#.##.#.
 *
 *     #...##..#
 *     #....#..#
 *     ..##..###
 *     #####.##.
 *     #####.##.
 *     ..##..###
 *     #....#..#
 *
 * To find the reflection in each pattern, you need to find a perfect reflection across either a horizontal line between
 * two rows or across a vertical line between two columns.
 *
 * In the first pattern, the reflection is across a vertical line between two columns; arrows on each of the two columns
 * point at the line between the columns:
 *
 *     123456789
 *         ><
 *     #.##..##.
 *     ..#.##.#.
 *     ##......#
 *     ##......#
 *     ..#.##.#.
 *     ..##..##.
 *     #.#.##.#.
 *         ><
 *     123456789
 *
 * In this pattern, the line of reflection is the vertical line between columns 5 and 6. Because the vertical line is
 * not perfectly in the middle of the pattern, part of the pattern (column 1) has nowhere to reflect onto and can be
 * ignored; every other column has a reflected column within the pattern and must match exactly: column 2 matches column
 * 9, column 3 matches 8, 4 matches 7, and 5 matches 6.
 *
 * The second pattern reflects across a horizontal line instead:
 *
 *     1 #...##..# 1
 *     2 #....#..# 2
 *     3 ..##..### 3
 *     4v#####.##.v4
 *     5^#####.##.^5
 *     6 ..##..### 6
 *     7 #....#..# 7
 *
 * This pattern reflects across the horizontal line between rows 4 and 5. Row 1 would reflect with a hypothetical row 8,
 * but since that's not in the pattern, row 1 doesn't need to match anything. The remaining rows match: row 2 matches
 * row 7, row 3 matches row 6, and row 4 matches row 5.
 *
 * To summarize your pattern notes, add up the number of columns to the left of each vertical line of reflection; to
 * that, also add 100 multiplied by the number of rows above each horizontal line of reflection. In the above example,
 * the first pattern's vertical line has 5 columns to its left and the second pattern's horizontal line has 4 rows above
 * it, a total of 405.
 *
 * Find the line of reflection in each of the patterns in your notes. What number do you get after summarizing all of
 * your notes?
 *
 * --- Part Two ---
 *
 * You resume walking through the valley of mirrors and - SMACK! - run directly into one. Hopefully nobody was watching,
 * because that must have been pretty embarrassing.
 *
 * Upon closer inspection, you discover that every mirror has exactly one smudge: exactly one . or # should be the
 * opposite type.
 *
 * In each pattern, you'll need to locate and fix the smudge that causes a different reflection line to be valid. (The
 * old reflection line won't necessarily continue being valid after the smudge is fixed.)
 *
 * Here's the above example again:
 *
 *     #.##..##.
 *     ..#.##.#.
 *     ##......#
 *     ##......#
 *     ..#.##.#.
 *     ..##..##.
 *     #.#.##.#.
 *
 *     #...##..#
 *     #....#..#
 *     ..##..###
 *     #####.##.
 *     #####.##.
 *     ..##..###
 *     #....#..#
 *
 * The first pattern's smudge is in the top-left corner. If the top-left # were instead ., it would have a different,
 * horizontal line of reflection:
 *
 *     1 ..##..##. 1
 *     2 ..#.##.#. 2
 *     3v##......#v3
 *     4^##......#^4
 *     5 ..#.##.#. 5
 *     6 ..##..##. 6
 *     7 #.#.##.#. 7
 *
 * With the smudge in the top-left corner repaired, a new horizontal line of reflection between rows 3 and 4 now exists.
 * Row 7 has no corresponding reflected row and can be ignored, but every other row matches exactly: row 1 matches row
 * 6, row 2 matches row 5, and row 3 matches row 4.
 *
 * In the second pattern, the smudge can be fixed by changing the fifth symbol on row 2 from . to #:
 *
 *     1v#...##..#v1
 *     2^#...##..#^2
 *     3 ..##..### 3
 *     4 #####.##. 4
 *     5 #####.##. 5
 *     6 ..##..### 6
 *     7 #....#..# 7
 *
 * Now, the pattern has a different horizontal line of reflection between rows 1 and 2.
 *
 * Summarize your notes as before, but instead use the new different reflection lines. In this example, the first
 * pattern's new horizontal line has 3 rows above it and the second pattern's new horizontal line has 1 row above it,
 * summarizing to the value 400.
 *
 * In each pattern, fix the smudge and find the different line of reflection. What number do you get after summarizing
 * the new reflection line in each pattern in your notes?
 */
package io.ysakhno.adventofcode2023.day13

import io.ysakhno.adventofcode2023.util.ProblemInput
import io.ysakhno.adventofcode2023.util.println
import io.ysakhno.adventofcode2023.util.transpose
import kotlin.math.max

private val problemInput = object : ProblemInput {}

private fun gridsFrom(input: List<String>) = input.fold(mutableListOf(mutableListOf<List<Char>>())) { acc, line ->
    if (line != ">") acc.last().add(line.toList())
    else acc.add(mutableListOf())
    acc
}

private fun List<Char>.countDifferences(other: List<Char>) = zip(other).count { (ch1, ch2) -> ch1 != ch2 }

private fun List<List<Char>>.isReflectedAbout(idx: Int, numDifferences: Int) = (idx..<size)
    .map { 2 * idx - it - 1 to it }
    .filterNot { (upper) -> upper < 0 }
    .sumOf { (upper, lower) -> this[upper].countDifferences(this[lower]) } == numDifferences

private fun findReflectionIn(grid: List<List<Char>>, numDifferences: Int) =
    grid.withIndex().drop(1).firstOrNull { (idx) -> grid.isReflectedAbout(idx, numDifferences) }?.index ?: -1

private fun calculateReflectionPoints(grid: List<List<Char>>, numDifferences: Int = 0) =
    max(findReflectionIn(grid, numDifferences) * 100, findReflectionIn(grid.transpose(), numDifferences))

private fun part1(input: List<String>) = gridsFrom(input).sumOf { grid -> calculateReflectionPoints(grid) }

private fun part2(input: List<String>)= gridsFrom(input).sumOf { grid -> calculateReflectionPoints(grid, 1) }

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    check(part1(testInput) == 405)
    check(part2(testInput) == 400)

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
