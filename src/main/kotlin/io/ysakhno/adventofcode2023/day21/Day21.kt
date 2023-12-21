/*
 * --- Day 21: Step Counter ---
 *
 * The gardener elf needs to get his steps in for the day, and so he'd like to know, which garden plots he can reach
 * with exactly his remaining 64 steps.
 *
 * He gives you an up-to-date map (your puzzle input) of his starting position (S), garden plots (.), and rocks (#). For
 * example:
 *
 *     ...........
 *     .....###.#.
 *     .###.##..#.
 *     ..#.#...#..
 *     ....#.#....
 *     .##..S####.
 *     .##..#...#.
 *     .......##..
 *     .##.#.####.
 *     .##..##.##.
 *     ...........
 *
 * The Elf starts at the starting position (S) which also counts as a garden plot. Then, he can take one step north,
 * south, east, or west, but only onto tiles that are garden plots. This would allow him to reach any of the tiles
 * marked O:
 *
 *     ...........
 *     .....###.#.
 *     .###.##..#.
 *     ..#.#...#..
 *     ....#O#....
 *     .##.OS####.
 *     .##..#...#.
 *     .......##..
 *     .##.#.####.
 *     .##..##.##.
 *     ...........
 *
 * Then, he takes a second step. Since at this point he could be at either tile marked O, his second step would allow
 * him to reach any garden plot that is one step north, south, east, or west of any tile that he could have reached
 * after the first step:
 *
 *     ...........
 *     .....###.#.
 *     .###.##..#.
 *     ..#.#O..#..
 *     ....#.#....
 *     .##O.O####.
 *     .##.O#...#.
 *     .......##..
 *     .##.#.####.
 *     .##..##.##.
 *     ...........
 *
 * After two steps, he could be at any of the tiles marked O above, including the starting position (either by going
 * north-then-south or by going west-then-east).
 *
 * A single third step leads to even more possibilities:
 *
 *     ...........
 *     .....###.#.
 *     .###.##..#.
 *     ..#.#.O.#..
 *     ...O#O#....
 *     .##.OS####.
 *     .##O.#...#.
 *     ....O..##..
 *     .##.#.####.
 *     .##..##.##.
 *     ...........
 *
 * He will continue like this until his steps for the day have been exhausted. After a total of 6 steps, he could reach
 * any of the garden plots marked O:
 *
 *     ...........
 *     .....###.#.
 *     .###.##.O#.
 *     .O#O#O.O#..
 *     O.O.#.#.O..
 *     .##O.O####.
 *     .##.O#O..#.
 *     .O.O.O.##..
 *     .##.#.####.
 *     .##O.##.##.
 *     ...........
 *
 * In this example, if the Elf's goal was to get exactly 6 more steps today, he could use them to reach any of 16 garden
 * plots.
 *
 * However, the Elf actually needs to get 64 steps today, and the map he's handed you is much larger than the example
 * map.
 *
 * Starting from the garden plot marked S on your map, how many garden plots could the Elf reach in exactly 64 steps?
 *
 * --- Part Two ---
 *
 * The Elf seems confused by your answer until he realizes his mistake: he was reading from a list of his favorite
 * numbers that are both perfect squares and perfect cubes, not his step counter.
 *
 * The actual number of steps he needs to get today is exactly 26501365.
 *
 * He also points out that the garden plots and rocks are set up so that the map repeats infinitely in every direction.
 *
 * So, if you were to look one additional map-width or map-height out from the edge of the example map above, you would
 * find that it keeps repeating:
 *
 *     .................................
 *     .....###.#......###.#......###.#.
 *     .###.##..#..###.##..#..###.##..#.
 *     ..#.#...#....#.#...#....#.#...#..
 *     ....#.#........#.#........#.#....
 *     .##...####..##...####..##...####.
 *     .##..#...#..##..#...#..##..#...#.
 *     .......##.........##.........##..
 *     .##.#.####..##.#.####..##.#.####.
 *     .##..##.##..##..##.##..##..##.##.
 *     .................................
 *     .................................
 *     .....###.#......###.#......###.#.
 *     .###.##..#..###.##..#..###.##..#.
 *     ..#.#...#....#.#...#....#.#...#..
 *     ....#.#........#.#........#.#....
 *     .##...####..##..S####..##...####.
 *     .##..#...#..##..#...#..##..#...#.
 *     .......##.........##.........##..
 *     .##.#.####..##.#.####..##.#.####.
 *     .##..##.##..##..##.##..##..##.##.
 *     .................................
 *     .................................
 *     .....###.#......###.#......###.#.
 *     .###.##..#..###.##..#..###.##..#.
 *     ..#.#...#....#.#...#....#.#...#..
 *     ....#.#........#.#........#.#....
 *     .##...####..##...####..##...####.
 *     .##..#...#..##..#...#..##..#...#.
 *     .......##.........##.........##..
 *     .##.#.####..##.#.####..##.#.####.
 *     .##..##.##..##..##.##..##..##.##.
 *     .................................
 *
 * This is just a tiny three-map-by-three-map slice of the inexplicably-infinite farm layout; garden plots and rocks
 * repeat as far as you can see. The Elf still starts on the one middle tile marked S, though - every other repeated S
 * is replaced with a normal garden plot (.).
 *
 * Here are the number of reachable garden plots in this new infinite version of the example map for different numbers
 * of steps:
 *
 * - In exactly 6 steps, he can still reach 16 garden plots.
 * - In exactly 10 steps, he can reach any of 50 garden plots.
 * - In exactly 50 steps, he can reach 1594 garden plots.
 * - In exactly 100 steps, he can reach 6536 garden plots.
 * - In exactly 500 steps, he can reach 167004 garden plots.
 * - In exactly 1000 steps, he can reach 668697 garden plots.
 * - In exactly 5000 steps, he can reach 16733044 garden plots.
 *
 * However, the step count the Elf needs is much larger! Starting from the garden plot marked S on your infinite map,
 * how many garden plots could the Elf reach in exactly 26501365 steps?
 */
package io.ysakhno.adventofcode2023.day21

import io.ysakhno.adventofcode2023.util.ProblemInput
import io.ysakhno.adventofcode2023.util.bordered
import io.ysakhno.adventofcode2023.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private data class Pt(val x: Int, val y: Int)

private fun List<List<Char>>.neighbors(pt: Pt): List<Pt> {
    val (x, y) = pt
    return buildList(4) {
        if (this@neighbors[y][x - 1] == '.') add(Pt(x - 1, y))
        if (this@neighbors[y][x + 1] == '.') add(Pt(x + 1, y))
        if (this@neighbors[y - 1][x] == '.') add(Pt(x, y - 1))
        if (this@neighbors[y + 1][x] == '.') add(Pt(x, y + 1))
    }
}

private fun List<List<Char>>.aimlesslyRoamAround(start: Pt, numSteps: Int = 64) =
    (1..numSteps).fold(setOf(start)) { pts, _ -> pts.flatMap(this::neighbors).toSet() }.size

private fun part1(input: List<String>, numSteps: Int = 64): Int {
    var start = Pt(0, 0)

    input.forEachIndexed { rowIdx, row ->
        val idxFound = row.indexOf('S')
        if (idxFound >= 0) {
            start = Pt(idxFound + 1, rowIdx + 1)
        }
    }

    val map = input.map { it.toList().map { ch -> if (ch == 'S') '.' else ch } }.bordered('#')

    return map.aimlesslyRoamAround(start, numSteps)
}

private fun part2(input: List<String>): Int {
    return input.size
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(16, part1(testInput, 6), "Part one (sample input)")
    assertEquals(11, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
