/*
 * --- Day 10: Pipe Maze ---
 *
 * Puzzle input is a formed by pipes and a starting point along one of the pipes. The pipes are arranged in a
 * two-dimensional grid of tiles:
 *
 * - '|' is a vertical pipe connecting north and south.
 * - '-' is a horizontal pipe connecting east and west.
 * - 'L' is a 90-degree bend connecting north and east.
 * - 'J' is a 90-degree bend connecting north and west.
 * - '7' is a 90-degree bend connecting south and west.
 * - 'F' is a 90-degree bend connecting south and east.
 * - '.' is ground; there is no pipe in this tile.
 * - 'S' is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape
 *   the pipe has.
 *
 * Based on the acoustics of the animal's scurrying, you're confident the pipe that contains the animal is one large,
 * continuous loop.
 *
 * For example, here is a square loop of pipe:
 *
 *     .....
 *     .F-7.
 *     .|.|.
 *     .L-J.
 *     .....
 *
 * If the animal had entered this loop in the northwest corner, the sketch would instead look like this:
 *
 *     .....
 *     .S-7.
 *     .|.|.
 *     .L-J.
 *     .....
 *
 * In the above diagram, the S tile is still a 90-degree F bend: you can tell because of how the adjacent pipes connect
 * to it.
 *
 * Unfortunately, there are also many pipes that aren't connected to the loop! This sketch shows the same loop as above:
 *
 *     -L|F7
 *     7S-7|
 *     L|7||
 *     -L-J|
 *     L|-JF
 *
 * In the above diagram, you can still figure out which pipes form the main loop: they're the ones connected to S, pipes
 * those pipes connect to, pipes those pipes connect to, and so on. Every pipe in the main loop connects to its two
 * neighbors (including S, which will have exactly two pipes connecting to it, and which is assumed to connect back to
 * those two pipes).
 *
 * Here is a sketch that contains a slightly more complex main loop:
 *
 *     ..F7.
 *     .FJ|.
 *     SJ.L7
 *     |F--J
 *     LJ...
 *
 * Here's the same example sketch with the extra, non-main-loop pipe tiles also shown:
 *
 *     7-F7-
 *     .FJ|7
 *     SJLL7
 *     |F--J
 *     LJ.LJ
 *
 * If you want to get out ahead of the animal, you should find the tile in the loop that is farthest from the starting
 * position. Because the animal is in the pipe, it doesn't make sense to measure this by direct distance. Instead, you
 * need to find the tile that would take the longest number of steps along the loop to reach from the starting point -
 * regardless of which way around the loop the animal went.
 *
 * In the first example with the square loop:
 *
 *     .....
 *     .S-7.
 *     .|.|.
 *     .L-J.
 *     .....
 *
 * You can count the distance each tile in the loop is from the starting point like this:
 *
 *     .....
 *     .012.
 *     .1.3.
 *     .234.
 *     .....
 *
 * In this example, the farthest point from the start is 4 steps away.
 *
 * Here's the more complex loop again:
 *
 *     ..F7.
 *     .FJ|.
 *     SJ.L7
 *     |F--J
 *     LJ...
 *
 * Here are the distances for each tile on that loop:
 *
 *     ..45.
 *     .236.
 *     01.78
 *     14567
 *     23...
 *
 * Find the single giant loop starting at S. How many steps along the loop does it take to get from the starting
 * position to the point farthest from the starting position?
 *
 * --- Part Two ---
 *
 * You quickly reach the farthest point of the loop, but the animal never emerges. Maybe its nest is within the area
 * enclosed by the loop?
 *
 * To determine whether it's even worth taking the time to search for such a nest, you should calculate how many tiles
 * are contained within the loop. For example:
 *
 *     ...........
 *     .S-------7.
 *     .|F-----7|.
 *     .||.....||.
 *     .||.....||.
 *     .|L-7.F-J|.
 *     .|..|.|..|.
 *     .L--J.L--J.
 *     ...........
 *
 * The above loop encloses merely four tiles - the two pairs of '.' in the southwest and southeast (marked I below). The
 * middle '.' tiles (marked O below) are not in the loop. Here is the same loop again with those regions marked:
 *
 *     ...........
 *     .S-------7.
 *     .|F-----7|.
 *     .||OOOOO||.
 *     .||OOOOO||.
 *     .|L-7OF-J|.
 *     .|II|O|II|.
 *     .L--JOL--J.
 *     .....O.....
 *
 * In fact, there doesn't even need to be a full tile path to the outside for tiles to count as outside the loop -
 * squeezing between pipes is also allowed! Here, I is still within the loop and O is still outside the loop:
 *
 *     ..........
 *     .S------7.
 *     .|F----7|.
 *     .||OOOO||.
 *     .||OOOO||.
 *     .|L-7F-J|.
 *     .|II||II|.
 *     .L--JL--J.
 *     ..........
 *
 * In both of the above examples, 4 tiles are enclosed by the loop.
 *
 * Here's a larger example:
 *
 *     .F----7F7F7F7F-7....
 *     .|F--7||||||||FJ....
 *     .||.FJ||||||||L7....
 *     FJL7L7LJLJ||LJ.L-7..
 *     L--J.L7...LJS7F-7L7.
 *     ....F-J..F7FJ|L7L7L7
 *     ....L7.F7||L7|.L7L7|
 *     .....|FJLJ|FJ|F7|.LJ
 *     ....FJL-7.||.||||...
 *     ....L---J.LJ.LJLJ...
 *
 * The above sketch has many random bits of ground, some of which are in the loop (I) and some of which are outside it
 * (O):
 *
 *     OF----7F7F7F7F-7OOOO
 *     O|F--7||||||||FJOOOO
 *     O||OFJ||||||||L7OOOO
 *     FJL7L7LJLJ||LJIL-7OO
 *     L--JOL7IIILJS7F-7L7O
 *     OOOOF-JIIF7FJ|L7L7L7
 *     OOOOL7IF7||L7|IL7L7|
 *     OOOOO|FJLJ|FJ|F7|OLJ
 *     OOOOFJL-7O||O||||OOO
 *     OOOOL---JOLJOLJLJOOO
 *
 * In this larger example, 8 tiles are enclosed by the loop.
 *
 * Any tile that isn't part of the main loop can count as being enclosed by the loop. Here's another example with many
 * bits of junk pipe lying around that aren't connected to the main loop at all:
 *
 *     FF7FSF7F7F7F7F7F---7
 *     L|LJ||||||||||||F--J
 *     FL-7LJLJ||||||LJL-77
 *     F--JF--7||LJLJ7F7FJ-
 *     L---JF-JLJ.||-FJLJJ7
 *     |F|F-JF---7F7-L7L|7|
 *     |FFJF7L7F-JF7|JL---7
 *     7-L-JL7||F7|L7F-7F7|
 *     L.L7LFJ|||||FJL7||LJ
 *     L7JLJL-JLJLJL--JLJ.L
 *
 * Here are just the tiles that are enclosed by the loop marked with I:
 *
 *     FF7FSF7F7F7F7F7F---7
 *     L|LJ||||||||||||F--J
 *     FL-7LJLJ||||||LJL-77
 *     F--JF--7||LJLJIF7FJ-
 *     L---JF-JLJIIIIFJLJJ7
 *     |F|F-JF---7IIIL7L|7|
 *     |FFJF7L7F-JF7IIL---7
 *     7-L-JL7||F7|L7F-7F7|
 *     L.L7LFJ|||||FJL7||LJ
 *     L7JLJL-JLJLJL--JLJ.L
 *
 * In this last example, 10 tiles are enclosed by the loop.
 *
 * Figure out whether you have time to search for the nest by calculating the area within the loop. How many tiles are
 * enclosed by the loop?
 */

