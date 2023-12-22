/*
 * --- Day 22: Sand Slabs ---
 *
 * The Elves responsible for water filtering operations took a snapshot of the bricks while they were still falling
 * (your puzzle input) which should let you work out which bricks are safe to disintegrate. For example:
 *
 *     1,0,1~1,2,1
 *     0,0,2~2,0,2
 *     0,2,3~2,2,3
 *     0,0,4~0,2,4
 *     2,0,5~2,2,5
 *     0,1,6~2,1,6
 *     1,1,8~1,1,9
 *
 * Each line of text in the snapshot represents the position of a single brick at the time the snapshot was taken. The
 * position is given as two x,y,z coordinates - one for each end of the brick - separated by a tilde (~). Each brick is
 * made up of a single straight line of cubes, and the Elves were even careful to choose a time for the snapshot that
 * had all free-falling bricks at integer positions above the ground, so the whole snapshot is aligned to a
 * three-dimensional cube grid.
 *
 * A line like 2,2,2~2,2,2 means that both ends of the brick are at the same coordinate - in other words, that the brick
 * is a single cube.
 *
 * Lines like 0,0,10~1,0,10 or 0,0,10~0,1,10 both represent bricks that are two cubes in volume, both oriented
 * horizontally. The first brick extends in the x direction, while the second brick extends in the y direction.
 *
 * A line like 0,0,1~0,0,10 represents a ten-cube brick which is oriented vertically. One end of the brick is the cube
 * located at 0,0,1, while the other end of the brick is located directly above it at 0,0,10.
 *
 * The ground is at z=0 and is perfectly flat; the lowest z value a brick can have is therefore 1. So, 5,5,1~5,6,1 and
 * 0,2,1~0,2,5 are both resting on the ground, but 3,3,2~3,3,3 was above the ground at the time of the snapshot.
 *
 * Because the snapshot was taken while the bricks were still falling, some bricks will still be in the air; you'll need
 * to start by figuring out where they will end up. Bricks are magically stabilized, so they never rotate, even in weird
 * situations like where a long horizontal brick is only supported on one end. Two bricks cannot occupy the same
 * position, so a falling brick will come to rest upon the first other brick it encounters.
 *
 * Here is the same example again, this time with each brick given a letter so it can be marked in diagrams:
 *
 *     1,0,1~1,2,1   <- A
 *     0,0,2~2,0,2   <- B
 *     0,2,3~2,2,3   <- C
 *     0,0,4~0,2,4   <- D
 *     2,0,5~2,2,5   <- E
 *     0,1,6~2,1,6   <- F
 *     1,1,8~1,1,9   <- G
 *
 * At the time of the snapshot, from the side so the x-axis goes left to right, these bricks are arranged like this:
 *
 *      x
 *     012
 *     .G. 9
 *     .G. 8
 *     ... 7
 *     FFF 6
 *     ..E 5 z
 *     D.. 4
 *     CCC 3
 *     BBB 2
 *     .A. 1
 *     --- 0
 *
 * Rotating the perspective 90 degrees so the y-axis now goes left to right, the same bricks are arranged like this:
 *
 *      y
 *     012
 *     .G. 9
 *     .G. 8
 *     ... 7
 *     .F. 6
 *     EEE 5 z
 *     DDD 4
 *     ..C 3
 *     B.. 2
 *     AAA 1
 *     --- 0
 *
 * Once all the bricks fall downward as far as they can go, the stack looks like this, where ? means bricks are hidden
 * behind other bricks at that location:
 *
 *      x
 *     012
 *     .G. 6
 *     .G. 5
 *     FFF 4
 *     D.E 3 z
 *     ??? 2
 *     .A. 1
 *     --- 0
 *
 * Again from the side:
 *
 *      y
 *     012
 *     .G. 6
 *     .G. 5
 *     .F. 4
 *     ??? 3 z
 *     B.C 2
 *     AAA 1
 *     --- 0
 *
 * Now that all bricks have settled, it becomes easier to tell, which bricks are supporting which other bricks:
 *
 * - Brick A is the only brick supporting bricks B and C.
 * - Brick B is one of two bricks supporting brick D and brick E.
 * - Brick C is the other brick supporting brick D and brick E.
 * - Brick D supports brick F.
 * - Brick E also supports brick F.
 * - Brick F supports brick G.
 * - Brick G isn't supporting any bricks.
 *
 * Your first task is to figure out which bricks are safe to disintegrate. A brick can be safely disintegrated if, after
 * removing it, no other bricks would fall further directly downward. Don't actually disintegrate any bricks - just
 * determine what would happen if, for each brick, only that brick were disintegrated. Bricks can be disintegrated even
 * if they're completely surrounded by other bricks; you can squeeze between bricks if you need to.
 *
 * In this example, the bricks can be disintegrated as follows:
 *
 * - Brick A cannot be disintegrated safely; if it were disintegrated, bricks B and C would both fall.
 * - Brick B can be disintegrated; the bricks above it (D and E) would still be supported by brick C.
 * - Brick C can be disintegrated; the bricks above it (D and E) would still be supported by brick B.
 * - Brick D can be disintegrated; the brick above it (F) would still be supported by brick E.
 * - Brick E can be disintegrated; the brick above it (F) would still be supported by brick D.
 * - Brick F cannot be disintegrated; the brick above it (G) would fall.
 * - Brick G can be disintegrated; it does not support any other bricks.
 *
 * So, in this example, 5 bricks can be safely disintegrated.
 *
 * Figure how the blocks will settle based on the snapshot. Once they've settled, consider disintegrating a single
 * brick; how many bricks could be safely chosen as the one to get disintegrated?
 *
 * --- Part Two ---
 *
 * Disintegrating bricks one at a time isn't going to be fast enough. While it might sound dangerous, what you really
 * need is a chain reaction.
 *
 * You'll need to figure out the best brick to disintegrate. For each brick, determine how many other bricks would fall
 * if that brick were disintegrated.
 *
 * Using the same example as above:
 *
 * - Disintegrating brick A would cause all 6 other bricks to fall.
 * - Disintegrating brick F would cause only 1 other brick, G, to fall.
 *
 * Disintegrating any other brick would cause no other bricks to fall. So, in this example, the sum of the number of
 * other bricks that would fall as a result of disintegrating each brick is 7.
 *
 * For each brick, determine how many other bricks would fall if that brick were disintegrated. What is the sum of the
 * number of other bricks that would fall?
 */
