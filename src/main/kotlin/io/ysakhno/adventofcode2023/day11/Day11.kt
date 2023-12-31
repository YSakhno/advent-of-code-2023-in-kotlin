/*
 * --- Day 11: Cosmic Expansion ---
 *
 * The researcher has collected a bunch of data and compiled the data into a single giant image (your puzzle input). The
 * image includes empty space (.) and galaxies (#). For example:
 *
 *     ...#......
 *     .......#..
 *     #.........
 *     ..........
 *     ......#...
 *     .#........
 *     .........#
 *     ..........
 *     .......#..
 *     #...#.....
 *
 * The researcher is trying to figure out the sum of the lengths of the shortest path between every pair of galaxies.
 * However, there's a catch: the universe expanded in the time it took the light from those galaxies to reach the
 * observatory.
 *
 * Due to something involving gravitational effects, only some space expands. In fact, the result is that any rows or
 * columns that contain no galaxies should all actually be twice as big.
 *
 * In the above example, three columns and two rows contain no galaxies:
 *
 *        v  v  v
 *      ...#......
 *      .......#..
 *      #.........
 *     >..........<
 *      ......#...
 *      .#........
 *      .........#
 *     >..........<
 *      .......#..
 *      #...#.....
 *        ^  ^  ^
 *
 * These rows and columns need to be twice as big; the result of cosmic expansion therefore looks like this:
 *
 *     ....#........
 *     .........#...
 *     #............
 *     .............
 *     .............
 *     ........#....
 *     .#...........
 *     ............#
 *     .............
 *     .............
 *     .........#...
 *     #....#.......
 *
 * Equipped with this expanded universe, the shortest path between every pair of galaxies can be found. It can help to
 * assign every galaxy a unique number:
 *
 *     ....1........
 *     .........2...
 *     3............
 *     .............
 *     .............
 *     ........4....
 *     .5...........
 *     ............6
 *     .............
 *     .............
 *     .........7...
 *     8....9.......
 *
 * In these 9 galaxies, there are 36 pairs. Only count each pair once; order within the pair doesn't matter. For each
 * pair, find any shortest path between the two galaxies using only steps that move up, down, left, or right exactly one
 * '.' or # at a time. (The shortest path between two galaxies is allowed to pass through another galaxy.)
 *
 * For example, here is one of the shortest paths between galaxies 5 and 9:
 *
 *     ....1........
 *     .........2...
 *     3............
 *     .............
 *     .............
 *     ........4....
 *     .5...........
 *     .##.........6
 *     ..##.........
 *     ...##........
 *     ....##...7...
 *     8....9.......
 *
 * This path has length 9 because it takes a minimum of nine steps to get from galaxy 5 to galaxy 9 (the eight locations
 * marked # plus the step onto galaxy 9 itself). Here are some other example shortest path lengths:
 *
 * - Between galaxy 1 and galaxy 7: 15
 * - Between galaxy 3 and galaxy 6: 17
 * - Between galaxy 8 and galaxy 9: 5
 *
 * In this example, after expanding the universe, the sum of the shortest path between all 36 pairs of galaxies is 374.
 *
 * Expand the universe, then find the length of the shortest path between every pair of galaxies. What is the sum of
 * these lengths?
 *
 * --- Part Two ---
 *
 * The galaxies are much older (and thus much farther apart) than the researcher initially estimated.
 *
 * Now, instead of the expansion you did before, make each empty row or column one million times larger. That is, each
 * empty row should be replaced with 1000000 empty rows, and each empty column should be replaced with 1000000 empty
 * columns.
 *
 * (In the example above, if each empty row or column were merely 10 times larger, the sum of the shortest paths between
 * every pair of galaxies would be 1030. If each empty row or column were merely 100 times larger, the sum of the
 * shortest paths between every pair of galaxies would be 8410. However, your universe will need to expand far beyond
 * these values.)
 *
 * Starting with the same initial image, expand the universe according to these new rules, then find the length of the
 * shortest path between every pair of galaxies. What is the sum of these lengths?
 */
package io.ysakhno.adventofcode2023.day11

import io.ysakhno.adventofcode2023.util.ProblemInput
import io.ysakhno.adventofcode2023.util.println
import io.ysakhno.adventofcode2023.util.transpose
import kotlin.math.abs

private val problemInput = object : ProblemInput {}

private data class Galaxy(val x: Long, val y: Long)

private fun Galaxy.manhattanDistance(other: Galaxy) = abs(other.x - x) + abs(other.y - y)

private typealias Universe = List<List<Char>>

private fun List<String>.toUniverse(): Universe = map(String::toList)

private fun Universe.expand(expansionFactor: Int = 2): Universe {
    val preExpandedRow = List(expansionFactor) { List(size) { '.' } }
    return transpose()
        .flatMap { row -> if (row.all { it == '.' }) preExpandedRow else listOf(row) }
        .transpose()
        .flatMap { row -> if (row.all { it == '.' }) preExpandedRow else listOf(row) }
}

private fun Universe.solve(expansionFactor: Int = 2): Long {
    val galaxies = expand(expansionFactor)
        .flatMapIndexed { rowIdx, row ->
            row.mapIndexedNotNull { colIdx, ch -> if (ch == '#') Galaxy(colIdx.toLong(), rowIdx.toLong()) else null }
        }

    return galaxies.foldIndexed(mutableListOf<List<Long>>()) { idx, acc, galaxy1 ->
        acc.add(galaxies.drop(idx + 1).fold(listOf()) { acc2, galaxy2 ->
            acc2 + listOf(galaxy1.manhattanDistance(galaxy2))
        })
        acc
    }.flatten().sum()
}

private fun part1(input: List<String>) = input.toUniverse().solve()

private fun part2(input: List<String>) = input.toUniverse().solve(1_000_000)

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    check(part1(testInput) == 374L)
    check(testInput.toUniverse().solve(10) == 1030L)
    check(testInput.toUniverse().solve(100) == 8410L)
    check(part2(testInput) == 82_000_210L)

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