import Pipe.GROUND
import Pipe.HORIZONTAL
import Pipe.INSIDE
import Pipe.NORTH_TO_EAST
import Pipe.NORTH_TO_WEST
import Pipe.SOUTH_TO_EAST
import Pipe.SOUTH_TO_WEST
import Pipe.START
import Pipe.VERTICAL

private val filename = object {}

private enum class Pipe(
    val symbol: Char,
    val boxDrawing: Char,
    val connectRight: Boolean = false,
    val connectDown: Boolean = false,
    val connectLeft: Boolean = false,
    val connectUp: Boolean = false,
) {
    VERTICAL('|', '│', connectDown = true, connectUp = true),
    HORIZONTAL('-', '─', connectRight = true, connectLeft = true),
    NORTH_TO_EAST('L', '└', connectRight = true, connectUp = true),
    NORTH_TO_WEST('J', '┘', connectLeft = true, connectUp = true),
    SOUTH_TO_EAST('F', '┌', connectDown = true, connectRight = true),
    SOUTH_TO_WEST('7', '┐', connectLeft = true, connectDown = true),
    START('S', 'S', connectRight = true, connectDown = true, connectLeft = true, connectUp = true),
    GROUND('.', '.'),
    ANY_PIPE('P', 'P'),
    INSIDE('I', 'I'),
    OUTSIDE('O', 'O'),
}

private val Pipe.verticalConnectionsCount get() = (if (connectDown) 1 else 0) + (if (connectUp) 1 else 0)

private fun Char.toPipe() = Pipe.entries.first { it.symbol == this }

private data class Point(val x: Int, val y: Int)

private typealias Area = List<List<Pipe>>

private val Area.startPos: Point
    get() {
        for (row in indices) {
            this[row].indexOf(START).let { col -> if (col != -1) return Point(col, row) }
        }
        error("No start position found")
    }

