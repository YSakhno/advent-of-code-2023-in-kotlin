/*
 * --- Day 17: Clumsy Crucible ---
 *
 * To get Desert Island the machine parts it needs as soon as possible, you'll need to find the best way to get the
 * crucible from the lava pool to the machine parts factory. To do this, you need to minimize heat loss while choosing a
 * route that doesn't require the crucible to go in a straight line for too long.
 *
 * Fortunately, the Elves here have a map (your puzzle input) that uses traffic patterns, ambient temperature, and
 * hundreds of other parameters to calculate exactly how much heat loss can be expected for a crucible entering any
 * particular city block.
 *
 * For example:
 *
 *     2413432311323
 *     3215453535623
 *     3255245654254
 *     3446585845452
 *     4546657867536
 *     1438598798454
 *     4457876987766
 *     3637877979653
 *     4654967986887
 *     4564679986453
 *     1224686865563
 *     2546548887735
 *     4322674655533
 *
 * Each city block is marked by a single digit that represents the amount of heat loss if the crucible enters that
 * block. The starting point, the lava pool, is the top-left city block; the destination, the machine parts factory, is
 * the bottom-right city block. (Because you already start in the top-left block, you don't incur that block's heat loss
 * unless you leave that block and then return to it.)
 *
 * Because it is difficult to keep the top-heavy crucible going in a straight line for very long, it can move at most
 * three blocks in a single direction before it must turn 90 degrees left or right. The crucible also can't reverse
 * direction; after entering each city block, it may only turn left, continue straight, or turn right.
 *
 * One way to minimize heat loss is this path:
 *
 *     2>>34^>>>1323
 *     32v>>>35v5623
 *     32552456v>>54
 *     3446585845v52
 *     4546657867v>6
 *     14385987984v4
 *     44578769877v6
 *     36378779796v>
 *     465496798688v
 *     456467998645v
 *     12246868655<v
 *     25465488877v5
 *     43226746555v>
 *
 * This path never moves more than three consecutive blocks in the same direction and incurs a heat loss of only 102.
 *
 * Directing the crucible from the lava pool to the machine parts factory, but not moving more than three consecutive
 * blocks in the same direction, what is the least heat loss it can incur?
 *
 * --- Part Two ---
 *
 * The crucibles of lava simply aren't large enough to provide an adequate supply of lava to the machine parts factory.
 * Instead, the Elves are going to upgrade to ultra crucibles.
 *
 * Ultra crucibles are even more difficult to steer than normal crucibles. Not only do they have trouble going in a
 * straight line, but they also have trouble turning!
 *
 * Once an ultra crucible starts moving in a direction, it needs to move a minimum of four blocks in that direction
 * before it can turn (or even before it can stop at the end). However, it will eventually start to get wobbly: an ultra
 * crucible can move a maximum of ten consecutive blocks without turning.
 *
 * In the above example, an ultra crucible could follow this path to minimize heat loss:
 *
 *     2>>>>>>>>1323
 *     32154535v5623
 *     32552456v4254
 *     34465858v5452
 *     45466578v>>>>
 *     143859879845v
 *     445787698776v
 *     363787797965v
 *     465496798688v
 *     456467998645v
 *     122468686556v
 *     254654888773v
 *     432267465553v
 *
 * In the above example, an ultra crucible would incur the minimum possible heat loss of 94.
 *
 * Here's another example:
 *
 *     111111111111
 *     999999999991
 *     999999999991
 *     999999999991
 *     999999999991
 *
 * Sadly, an ultra crucible would need to take an unfortunate path like this one:
 *
 *     1>>>>>>>1111
 *     9999999v9991
 *     9999999v9991
 *     9999999v9991
 *     9999999v>>>>
 *
 * This route causes the ultra crucible to incur the minimum possible heat loss of 71.
 *
 * Directing the ultra crucible from the lava pool to the machine parts factory, what is the least heat loss it can
 * incur?
 */
package io.ysakhno.adventofcode2023.day17

import io.ysakhno.adventofcode2023.day17.Direction.DOWN
import io.ysakhno.adventofcode2023.day17.Direction.LEFT
import io.ysakhno.adventofcode2023.day17.Direction.RIGHT
import io.ysakhno.adventofcode2023.day17.Direction.UP
import io.ysakhno.adventofcode2023.util.ProblemInput
import io.ysakhno.adventofcode2023.util.println
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.PriorityQueue

