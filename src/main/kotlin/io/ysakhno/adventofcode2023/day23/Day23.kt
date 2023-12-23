/*
 * --- Day 23: A Long Walk ---
 *
 * There's a map of nearby hiking trails (your puzzle input) that indicates paths (.), forest (#), and steep slopes
 * (^, >, v, and <).
 *
 * For example:
 *
 *     #.#####################
 *     #.......#########...###
 *     #######.#########.#.###
 *     ###.....#.>.>.###.#.###
 *     ###v#####.#v#.###.#.###
 *     ###.>...#.#.#.....#...#
 *     ###v###.#.#.#########.#
 *     ###...#.#.#.......#...#
 *     #####.#.#.#######.#.###
 *     #.....#.#.#.......#...#
 *     #.#####.#.#.#########v#
 *     #.#...#...#...###...>.#
 *     #.#.#v#######v###.###v#
 *     #...#.>.#...>.>.#.###.#
 *     #####v#.#.###v#.#.###.#
 *     #.....#...#...#.#.#...#
 *     #.#########.###.#.#.###
 *     #...###...#...#...#.###
 *     ###.###.#.###v#####v###
 *     #...#...#.#.>.>.#.>.###
 *     #.###.###.#.###.#.#v###
 *     #.....###...###...#...#
 *     #####################.#
 *
 * You're currently on the single path tile in the top row; your goal is to reach the single path tile in the bottom
 * row. Because of all the mist from the waterfall, the slopes are probably quite icy; if you step onto a slope tile,
 * your next step must be downhill (in the direction the arrow is pointing). To make sure you have the most scenic hike
 * possible, never step onto the same tile twice. What is the longest hike you can take?
 *
 * In the example above, the longest hike you can take is marked with O, and your starting position is marked S:
 *
 *     #S#####################
 *     #OOOOOOO#########...###
 *     #######O#########.#.###
 *     ###OOOOO#OOO>.###.#.###
 *     ###O#####O#O#.###.#.###
 *     ###OOOOO#O#O#.....#...#
 *     ###v###O#O#O#########.#
 *     ###...#O#O#OOOOOOO#...#
 *     #####.#O#O#######O#.###
 *     #.....#O#O#OOOOOOO#...#
 *     #.#####O#O#O#########v#
 *     #.#...#OOO#OOO###OOOOO#
 *     #.#.#v#######O###O###O#
 *     #...#.>.#...>OOO#O###O#
 *     #####v#.#.###v#O#O###O#
 *     #.....#...#...#O#O#OOO#
 *     #.#########.###O#O#O###
 *     #...###...#...#OOO#O###
 *     ###.###.#.###v#####O###
 *     #...#...#.#.>.>.#.>O###
 *     #.###.###.#.###.#.#O###
 *     #.....###...###...#OOO#
 *     #####################O#
 *
 * This hike contains 94 steps. (The other possible hikes you could have taken were 90, 86, 82, 82, and 74 steps long.)
 *
 * Find the longest hike you can take through the hiking trails listed on your map. How many steps long is the longest
 * hike?
 *
 * --- Part Two ---
 *
 * As you reach the trailhead, you realize that the ground isn't as slippery as you expected; you'll have no problem
 * climbing up the steep slopes.
 *
 * Now, treat all slopes as if they were normal paths (.). You still want to make sure you have the most scenic hike
 * possible, so continue to ensure that you never step onto the same tile twice. What is the longest hike you can take?
 *
 * In the example above, this increases the longest hike to 154 steps:
 *
 *     #S#####################
 *     #OOOOOOO#########OOO###
 *     #######O#########O#O###
 *     ###OOOOO#.>OOO###O#O###
 *     ###O#####.#O#O###O#O###
 *     ###O>...#.#O#OOOOO#OOO#
 *     ###O###.#.#O#########O#
 *     ###OOO#.#.#OOOOOOO#OOO#
 *     #####O#.#.#######O#O###
 *     #OOOOO#.#.#OOOOOOO#OOO#
 *     #O#####.#.#O#########O#
 *     #O#OOO#...#OOO###...>O#
 *     #O#O#O#######O###.###O#
 *     #OOO#O>.#...>O>.#.###O#
 *     #####O#.#.###O#.#.###O#
 *     #OOOOO#...#OOO#.#.#OOO#
 *     #O#########O###.#.#O###
 *     #OOO###OOO#OOO#...#O###
 *     ###O###O#O###O#####O###
 *     #OOO#OOO#O#OOO>.#.>O###
 *     #O###O###O#O###.#.#O###
 *     #OOOOO###OOO###...#OOO#
 *     #####################O#
 *
 * Find the longest hike you can take through the surprisingly dry hiking trails listed on your map. How many steps long
 * is the longest hike?
 */
