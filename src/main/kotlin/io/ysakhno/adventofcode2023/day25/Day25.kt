/*
 * --- Day 25: Snowverload ---
 *
 * Fortunately, someone left a wiring diagram (your puzzle input) that shows how the components are connected. For
 * example:
 *
 *     jqt: rhn xhk nvd
 *     rsh: frs pzl lsr
 *     xhk: hfx
 *     cmg: qnr nvd lhk bvb
 *     rhn: xhk bvb hfx
 *     bvb: xhk hfx
 *     pzl: lsr hfx nvd
 *     qnr: nvd
 *     ntq: jqt hfx bvb xhk
 *     nvd: lhk
 *     lsr: lhk
 *     rzs: qnr cmg lsr rsh
 *     frs: qnr lhk lsr
 *
 * Each line shows the name of a component, a colon, and then a list of other components to which that component is
 * connected. Connections aren't directional; abc: xyz and xyz: abc both represent the same configuration. Each
 * connection between two components is represented only once, so some components might only ever appear on the left or
 * right side of a colon.
 *
 * In this example, if you disconnect the wire between hfx/pzl, the wire between bvb/cmg, and the wire between nvd/jqt,
 * you will divide the components into two separate, disconnected groups:
 *
 * - 9 components: cmg, frs, lhk, lsr, nvd, pzl, qnr, rsh, and rzs.
 * - 6 components: bvb, hfx, jqt, ntq, rhn, and xhk.
 *
 * Multiplying the sizes of these groups together produces 54.
 *
 * Find the three wires you need to disconnect to divide the components into two separate groups. What do you get if you
 * multiply the sizes of these two groups together?
 */
package io.ysakhno.adventofcode2023.day25

import io.ysakhno.adventofcode2023.util.ProblemInput
import io.ysakhno.adventofcode2023.util.allWords
import io.ysakhno.adventofcode2023.util.println
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.math.max

private val problemInput = object : ProblemInput {}

private data class Node(val neighbors: Collection<Int>, val numVertices: Int = 1)

private fun Node.merge(other: Node?, s: Int, t: Int) = if (other != null) copy(
    neighbors = neighbors.filter { it != t } + other.neighbors.filter { it != s },
    numVertices = numVertices + other.numVertices
) else this

private fun Node.replace(old: Int, new: Int) = copy(neighbors = neighbors.map { if (it == old) new else it })

private fun minimumCutPhase(adjList: List<Collection<Int>>): Map<Int, Node> {
    val graph = adjList.mapIndexed { idx, neighbors -> idx to Node(neighbors) }.associateTo(hashMapOf()) { it }

    fun contract(s: Int, t: Int) {
        graph[s] = graph.getValue(s).merge(graph.remove(t), s, t)
        graph.forEach { (vertex, node) -> if (vertex != s) graph[vertex] = node.replace(t, s) }
    }

    while (graph.size > 2) {
        val s = graph.keys.random()
        val t = graph.getValue(s).neighbors.random()

        contract(s, t)
    }

    return graph
}

private fun minimumCut(adjList: List<Collection<Int>>): Pair<Int, Int> {
    while (true) {
        val cut = minimumCutPhase(adjList)
        if (cut.size == 2 && cut.getValue(cut.keys.first()).neighbors.size == 3) {
            return cut.getValue(cut.keys.first()).numVertices to cut.getValue(cut.keys.last()).numVertices
        }
    }
}

fun <A, B> Pair<A, B>.swap(): Pair<B, A> = second to first

private fun part1(input: List<String>): Int {
    val sentences = input.map { it.allWords().toList() }
    val wordsToIndex = sentences.flatten().distinct().mapIndexed { idx, word -> word to idx }.associate { it }

    val edges = sentences
        .map { list -> wordsToIndex.getValue(list.first()) to list.drop(1).map { wordsToIndex.getValue(it) } }
        .flatMap { (node1, nodes) -> nodes.map { node1 to it }.map { if (it.first > it.second) it.swap() else it } }
        .toHashSet()
    val adjList = MutableList(edges.maxOf { (n1, n2) -> max(n1, n2) } + 1) { mutableListOf<Int>() }

    edges.forEach { (u, v) ->
        adjList[u] += v
        adjList[v] += u
    }

    val (left, right) = minimumCut(adjList)
    return left * right
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(54, part1(testInput), "Part one (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
}
