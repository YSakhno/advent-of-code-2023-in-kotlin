/*
 * --- Day 16: The Floor Will Be Lava ---
 *
 * You note the layout of the contraption (your puzzle input). For example:
 *
 *     .|...\....
 *     |.-.\.....
 *     .....|-...
 *     ........|.
 *     ..........
 *     .........\
 *     ..../.\\..
 *     .-.-/..|..
 *     .|....-|.\
 *     ..//.|....
 *
 * The beam enters in the top-left corner from the left and heading to the right. Then, its behavior depends on what it
 * encounters as it moves:
 *
 * - If the beam encounters empty space (.), it continues in the same direction.
 * - If the beam encounters a mirror (/ or \), the beam is reflected 90 degrees depending on the angle of the mirror.
 *   For instance, a rightward-moving beam that encounters a / mirror would continue upward in the mirror's column,
 *   while a rightward-moving beam that encounters a \ mirror would continue downward from the mirror's column.
 * - If the beam encounters the pointy end of a splitter (| or -), the beam passes through the splitter as if the
 *   splitter were empty space. For instance, a rightward-moving beam that encounters a - splitter would continue in the
 *   same direction.
 * - If the beam encounters the flat side of a splitter (| or -), the beam is split into two beams going in each of the
 *   two directions the splitter's pointy ends are pointing. For instance, a rightward-moving beam that encounters a |
 *   splitter would split into two beams: one that continues upward from the splitter's column and one that continues
 *   downward from the splitter's column.
 *
 * Beams do not interact with other beams; a tile can have many beams passing through it at the same time. A tile is
 * energized if that tile has at least one beam pass through it, reflect in it, or split in it.
 *
 * In the above example, here is how the beam of light bounces around the contraption:
 *
 *     >|<<<\....
 *     |v-.\^....
 *     .v...|->>>
 *     .v...v^.|.
 *     .v...v^...
 *     .v...v^..\
 *     .v../2\\..
 *     <->-/vv|..
 *     .|<<<2-|.\
 *     .v//.|.v..
 *
 * Beams are only shown on empty tiles; arrows indicate the direction of the beams. If a tile contains beams moving in
 * multiple directions, the number of distinct directions is shown instead. Here is the same diagram but instead only
 * showing whether a tile is energized (#) or not (.):
 *
 *     ######....
 *     .#...#....
 *     .#...#####
 *     .#...##...
 *     .#...##...
 *     .#...##...
 *     .#..####..
 *     ########..
 *     .#######..
 *     .#...#.#..
 *
 * Ultimately, in this example, 46 tiles become energized.
 *
 * The light isn't energizing enough tiles to produce lava; to debug the contraption, you need to start by analyzing the
 * current situation. With the beam starting in the top-left heading right, how many tiles end up being energized?
 *
 * --- Part Two ---
 *
 * As you try to work out what might be wrong, the reindeer tugs on your shirt and leads you to a nearby control panel.
 * There, a collection of buttons lets you align the contraption so that the beam enters from any edge tile and heading
 * away from that edge. (You can choose either of two directions for the beam if it starts on a corner; for instance, if
 * the beam starts in the bottom-right corner, it can start heading either left or upward.)
 *
 * So, the beam could start on any tile in the top row (heading downward), any tile in the bottom row (heading upward),
 * any tile in the leftmost column (heading right), or any tile in the rightmost column (heading left). To produce lava,
 * you need to find the configuration that energizes as many tiles as possible.
 *
 * In the above example, this can be achieved by starting the beam in the fourth tile from the left in the top row:
 *
 *     .|<2<\....
 *     |v-v\^....
 *     .v.v.|->>>
 *     .v.v.v^.|.
 *     .v.v.v^...
 *     .v.v.v^..\
 *     .v.v/2\\..
 *     <-2-/vv|..
 *     .|<<<2-|.\
 *     .v//.|.v..
 *
 * Using this configuration, 51 tiles are energized:
 *
 *     .#####....
 *     .#.#.#....
 *     .#.#.#####
 *     .#.#.##...
 *     .#.#.##...
 *     .#.#.##...
 *     .#.#####..
 *     ########..
 *     .#######..
 *     .#...#.#..
 *
 * Find the initial beam configuration that energizes the largest number of tiles; how many tiles are energized in that
 * configuration?
 */