package io.ysakhno.adventofcode2023.day22

import io.ysakhno.adventofcode2023.util.ProblemInput
import io.ysakhno.adventofcode2023.util.allInts
import io.ysakhno.adventofcode2023.util.println
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.LinkedList

private val problemInput = object : ProblemInput {}

private data class Brick(
    val x1: Int,
    val y1: Int,
    val z1: Int,
    val x2: Int,
    val y2: Int,
    val z2: Int,
) : Comparable<Brick> {
    var supported: List<Brick> = emptyList()
    override fun compareTo(other: Brick) = z1.compareTo(other.z1)
}

private fun Brick.moveDown(distance: Int) = copy(z1 = z1 - distance, z2 = z2 - distance)

private fun Brick.distanceToFall(field: List<IntArray>) =
    z1 - (y1..y2).maxOf { y -> (x1..x2).maxOf { x-> field[y][x] } } - 1

private fun Brick.isSupportedBy(brick: Brick) =
    z1 == brick.z2 + 1 && x1 <= brick.x2 && x2 >= brick.x1 && y1 <= brick.y2 && y2 >= brick.y1

private fun Brick.countReachableWithout(brick: Brick): Int {
    val queue = LinkedList<Brick>().apply { add(this@countReachableWithout) }
    val visited = mutableSetOf(brick)
    var count = 0

    while (queue.isNotEmpty()) {
        val current = queue.poll()
        if (!visited.add(current)) continue
        queue.addAll(current.supported)
        ++count
    }

    return count
}

private operator fun <E> List<E>.component6() = this[5]

private fun parseBrick(line: String) =
    line.allInts().toList().let { (x1, y1, z1, x2, y2, z2) -> Brick(x1, y1, z1, x2, y2, z2) }

private fun List<IntArray>.update(brick: Brick) {
    for (y in brick.y1..brick.y2) {
        for (x in brick.x1..brick.x2) {
            this[y][x] = brick.z2
        }
    }
}

private fun List<Brick>.supportingField(): List<IntArray> {
    var minX = Int.MAX_VALUE
    var minY = Int.MAX_VALUE
    var maxX = Int.MIN_VALUE
    var maxY = Int.MIN_VALUE

    forEach { brick ->
        minX = minOf(minX, brick.x1)
        minY = minOf(minY, brick.y1)
        maxX = maxOf(maxX, brick.x2)
        maxY = maxOf(maxY, brick.y2)
    }

    return List(maxY + 1) { IntArray(maxX + 1) }
}

private fun List<Brick>.disintegratable() = this - foldRightIndexed(emptyList<List<Brick>>()) { idx, brick, acc ->
    acc + listOf(subList(0, idx).filter(brick::isSupportedBy))
}.filter { it.size == 1 }.flatten().toSet()

private fun part1(input: List<String>): Int {
    val bricks = input.map(::parseBrick).sorted()
    val field = bricks.supportingField()

    return bricks.map { it.distanceToFall(field).let(it::moveDown).also(field::update) }
        .sorted()
        .disintegratable()
        .size
}

private fun List<Brick>.appendFieldAsBrick(sizeX: Int, sizeY: Int) = this + Brick(0, 0, 0, sizeX, sizeY, 0)

private val List<Brick>.numOfBricksThatWouldFall: Int
    get() {
        val graph = foldIndexed(mutableMapOf<Brick, List<Brick>>()) { idx, acc, brick ->
            acc[brick] = subList(idx + 1, size).filter { it.isSupportedBy(brick) }
            acc
        }.map { (brick, supported) -> brick.also { it.supported = supported } }
        val field = graph.first()

        return graph.drop(1).sumOf { brick -> graph.lastIndex - field.countReachableWithout(brick) }
    }

private fun part2(input: List<String>): Int {
    val bricks = input.map(::parseBrick).sorted()
    val field = bricks.supportingField()

    return bricks.map { it.distanceToFall(field).let(it::moveDown).also(field::update) }
        .appendFieldAsBrick(field.first().size + 1, field.size + 1)
        .sorted()
        .numOfBricksThatWouldFall
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(5, part1(testInput), "Part one (sample input)")
    assertEquals(7, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
