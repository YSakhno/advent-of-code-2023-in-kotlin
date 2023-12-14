/*
 * --- Day 14: Parabolic Reflector Dish ---
 *
 * The rounded rocks (O) will roll when the platform is tilted, while the cube-shaped rocks (#) will stay in place. You
 * note the positions of all empty spaces (.) and rocks (your puzzle input). For example:
 *
 *     O....#....
 *     O.OO#....#
 *     .....##...
 *     OO.#O....O
 *     .O.....O#.
 *     O.#..O.#.#
 *     ..O..#O..O
 *     .......O..
 *     #....###..
 *     #OO..#....
 *
 * Start by tilting the lever so all rocks will slide north as far as they will go:
 *
 *     OOOO.#.O..
 *     OO..#....#
 *     OO..O##..O
 *     O..#.OO...
 *     ........#.
 *     ..#....#.#
 *     ..O..#.O.O
 *     ..O.......
 *     #....###..
 *     #....#....
 *
 * You notice that the support beams along the north side of the platform are damaged; to ensure the platform doesn't
 * collapse, you should calculate the total load on the north support beams.
 *
 * The amount of load caused by a single rounded rock (O) is equal to the number of rows from the rock to the south edge
 * of the platform, including the row the rock is on. (Cube-shaped rocks (#) don't contribute to load.) So, the amount
 * of load caused by each rock in each row is as follows:
 *
 *     OOOO.#.O.. 10
 *     OO..#....#  9
 *     OO..O##..O  8
 *     O..#.OO...  7
 *     ........#.  6
 *     ..#....#.#  5
 *     ..O..#.O.O  4
 *     ..O.......  3
 *     #....###..  2
 *     #....#....  1
 *
 * The total load is the sum of the load caused by all rounded rocks. In this example, the total load is 136.
 *
 * Tilt the platform so that the rounded rocks all roll north. Afterward, what is the total load on the north support
 * beams?
 *
 * --- Part Two ---
 *
 * The parabolic reflector dish deforms, but not in a way that focuses the beam. To do that, you'll need to move the
 * rocks to the edges of the platform. Fortunately, a button on the side of the control panel labeled "spin cycle"
 * attempts to do just that!
 *
 * Each cycle tilts the platform four times so that the rounded rocks roll north, then west, then south, then east.
 * After each tilt, the rounded rocks roll as far as they can before the platform tilts in the next direction. After one
 * cycle, the platform will have finished rolling the rounded rocks in those four directions in that order.
 *
 * Here's what happens in the example above after each of the first few cycles:
 *
 *     After 1 cycle:
 *     .....#....
 *     ....#...O#
 *     ...OO##...
 *     .OO#......
 *     .....OOO#.
 *     .O#...O#.#
 *     ....O#....
 *     ......OOOO
 *     #...O###..
 *     #..OO#....
 *
 *     After 2 cycles:
 *     .....#....
 *     ....#...O#
 *     .....##...
 *     ..O#......
 *     .....OOO#.
 *     .O#...O#.#
 *     ....O#...O
 *     .......OOO
 *     #..OO###..
 *     #.OOO#...O
 *
 *     After 3 cycles:
 *     .....#....
 *     ....#...O#
 *     .....##...
 *     ..O#......
 *     .....OOO#.
 *     .O#...O#.#
 *     ....O#...O
 *     .......OOO
 *     #...O###.O
 *     #.OOO#...O
 *
 * This process should work if you leave it running long enough, but you're still worried about the north support beams.
 * To make sure they'll survive for a while, you need to calculate the total load on the north support beams after
 * 1000000000 cycles.
 *
 * In the above example, after 1000000000 cycles, the total load on the north support beams is 64.
 *
 * Run the spin cycle for 1000000000 cycles. Afterward, what is the total load on the north support beams?
 */
package io.ysakhno.adventofcode2023.day14

import io.ysakhno.adventofcode2023.util.ProblemInput
import io.ysakhno.adventofcode2023.util.println
import io.ysakhno.adventofcode2023.util.transpose

private val problemInput = object : ProblemInput {}

private fun List<List<Char>>.rollOnce() = map {
    val row = if (it !is MutableList) it.toMutableList() else it
    var i = 0
    while (i < row.size) {
        if (row[i] != 'O' && row[i] != '#') {
            for (j in i + 1..<row.size) {
                when (row[j]) {
                    'O' -> {
                        row[i] = 'O'
                        row[j] = '.'
                    }
                    '#' -> i = j
                    else -> continue
                }
                break
            }
        }
        ++i
    }
    row
}

private fun List<List<Char>>.spinCycle() = transpose()
    // Roll after tilting to North
    .rollOnce()
    .transpose()
    // Roll after tilting to West
    .rollOnce()
    .reversed()
    .transpose()
    // Roll after tilting to South
    .rollOnce()
    .reversed()
    .transpose()
    // Roll after tilting to East
    .rollOnce()
    .map(MutableList<Char>::reversed)
    .reversed()

private fun repeatSpinCycles(grid: List<List<Char>>, @Suppress("SameParameterValue") numCycles: Int): List<List<Char>> {
    var current = grid
    val generatedStates = mutableListOf(current)

    for (i in 1..numCycles) {
        current = current.spinCycle()
        if (current in generatedStates) {
            val indexOfSeen = generatedStates.indexOf(current)
            return generatedStates[indexOfSeen + (numCycles - indexOfSeen) % (i - indexOfSeen)]
        } else generatedStates += current
    }

    return current
}

private val List<List<Char>>.totalLoad get() = reversed()
    .mapIndexed { rowIdx, row -> row.count { it == 'O' } * (rowIdx + 1) }
    .sum()

private fun part1(input: List<String>) = input.map(String::toList)
    .transpose()
    .rollOnce()
    .transpose()
    .totalLoad

private fun part2(input: List<String>) = repeatSpinCycles(input.map(String::toList), 1_000_000_000).totalLoad

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    check(part1(testInput) == 136)
    check(part2(testInput) == 64)

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