package io.ysakhno.adventofcode2023.day23

import io.ysakhno.adventofcode2023.util.ProblemInput
import io.ysakhno.adventofcode2023.util.println
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.LinkedList

private val problemInput = object : ProblemInput {}

private typealias HikingMap = List<List<Char>>

private data class Pt(val x: Int, val y: Int)

private data class Edge(val from: Pt, val to: Pt, val length: Int)

private val offsets = listOf(0 to -1, -1 to 0, 1 to 0, 0 to 1)

private val HikingMap.findVertices
    get() = flatMapIndexed { y, row ->
        if (y != 0 && y != lastIndex) row.mapIndexed { x, ch -> Pt(x, y) to ch }
            .filter { (_, ch) -> ch != '#' }
            .filter { (pt) ->
                offsets.map { (ofsX, ofsY) -> Pt(pt.x + ofsX, pt.y + ofsY) }.count { this[it.y][it.x] != '#' } > 2
            }.map { (pt) -> pt }
        else emptyList() // Skip first and last rows, their vertices are accounted for separately
    }.toSet()

private fun HikingMap.findEdges(allOtherVertices: Set<Pt>, vertex: Pt, isPart2: Boolean): List<Edge> {
    val queue = LinkedList<Pair<Pt, Int>>().apply { add(vertex to 0) }
    val visited = mutableSetOf<Pt>()
    val edges = mutableListOf<Edge>()

    while (queue.isNotEmpty()) {
        val (cur, len) = queue.poll()

        if (cur !in visited) {
            visited.add(cur)
            if (cur in allOtherVertices) edges.add(Edge(vertex, cur, len))
            else offsets.map { (ofsX, ofsY) -> Pt(cur.x + ofsX, cur.y + ofsY) }
                .filter { it.y in indices }
                .filter {
                    when (this[it.y][it.x]) {
                        '>' -> isPart2 || it.x > cur.x
                        '<' -> isPart2 || it.x < cur.x
                        'v' -> isPart2 || it.y > cur.y
                        '^' -> isPart2 || it.y < cur.y
                        '#' -> false
                        else -> true
                    }
                }
                .forEach { queue.add(it to len + 1) }
        }
    }

    return edges
}

private fun HikingMap.findEdges(vertices: Set<Pt>, isPart2: Boolean) =
    vertices.flatMap { findEdges(vertices - it, it, isPart2) }

private fun Collection<Edge>.toGraph() = groupBy(keySelector = Edge::from, valueTransform = { it.to to it.length })
    .mapValues { (_, list) -> list.associate { it } }

private fun Map<Pt, Map<Pt, Int>>.findLongestPathLength(start: Pt, finish: Pt): Int {
    val queue = LinkedList<Pair<Pt, Set<Pt>>>().apply { add(start to setOf(start)) }
    var maxLength = 0

    while (queue.isNotEmpty()) {
        val (pt, visited) = queue.poll()

        if (pt != finish) getValue(pt).filter { (to) -> to !in visited }.forEach { (p) -> queue.push(p to visited + p) }
        else maxLength = visited.zipWithNext { pt1, pt2 -> getValue(pt1).getValue(pt2) }.sum().coerceAtLeast(maxLength)
    }

    return maxLength
}

private fun solve(input: List<String>, isPart2: Boolean = false): Int {
    val hikingMap: HikingMap = input.map(String::toList)
    val start = Pt(hikingMap.first().indexOf('.'), 0)
    val finish = Pt(hikingMap.last().indexOf('.'), hikingMap.lastIndex)

    return hikingMap
        .findEdges(hikingMap.findVertices + setOf(start, finish), isPart2)
        .toGraph()
        .findLongestPathLength(start, finish)
}

private fun part1(input: List<String>) = solve(input)

private fun part2(input: List<String>) = solve(input, true)

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(94, part1(testInput), "Part one (sample input)")
    assertEquals(154, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