private val problemInput = object : ProblemInput {}

private enum class Direction { UP, DOWN, LEFT, RIGHT }

private data class Position(
    val x: Int,
    val y: Int,
    val direction: Direction,
    val straightLineLength: Int = 0,
) : Comparable<Position> {
    var totalLoss: Int = 0
    var prevPos: Position? = null
    val possibleDirections: Collection<Direction>
        get() = if (straightLineLength >= 3) when (direction) {
            UP, DOWN -> listOf(RIGHT, LEFT)
            LEFT, RIGHT -> listOf(UP, DOWN)
        } else when (direction) {
            UP -> listOf(direction, RIGHT, LEFT)
            DOWN -> listOf(direction, RIGHT, LEFT)
            LEFT -> listOf(direction, UP, DOWN)
            RIGHT -> listOf(direction, UP, DOWN)
        }
    val possibleUltraDirections: Collection<Direction>
        get() = if (straightLineLength >= 10) when (direction) {
            UP, DOWN -> listOf(RIGHT, LEFT)
            LEFT, RIGHT -> listOf(UP, DOWN)
        } else if (straightLineLength >= 4) when (direction) {
            UP -> listOf(direction, RIGHT, LEFT)
            DOWN -> listOf(direction, RIGHT, LEFT)
            LEFT -> listOf(direction, UP, DOWN)
            RIGHT -> listOf(direction, UP, DOWN)
        } else listOf(direction)

    override fun compareTo(other: Position) = totalLoss.compareTo(other.totalLoss)

    fun move(dir: Direction) = when (dir) {
        UP -> copy(y = y - 1, direction = dir, straightLineLength = straightLineLength + 1)
        DOWN -> copy(y = y + 1, direction = dir, straightLineLength = straightLineLength + 1)
        LEFT -> copy(x = x - 1, direction = dir, straightLineLength = straightLineLength + 1)
        RIGHT -> copy(x = x + 1, direction = dir, straightLineLength = straightLineLength + 1)
    }
        .let { if (dir != direction) it.copy(straightLineLength = 1) else it }
        .also {
            it.totalLoss = totalLoss
            it.prevPos = this
        }
}

private fun Position.constructPath() = generateSequence(this, Position::prevPos).toList().reversed()

private const val HUGE_LOSS = Int.MAX_VALUE / 2

private fun List<List<Int>>.bordered() = listOf(List(size + 2) { HUGE_LOSS })
    .let { extraRow -> extraRow + map { listOf(HUGE_LOSS) + it + listOf(HUGE_LOSS) } + extraRow }

private fun List<List<Int>>.findShortestPath(
    starts: List<Position>,
    minStraightLineLength: Int = 1,
    nextDirections: Position.() -> Collection<Direction> = { possibleDirections },
): List<Position> {
    val targetX = first().lastIndex - 1
    val targetY = lastIndex - 1

    val queue = PriorityQueue<Position>().apply { addAll(starts) }
    val visited = mutableSetOf<Position>()

    while (queue.isNotEmpty()) {
        val cur = queue.poll()

        if (cur in visited) continue
        if (cur.x == targetX && cur.y == targetY && cur.straightLineLength >= minStraightLineLength) {
            return cur.constructPath()
        }

        visited += cur

        cur.nextDirections()
            .map(cur::move)
            .map { it.apply { totalLoss += this@findShortestPath[it.y][it.x] } }
            .let(queue::addAll)
    }

    return emptyList()
}

private fun part1(input: List<String>) = input.map { it.map(Char::digitToInt) }
    .bordered()
    .findShortestPath(listOf(Position(1, 1, RIGHT), Position(1, 1, DOWN)))
    .lastOrNull()
    ?.totalLoss ?: -1

private fun part2(input: List<String>) = input.map { it.map(Char::digitToInt) }
    .bordered()
    .findShortestPath(listOf(Position(1, 1, RIGHT), Position(1, 1, DOWN)), 4) { possibleUltraDirections }
    .lastOrNull()
    ?.totalLoss ?: -1

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(102, part1(testInput), "Part one (sample input)")
    assertEquals(94, part2(testInput), "Part two (sample input)")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
