/*
 * --- Day 8: Haunted Wasteland ---
 *
 * It's full of documents (your puzzle input) about how to navigate the desert. At least, you're pretty sure that's what
 * they are; one of the documents contains a list of left/right instructions, and the rest of the documents seem to
 * describe some kind of network of labeled nodes.
 *
 * It seems like you're meant to use the left/right instructions to navigate the network. Perhaps if you have the camel
 * follow the same instructions, you can escape the haunted wasteland!
 *
 * After examining the maps for a bit, two nodes stick out: AAA and ZZZ. You feel like AAA is where you are now, and you
 * have to follow the left/right instructions until you reach ZZZ.
 *
 * This format defines each node of the network individually. For example:
 *
 *     RL
 *
 *     AAA = (BBB, CCC)
 *     BBB = (DDD, EEE)
 *     CCC = (ZZZ, GGG)
 *     DDD = (DDD, DDD)
 *     EEE = (EEE, EEE)
 *     GGG = (GGG, GGG)
 *     ZZZ = (ZZZ, ZZZ)
 *
 * Starting with AAA, you need to look up the next element based on the next left/right instruction in your input. In
 * this example, start with AAA and go right (R) by choosing the right element of AAA, CCC. Then, L means to choose the
 * left element of CCC, ZZZ. By following the left/right instructions, you reach ZZZ in 2 steps.
 *
 * Of course, you might not find ZZZ right away. If you run out of left/right instructions, repeat the whole sequence of
 * instructions as necessary: RL really means RLRLRLRLRLRLRLRL... and so on. For example, here is a situation that takes
 * 6 steps to reach ZZZ:
 *
 *     LLR
 *
 *     AAA = (BBB, BBB)
 *     BBB = (AAA, ZZZ)
 *     ZZZ = (ZZZ, ZZZ)
 *
 * Starting at AAA, follow the left/right instructions. How many steps are required to reach ZZZ?
 *
 * --- Part Two ---
 *
 * The sandstorm is upon you and you aren't any closer to escaping the wasteland. You had the camel follow the
 * instructions, but you've barely left your starting position. It's going to take significantly more steps to escape!
 *
 * What if the map isn't for people - what if the map is for ghosts? Are ghosts even bound by the laws of spacetime?
 * Only one way to find out.
 *
 * After examining the maps a bit longer, your attention is drawn to a curious fact: the number of nodes with names
 * ending in A is equal to the number ending in Z! If you were a ghost, you'd probably just start at every node that
 * ends with A and follow all of the paths at the same time until they all simultaneously end up at nodes that end with
 * Z.
 *
 * For example:
 *
 *     LR
 *
 *     11A = (11B, XXX)
 *     11B = (XXX, 11Z)
 *     11Z = (11B, XXX)
 *     22A = (22B, XXX)
 *     22B = (22C, 22C)
 *     22C = (22Z, 22Z)
 *     22Z = (22B, 22B)
 *     XXX = (XXX, XXX)
 *
 * Here, there are two starting nodes, 11A and 22A (because they both end with A). As you follow each left/right
 * instruction, use that instruction to simultaneously navigate away from both nodes you're currently on. Repeat this
 * process until all of the nodes you're currently on end with Z. (If only some of the nodes you're on end with Z, they
 * act like any other node and you continue as normal.) In this example, you would proceed as follows:
 *
 * - Step 0: You are at 11A and 22A.
 * - Step 1: You choose all of the left paths, leading you to 11B and 22B.
 * - Step 2: You choose all of the right paths, leading you to 11Z and 22C.
 * - Step 3: You choose all of the left paths, leading you to 11B and 22Z.
 * - Step 4: You choose all of the right paths, leading you to 11Z and 22B.
 * - Step 5: You choose all of the left paths, leading you to 11B and 22C.
 * - Step 6: You choose all of the right paths, leading you to 11Z and 22Z.
 *
 * So, in this example, you end up entirely on nodes that end in Z after 6 steps.
 *
 * Simultaneously start on every node that ends with A. How many steps does it take before you're only on nodes that end
 * with Z?
 */
package io.ysakhno.adventofcode2023.day08

import io.ysakhno.adventofcode2023.util.ProblemInput
import io.ysakhno.adventofcode2023.util.allWords
import io.ysakhno.adventofcode2023.util.println

private val problemInput = object : ProblemInput {}

private typealias WorldMap = Map<String, Pair<String, String>>

private fun WorldMap.next(current: String, step: Char) =
    getValue(current).let { if (step == 'L') it.first else it.second }

private fun String.stepAt(index: Int) = this[index % length]
private fun String.stepAt(index: Long) = this[(index % length).toInt()]