private val Area.width get() = first().size

private val Area.height get() = size

private operator fun Area.get(col: Int, row: Int) = this[row][col]
private operator fun Area.get(point: Point) = this[point.x, point.y]

private fun Area.print() {
    for (row in indices) {
        for (col in 0..<width) {
            print(this[col, row].boxDrawing)
        }
        kotlin.io.println()
    }
}

private fun Area.copyWithPipe(pipe: List<Point>): Area {
    val rows = List(height) { ArrayList<Pipe>(width) }

    for (row in rows) {
        for (col in 0..<width) {
            row.add(GROUND)
        }
    }

    for (idx in pipe.indices) {
        val pos = pipe[idx]
        rows[pos.y][pos.x] = this[pos].let { p ->
            if (p == START) {
                val prevPos = if (idx > 0) pipe[idx - 1] else pipe.last()
                val nextPos = if (idx < pipe.size - 1) pipe[idx + 1] else pipe.first()

                when {
                    (prevPos.x == nextPos.x) -> VERTICAL
                    (prevPos.y == nextPos.y) -> HORIZONTAL
                    (prevPos.x < nextPos.x && prevPos.y < nextPos.y) -> SOUTH_TO_WEST
                    (prevPos.x < nextPos.x) -> SOUTH_TO_EAST
                    (prevPos.y < nextPos.y) -> NORTH_TO_WEST
                    else -> NORTH_TO_EAST
                }
            } else p
        }
    }

    return rows.map(ArrayList<Pipe>::toList)
}

private fun Area.copyWithEnclosed(pointsInside: Iterable<Point>): Area {
    val rows = map(List<Pipe>::toMutableList)

    for (pos in pointsInside) {
        rows[pos.y][pos.x] = INSIDE
    }

    return rows.map(MutableList<Pipe>::toList)
}

private fun Area.getConnectedPointsFrom(point: Point): List<Point> {
    val currentPipe = this[point]
    return buildList {
        if (currentPipe.connectRight && point.x < width && get(point.x + 1, point.y).connectLeft) {
            add(Point(point.x + 1, point.y))
        }
        if (currentPipe.connectDown && point.y < height && get(point.x, point.y + 1).connectUp) {
            add(Point(point.x, point.y + 1))
        }
        if (currentPipe.connectLeft && point.x > 0 && get(point.x - 1, point.y).connectRight) {
            add(Point(point.x - 1, point.y))
        }
        if (currentPipe.connectUp && point.y > 0 && get(point.x, point.y - 1).connectDown) {
            add(Point(point.x, point.y - 1))
        }
    }
}

private fun Area.isInside(x: Int, y: Int): Boolean {
    val row = this[y]
    var segmentStartIdx = -1
    var count = 0

    for (col in x + 1..<row.size) {
        val pipe = row[col]
        when (pipe.verticalConnectionsCount) {
            1 -> {
                if (segmentStartIdx != -1) {
                    if (row[segmentStartIdx].connectUp == pipe.connectDown) ++count
                    segmentStartIdx = -1
                } else segmentStartIdx = col
            }
            2 -> ++count
        }
    }

    return count % 2 != 0
}

private fun findLoopedPipe(area: Area): List<Point> {
    val startPos = area.startPos
    val visited = mutableSetOf<Point>()
    val pipe = mutableListOf<Point>()
    var current = startPos

    while (current !in visited) {
        visited += current
        val candidates = area.getConnectedPointsFrom(current)
        val next = candidates.firstOrNull { it !in visited && area[it] != GROUND }

        if (next == null) {
            if (pipe.size > 0 && candidates.any { area[it] == START }) pipe += startPos
            break
        }

        current = next
        pipe += current
    }

    return pipe
}

fun main() {
    fun part1(input: List<String>) = findLoopedPipe(input.map { row -> row.map(Char::toPipe) }).size / 2

    fun part2(input: List<String>): Int {
        val area: Area = input.map { row -> row.map(Char::toPipe) }
        val areaWithPipe = area.copyWithPipe(findLoopedPipe(area))
        val enclosed = mutableListOf<Point>()

        for (y in areaWithPipe.indices) {
            for (x in 0..<areaWithPipe.width) {
                if (areaWithPipe[x, y] == GROUND && areaWithPipe.isInside(x, y)) {
                    enclosed += Point(x, y)
                }
            }
        }

        areaWithPipe.copyWithEnclosed(enclosed).print()

        return enclosed.size
    }

    // Test if implementation meets criteria from the description
    val testInput1 = readInput("${filename.dayNumber}_test")
    val testInput2 = readInput("${filename.dayNumber}_test-2")
    check(part1(testInput1) == 8)
    check(part2(testInput2) == 10)

    val input = readInput(filename.dayNumber)
    part1(input).println()
    part2(input).println()
}
