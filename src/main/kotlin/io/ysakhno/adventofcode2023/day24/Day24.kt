/*
 * --- Day 24: Never Tell Me The Odds ---
 *
 * You make a note of each hailstone's position and velocity (your puzzle input). For example:
 *
 *     19, 13, 30 @ -2,  1, -2
 *     18, 19, 22 @ -1, -1, -2
 *     20, 25, 34 @ -2, -2, -4
 *     12, 31, 28 @ -1, -2, -1
 *     20, 19, 15 @  1, -5, -3
 *
 * Each line of text corresponds to the position and velocity of a single hailstone. The positions indicate where the
 * hailstones are right now (at time 0). The velocities are constant and indicate exactly how far each hailstone will
 * move in one nanosecond.
 *
 * Each line of text uses the format px py pz @ vx vy vz. For instance, the hailstone specified by 20, 19, 15 @ 1, -5,
 * -3 has initial X position 20, Y position 19, Z position 15, X velocity 1, Y velocity -5, and Z velocity -3. After one
 * nanosecond, the hailstone would be at 21, 14, 12.
 *
 * Perhaps you won't have to do anything. How likely are the hailstones to collide with each other and smash into tiny
 * ice crystals?
 *
 * To estimate this, consider only the X and Y axes; ignore the Z axis. Looking forward in time, how many of the
 * hailstones' paths will intersect within a test area? (The hailstones themselves don't have to collide, just test for
 * intersections between the paths they will trace.)
 *
 * In this example, look for intersections that happen with an X and Y position each at least 7 and at most 27; in your
 * actual data, you'll need to check a much larger test area. Comparing all pairs of hailstones' future paths produces
 * the following results:
 *
 *     Hailstone A: 19, 13, 30 @ -2, 1, -2
 *     Hailstone B: 18, 19, 22 @ -1, -1, -2
 *     Hailstones' paths will cross inside the test area (at x=14.333, y=15.333).
 *
 *     Hailstone A: 19, 13, 30 @ -2, 1, -2
 *     Hailstone B: 20, 25, 34 @ -2, -2, -4
 *     Hailstones' paths will cross inside the test area (at x=11.667, y=16.667).
 *
 *     Hailstone A: 19, 13, 30 @ -2, 1, -2
 *     Hailstone B: 12, 31, 28 @ -1, -2, -1
 *     Hailstones' paths will cross outside the test area (at x=6.2, y=19.4).
 *
 *     Hailstone A: 19, 13, 30 @ -2, 1, -2
 *     Hailstone B: 20, 19, 15 @ 1, -5, -3
 *     Hailstones' paths crossed in the past for hailstone A.
 *
 *     Hailstone A: 18, 19, 22 @ -1, -1, -2
 *     Hailstone B: 20, 25, 34 @ -2, -2, -4
 *     Hailstones' paths are parallel; they never intersect.
 *
 *     Hailstone A: 18, 19, 22 @ -1, -1, -2
 *     Hailstone B: 12, 31, 28 @ -1, -2, -1
 *     Hailstones' paths will cross outside the test area (at x=-6, y=-5).
 *
 *     Hailstone A: 18, 19, 22 @ -1, -1, -2
 *     Hailstone B: 20, 19, 15 @ 1, -5, -3
 *     Hailstones' paths crossed in the past for both hailstones.
 *
 *     Hailstone A: 20, 25, 34 @ -2, -2, -4
 *     Hailstone B: 12, 31, 28 @ -1, -2, -1
 *     Hailstones' paths will cross outside the test area (at x=-2, y=3).
 *
 *     Hailstone A: 20, 25, 34 @ -2, -2, -4
 *     Hailstone B: 20, 19, 15 @ 1, -5, -3
 *     Hailstones' paths crossed in the past for hailstone B.
 *
 *     Hailstone A: 12, 31, 28 @ -1, -2, -1
 *     Hailstone B: 20, 19, 15 @ 1, -5, -3
 *     Hailstones' paths crossed in the past for both hailstones.
 *
 * So, in this example, 2 hailstones' future paths cross inside the boundaries of the test area.
 *
 * However, you'll need to search a much larger test area if you want to see if any hailstones might collide. Look for
 * intersections that happen with an X and Y position each at least 200000000000000 and at most 400000000000000.
 * Disregard the Z axis entirely.
 *
 * Considering only the X and Y axes, check all pairs of hailstones' future paths for intersections. How many of these
 * intersections occur within the test area?
 *
 * --- Part Two ---
 *
 * Upon further analysis, it doesn't seem like any hailstones will naturally collide. It's up to you to fix that!
 *
 * You find a rock on the ground nearby. While it seems extremely unlikely, if you throw it just right, you should be
 * able to hit every hailstone in a single throw!
 *
 * You can use the probably-magical winds to reach any integer position you like and to propel the rock at any integer
 * velocity. Now including the Z axis in your calculations, if you throw the rock at time 0, where do you need to be so
 * that the rock perfectly collides with every hailstone? Due to probably-magical inertia, the rock won't slow down or
 * change direction when it collides with a hailstone.
 *
 * In the example above, you can achieve this by moving to position 24, 13, 10 and throwing the rock at velocity -3, 1,
 * 2. If you do this, you will hit every hailstone as follows:
 *
 *     Hailstone: 19, 13, 30 @ -2, 1, -2
 *     Collision time: 5
 *     Collision position: 9, 18, 20
 *
 *     Hailstone: 18, 19, 22 @ -1, -1, -2
 *     Collision time: 3
 *     Collision position: 15, 16, 16
 *
 *     Hailstone: 20, 25, 34 @ -2, -2, -4
 *     Collision time: 4
 *     Collision position: 12, 17, 18
 *
 *     Hailstone: 12, 31, 28 @ -1, -2, -1
 *     Collision time: 6
 *     Collision position: 6, 19, 22
 *
 *     Hailstone: 20, 19, 15 @ 1, -5, -3
 *     Collision time: 1
 *     Collision position: 21, 14, 12
 *
 * Above, each hailstone is identified by its initial position and its velocity. Then, the time and position of that
 * hailstone's collision with your rock are given.
 *
 * After 1 nanosecond, the rock has exactly the same position as one of the hailstones, obliterating it into ice dust!
 * Another hailstone is smashed to bits two nanoseconds after that. After a total of 6 nanoseconds, all of the
 * hailstones have been destroyed.
 *
 * So, at time 0, the rock needs to be at X position 24, Y position 13, and Z position 10. Adding these three
 * coordinates together produces 47. (Don't add any coordinates from the rock's velocity.)
 *
 * Determine the exact position and velocity the rock needs to have at time 0 so that it perfectly collides with every
 * hailstone. What do you get if you add up the X, Y, and Z coordinates of that initial position?
 */
