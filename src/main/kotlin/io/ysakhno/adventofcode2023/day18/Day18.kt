/*
 * --- Day 18: Lavaduct Lagoon ---
 *
 * You are asked to take a look at the dig plan (your puzzle input). For example:
 *
 *     R 6 (#70c710)
 *     D 5 (#0dc571)
 *     L 2 (#5713f0)
 *     D 2 (#d2c081)
 *     R 2 (#59c680)
 *     D 2 (#411b91)
 *     L 5 (#8ceee2)
 *     U 2 (#caa173)
 *     L 1 (#1b58a2)
 *     U 2 (#caa171)
 *     R 2 (#7807d2)
 *     U 3 (#a77fa3)
 *     L 2 (#015232)
 *     U 2 (#7a21e3)
 *
 * The digger starts in a 1-meter cube hole in the ground. They then dig the specified number of meters up (U), down
 * (D), left (L), or right (R), clearing full 1-meter cubes as they go. The directions are given as seen from above, so
 * if "up" were north, then "right" would be east, and so on. Each trench is also listed with the color that the edge of
 * the trench should be painted as an RGB hexadecimal color code.
 *
 * When viewed from above, the above example dig plan would result in the following loop of trench (#) having been dug
 * out from otherwise ground-level terrain (.):
 *
 *     #######
 *     #.....#
 *     ###...#
 *     ..#...#
 *     ..#...#
 *     ###.###
 *     #...#..
 *     ##..###
 *     .#....#
 *     .######
 *
 * At this point, the trench could contain 38 cubic meters of lava. However, this is just the edge of the lagoon; the
 * next step is to dig out the interior so that it is one meter deep as well:
 *
 *     #######
 *     #######
 *     #######
 *     ..#####
 *     ..#####
 *     #######
 *     #####..
 *     #######
 *     .######
 *     .######
 *
 * Now, the lagoon can contain a much more respectable 62 cubic meters of lava. While the interior is dug out, the edges
 * are also painted according to the color codes in the dig plan.
 *
 * The Elves are concerned the lagoon won't be large enough; if they follow their dig plan, how many cubic meters of
 * lava could it hold?
 *
 * --- Part Two ---
 *
 * The Elves were right to be concerned; the planned lagoon would be much too small.
 *
 * After a few minutes, someone realizes what happened; someone swapped the color and instruction parameters when
 * producing the dig plan. They don't have time to fix the bug; one of them asks if you can extract the correct
 * instructions from the hexadecimal codes.
 *
 * Each hexadecimal code is six hexadecimal digits long. The first five hexadecimal digits encode the distance in meters
 * as a five-digit hexadecimal number. The last hexadecimal digit encodes the direction to dig: 0 means R, 1 means D, 2
 * means L, and 3 means U.
 *
 * So, in the above example, the hexadecimal codes can be converted into the true instructions:
 *
 * - #70c710 = R 461937
 * - #0dc571 = D 56407
 * - #5713f0 = R 356671
 * - #d2c081 = D 863240
 * - #59c680 = R 367720
 * - #411b91 = D 266681
 * - #8ceee2 = L 577262
 * - #caa173 = U 829975
 * - #1b58a2 = L 112010
 * - #caa171 = D 829975
 * - #7807d2 = L 491645
 * - #a77fa3 = U 686074
 * - #015232 = L 5411
 * - #7a21e3 = U 500254
 *
 * Digging out this loop and its interior produces a lagoon that can hold an impressive 952408144115 cubic meters of
 * lava.
 *
 * Convert the hexadecimal color codes into the correct instructions; if the Elves follow this new dig plan, how many
 * cubic meters of lava could the lagoon hold?
 */
package io.ysakhno.adventofcode2023.day18

import io.ysakhno.adventofcode2023.day18.Direction.DOWN
import io.ysakhno.adventofcode2023.day18.Direction.LEFT
import io.ysakhno.adventofcode2023.day18.Direction.RIGHT
import io.ysakhno.adventofcode2023.day18.Direction.UP
import io.ysakhno.adventofcode2023.util.ProblemInput
import io.ysakhno.adventofcode2023.util.println
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.math.abs

private val problemInput = object : ProblemInput {}

private enum class Direction { RIGHT, DOWN, LEFT, UP }

private data class DigStep(val dir: Direction, val length: Int)

private fun parseDigStep(str: String) =
    str.split(' ').let { (dir, len) -> DigStep(Direction.entries.first { it.name.startsWith(dir) }, len.toInt()) }

private fun parseDigStepFixed(str: String): DigStep {
    val substr = str.substringAfter('#')
    val length = substr.substring(0..<5).toInt(16)
    val dirCode = substr[5].digitToInt()

    return DigStep(Direction.entries[dirCode], length)
}

private fun List<DigStep>.isInnerAt(idx: Int): Boolean {
    val curDir = this[idx].dir
    val prevDir = this[if (idx > 0) idx - 1 else lastIndex].dir
    return (prevDir.ordinal + 1) % 4 != curDir.ordinal
}

private fun List<DigStep>.toPoints(): List<Pair<Int, Int>> {
    var x = 0
    var y = 0

    return mapIndexed { idx, (dir, length) ->
        val nextIdx = if (idx + 1 < size) idx + 1 else 0
        val inc = 1 - (if (isInnerAt(idx)) 1 else 0) - (if (isInnerAt(nextIdx)) 1 else 0)
        val prevX = x
        val prevY = y

        when (dir) {
            RIGHT -> x += length + inc
            DOWN -> y += length + inc
            LEFT -> x -= length + inc
            UP -> y -= length + inc
        }
        prevX to prevY
    }
}

private fun area(points: List<Pair<Int, Int>>): Long {
    var sum = 0L
    for (i in points.indices) {
        val j = if (i + 1 < points.size) i + 1 else 0
        sum += points[j].first.toLong() * points[i].second.toLong() -
                points[i].first.toLong() * points[j].second.toLong()
    }
    return abs(sum / 2)
}

private fun part1(input: List<String>) = area(input.map(::parseDigStep).toPoints())

private fun part2(input: List<String>) = area(input.map(::parseDigStepFixed).toPoints())

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(62L, part1(testInput), "Part one (sample input)")
    assertEquals(952_408_144_115L, part2(testInput), "Part two (sample input)")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