package io.ysakhno.adventofcode2023.day16

import io.ysakhno.adventofcode2023.day16.Direction.DOWN
import io.ysakhno.adventofcode2023.day16.Direction.LEFT
import io.ysakhno.adventofcode2023.day16.Direction.RIGHT
import io.ysakhno.adventofcode2023.day16.Direction.UP
import io.ysakhno.adventofcode2023.util.ProblemInput
import io.ysakhno.adventofcode2023.util.println
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.Deque
import java.util.LinkedList

private val problemInput = object : ProblemInput {}

private enum class Direction { RIGHT, DOWN, LEFT, UP }

private data class Tile(val x: Int, val y: Int) {
    var dirs = setOf<Direction>()
}

private data class Photon(val x: Int, val y: Int, val dir: Direction)

private fun List<List<Char>>.bordered() =
    listOf(List(size + 2) { 'x' }).let { extraRow -> extraRow + map { listOf('x') + it + listOf('x') } + extraRow }

private fun List<List<Char>>.countEnergized(startingPhoton: Photon): Int {
    val tiles = flatMapIndexed { y, row -> List(row.size) { x -> Tile(x, y) } }.associateWith { it }
    val photons: Deque<Photon> = LinkedList(listOf(startingPhoton))

    while (photons.isNotEmpty()) {
        val photon = photons.poll()
        val tile = tiles.getValue(Tile(photon.x, photon.y))

        if (photon.dir !in tile.dirs) tile.dirs += photon.dir else continue

        photons += when (photon.dir) {
            RIGHT -> when (this[photon.y][photon.x + 1]) {
                '.', '-' -> listOf(photon.copy(x = photon.x + 1))
                '/' -> listOf(photon.copy(x = photon.x + 1, dir = UP))
                '\\' -> listOf(photon.copy(x = photon.x + 1, dir = DOWN))
                '|' -> listOf(photon.copy(x = photon.x + 1, dir = DOWN), photon.copy(x = photon.x + 1, dir = UP))
                else -> emptyList()
            }

            DOWN -> when (this[photon.y + 1][photon.x]) {
                '.', '|' -> listOf(photon.copy(y = photon.y + 1))
                '/' -> listOf(photon.copy(y = photon.y + 1, dir = LEFT))
                '\\' -> listOf(photon.copy(y = photon.y + 1, dir = RIGHT))
                '-' -> listOf(photon.copy(y = photon.y + 1, dir = LEFT), photon.copy(y = photon.y + 1, dir = RIGHT))
                else -> emptyList()
            }

            LEFT -> when (this[photon.y][photon.x - 1]) {
                '.', '-' -> listOf(photon.copy(x = photon.x - 1))
                '/' -> listOf(photon.copy(x = photon.x - 1, dir = DOWN))
                '\\' -> listOf(photon.copy(x = photon.x - 1, dir = UP))
                '|' -> listOf(photon.copy(x = photon.x - 1, dir = DOWN), photon.copy(x = photon.x - 1, dir = UP))
                else -> emptyList()
            }

            UP -> when (this[photon.y - 1][photon.x]) {
                '.', '|' -> listOf(photon.copy(y = photon.y - 1))
                '/' -> listOf(photon.copy(y = photon.y - 1, dir = RIGHT))
                '\\' -> listOf(photon.copy(y = photon.y - 1, dir = LEFT))
                '-' -> listOf(photon.copy(y = photon.y - 1, dir = RIGHT), photon.copy(y = photon.y - 1, dir = LEFT))
                else -> emptyList()
            }
        }
    }

    return tiles.values.count { it.dirs.isNotEmpty() } - 1
}

private fun part1(input: List<String>) = input.map(String::toList).bordered().countEnergized(Photon(0, 1, RIGHT))

private fun part2(input: List<String>): Int {
    val layout = input.map(String::toList).bordered()

    return ((1..<layout.size - 1).flatMap { y ->
        listOf(Photon(0, y, RIGHT), Photon(layout.first().lastIndex, y, LEFT))
    } + (1..<layout.first().size - 1).flatMap { x ->
        listOf(Photon(x, 0, DOWN), Photon(x, layout.lastIndex, UP))
    }).maxOf(layout::countEnergized)
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(46, part1(testInput), "Part one (sample input)")
    assertEquals(51, part2(testInput), "Part two (sample input)")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