package io.ysakhno.adventofcode2023.day24

import com.microsoft.z3.Context
import com.microsoft.z3.IntNum
import io.ysakhno.adventofcode2023.util.ProblemInput
import io.ysakhno.adventofcode2023.util.allLongs
import io.ysakhno.adventofcode2023.util.println
import kotlin.math.abs
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private data class Vector2D(val x: Double, val y: Double)

private fun Vector2D.crossProduct(other: Vector2D) = x * other.y - y * other.x

private operator fun Vector3D.unaryMinus() = Vector3D(x = -x, y = -y, z = -z)

private operator fun Vector2D.minus(other: Vector2D) = Vector2D(x = x - other.x, y = y - other.y)

private operator fun Vector2D.plus(other: Vector2D) = Vector2D(x = x + other.x, y = y + other.y)

private operator fun Vector2D.times(other: Vector2D) = crossProduct(other)

private data class Vector3D(val x: Long, val y: Long, val z: Long) {
    val isEmpty get() = x == 0L && y == 0L && z == 0L
    infix fun isParallelTo(other: Vector3D) = if (isEmpty || other.isEmpty) false else x(other).isEmpty
}

private fun Vector3D.dotProduct(other: Vector3D) = x * other.x + y * other.y + z * other.z

private fun Vector3D.crossProduct(other: Vector3D): Vector3D = Vector3D(
    x = y * other.z - z * other.y,
    y = z * other.x - x * other.z,
    z = x * other.y - y * other.x,
)

private operator fun Vector3D.plus(other: Vector3D): Vector3D {
    return Vector3D(x = x + other.x, y = y + other.y, z = z + other.z)
}

private operator fun Vector3D.minus(other: Vector3D) = Vector3D(x = x - other.x, y = y - other.y, z = z - other.z)