private fun findCycleStart(start: String, worldMap: WorldMap, steps: String): Int {
    var tortoise = start
    var hare = start
    var tortoiseIdx = 0
    var hareIdx = 0

    while (true) {
        tortoise = worldMap.next(tortoise, steps.stepAt(tortoiseIdx++))
        hare = worldMap.next(hare, steps.stepAt(hareIdx++))
        hare = worldMap.next(hare, steps.stepAt(hareIdx++))

        if (tortoise == hare && tortoiseIdx % steps.length == hareIdx % steps.length) break
    }

    var numSteps = 0

    tortoise = start
    tortoiseIdx = 0
    while (tortoise != hare || tortoiseIdx % steps.length != hareIdx % steps.length) {
        tortoise = worldMap.next(tortoise, steps.stepAt(tortoiseIdx++))
        hare = worldMap.next(hare, steps.stepAt(hareIdx++))
        ++numSteps
    }

    return numSteps
}

private fun findCycleLength(walker: Walker, worldMap: WorldMap, steps: String): Int {
    val cycleStart = walker.current
    var current = cycleStart
    val startIdx = walker.numStepsTaken
    var numStepsTaken = startIdx
    var cycleLength = 0

    do {
        current = worldMap.next(current, steps.stepAt(numStepsTaken++))
        ++cycleLength
    } while (current != cycleStart || numStepsTaken % steps.length != startIdx % steps.length)

    return cycleLength
}

private data class Walker(
    val steps: String,
    val worldMap: WorldMap,
    var current: String,
    var numStepsTaken: Long = 0L,
) {
    val isArrived get() = current[0] == 'Z'

    fun advance() {
        current = worldMap.next(current, steps.stepAt(numStepsTaken++))
    }
}

private data class Strider(val destinations: List<Int>, val cycleLength: Int, var currentPos: Int = 0) {

    init {
        require(destinations.isNotEmpty()) { "Must have at least one destination" }
    }

    val distToNext get() = (destinations.indexOfFirst { it < currentPos } + 1).let { idx ->
        if (idx == destinations.size) destinations.first() + cycleLength - currentPos
        else destinations[idx] - currentPos
    }

    fun advanceBy(steps: Int) {
        currentPos += steps
        currentPos %= cycleLength
    }
}

private fun createStrider(walker: Walker, cycleLen: Int): Strider {
    val walkerCopy = walker.copy()
    val destinationIndices = (0..<cycleLen).map {
        walkerCopy.isArrived.also { walkerCopy.advance() }
    }.mapIndexedNotNull { idx, isArrived -> if (isArrived) idx else null }

    return Strider(destinationIndices, cycleLen)
}

private fun part1(input: List<String>): Int {
    val steps = input.first()
    val worldMap = input.drop(1)
        .map { it.allWords().toList() }
        .fold(mutableMapOf<String, Pair<String, String>>()) { acc, (src, left, right) ->
            acc[src] = left to right
            acc
        }

    var current = "AAA"
    var numStepsTaken = 0

    while (current != "ZZZ") {
        val (left, right) = worldMap.getValue(current)
        current = when (steps.stepAt(numStepsTaken++)) {
            'R' -> right
            'L' -> left
            else -> error("Unknown direction")
        }
    }

    return numStepsTaken
}

private fun part2(input: List<String>): Long {
    val steps = input.first()
    val worldMap: WorldMap = input.drop(1)
        .map { "\\b[0-9A-Z]+\\b".toRegex().findAll(it).map(MatchResult::value).map(String::reversed).toList() }
        .fold(mutableMapOf()) { acc, (src, left, right) ->
            acc[src] = left to right
            acc
        }

    val walkers = worldMap.keys.filter { it[0] == 'A' }.map { Walker(steps, worldMap, it) }
    val cycleStartIndices = walkers.map { findCycleStart(it.current, worldMap, steps) }.println()
    val maxCycleStartIndex = cycleStartIndices.max()

    while (walkers.any { !it.isArrived } && walkers.first().numStepsTaken < maxCycleStartIndex) {
        walkers.forEach(Walker::advance)
    }

    var numStepsTaken = walkers.first().numStepsTaken
    if  (walkers.all(Walker::isArrived)) return numStepsTaken

    val cycleLengths = List(cycleStartIndices.size) { idx ->
        findCycleLength(walkers[idx], worldMap, steps)
    }.println()

    val striders = cycleLengths.mapIndexed { idx, len -> createStrider(walkers[idx], len) }
    var iterNum = 0

    while (true) {
        val nextSteps = striders.map(Strider::distToNext)
        if (nextSteps.all { it == 0 }) return numStepsTaken.also { println() }
        val minSteps = nextSteps.filter { it != 0 }.min()

        striders.forEach { strider -> strider.advanceBy(minSteps) }
        numStepsTaken += minSteps.toLong()

        if (++iterNum == 10_000_000) {
            print("\rTook $numStepsTaken steps so far")
            iterNum = 0
        }
    }
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput1 = problemInput.readTest(1)
    val testInput2 = problemInput.readTest(2)
    check(part1(testInput1) == 6)
    check(part2(testInput2) == 6L)

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