private operator fun Vector3D.times(other: Vector3D) = dotProduct(other)

private operator fun Vector3D.times(scale: Long) = scale.let { Vector3D(x = x * it, y = y * it, z = z * it) }

private operator fun Long.times(vector: Vector3D) = vector * this

private infix fun Vector3D.x(other: Vector3D) = crossProduct(other)

private data class Ray2D(val start: Vector2D, val direction: Vector2D)

private operator fun Double.times(vector: Vector2D) = Vector2D(x = this * vector.x, y = this * vector.y)

private fun Ray2D.intersect(other: Ray2D): Vector2D? {
    val denominator = direction * other.direction
    if (abs(denominator) < 1.0e-6) {
        return null
    }
    val t = (other.start - start) * other.direction / denominator
    val u = (other.start - start) * direction / denominator
    return if (t >= 0.0 && u >= 0.0) start + t * direction else null
}

private data class Ray3D(val start: Vector3D, val direction: Vector3D) {
    infix fun isParallelTo(other: Ray3D) = direction isParallelTo other.direction
}

private data class Hailstone(val x: Long, val y: Long, val z: Long, val vx: Long, val vy: Long, val vz: Long) {
    val velocity2D get() = Vector2D(vx.toDouble(), vy.toDouble())
    val velocity3D get() = Vector3D(vx, vy, vz)
    val ray2D get() = Ray2D(start = Vector2D(x.toDouble(), y.toDouble()), direction = velocity2D)
    val ray3D get() = Ray3D(start = Vector3D(x, y, z), direction = velocity3D)
}

private operator fun <E> List<E>.component6() = this[5]

private fun List<String>.hailstones() =
    map { it.allLongs().toList() }.map { (x, y, z, vx, vy, vz) -> Hailstone(x, y, z, vx, vy, vz) }

private fun part1(input: List<String>, range: ClosedRange<Double>): Int {
    val stones = input.hailstones()
    return stones.flatMapIndexed { idx1, stone1 ->
        stones.drop(idx1 + 1).map { stone2 -> stone1 to stone2 }
    }.mapNotNull { (stone1, stone2) -> stone1.ray2D.intersect(stone2.ray2D) }
        .count { pt -> pt.x in range && pt.y in range }
}

private fun part2(input: List<String>): Long {
    val stones = input.hailstones()
    val nonParallelStones = stones.filterNot { stones.first().ray3D isParallelTo it.ray3D }

    return Context().use { ctx ->
        val x = ctx.mkIntConst("x")
        val y = ctx.mkIntConst("y")
        val z = ctx.mkIntConst("z")
        val vx = ctx.mkIntConst("vx")
        val vy = ctx.mkIntConst("vy")
        val vz = ctx.mkIntConst("vz")

        val solver = ctx.mkSolver()

        for (i in 1..3) {
            val t = ctx.mkIntConst("t$i")
            val stone = nonParallelStones[i]

            // x + t * vx == stone_x + t * stone_vx
            solver.add(
                ctx.mkEq(
                    ctx.mkAdd(x, ctx.mkMul(t, vx)),
                    ctx.mkAdd(ctx.mkInt(stone.x), ctx.mkMul(t, ctx.mkInt(stone.vx))),
                ),
            )
            // y + t * vy == stone_y + t * stone_vy
            solver.add(
                ctx.mkEq(
                    ctx.mkAdd(y, ctx.mkMul(t, vy)),
                    ctx.mkAdd(ctx.mkInt(stone.y), ctx.mkMul(t, ctx.mkInt(stone.vy))),
                ),
            )
            // z + t * vz == stone_z + t * stone_vz
            solver.add(
                ctx.mkEq(
                    ctx.mkAdd(z, ctx.mkMul(t, vz)),
                    ctx.mkAdd(ctx.mkInt(stone.z), ctx.mkMul(t, ctx.mkInt(stone.vz))),
                ),
            )
        }

        solver.check()
        (solver.model.evaluate(ctx.mkAdd(x, y, z), false) as IntNum).int64
    }
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(2, part1(testInput, 7.0..27.0), "Part one (sample input)")
    assertEquals(47, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input, 200_000_000_000_000.0..400_000_000_000_000.0).println()
    part2(input).println()
}
