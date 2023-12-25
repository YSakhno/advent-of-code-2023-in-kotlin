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

import io.ysakhno.adventofcode2023.util.ProblemInput
import io.ysakhno.adventofcode2023.util.allLongs
import io.ysakhno.adventofcode2023.util.println
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.math.abs
import kotlin.math.sqrt

private val problemInput = object : ProblemInput {}

data class Vector2D(val x: Double, val y: Double)

fun Vector2D.crossProduct(other: Vector2D) = x * other.y - y * other.x

private operator fun Vector3D.unaryMinus() = Vector3D(x = -x, y = -y, z = -z)

private operator fun Vector2D.minus(other: Vector2D) = Vector2D(x = x - other.x, y = y - other.y)

private operator fun Vector2D.plus(other: Vector2D) = Vector2D(x = x + other.x, y = y + other.y)

private operator fun Vector2D.times(other: Vector2D) = crossProduct(other)

data class Vector3D(val x: Double, val y: Double, val z: Double) {
    val isEmpty get() = x eq 0.0 && y eq 0.0 && z eq 0.0
    val magnitude get() = sqrt(x * x + y * y + z * z)
}

fun Vector3D.normalized() = magnitude.let { if (it ne 0.0) Vector3D(x = x / it, y = y / it, z = z / it) else this }

fun Vector3D.dotProduct(other: Vector3D) = x * other.x + y * other.y + z * other.z

fun Vector3D.crossProduct(other: Vector3D): Vector3D = Vector3D(
    x = y * other.z - z * other.y,
    y = z * other.x - x * other.z,
    z = x * other.y - y * other.x,
)

private operator fun Vector3D.plus(other: Vector3D): Vector3D {
    return Vector3D(x = x + other.x, y = y + other.y, z = z + other.z)
}

private operator fun Vector3D.minus(other: Vector3D) = Vector3D(x = x - other.x, y = y - other.y, z = z - other.z)

private operator fun Vector3D.times(other: Vector3D) = dotProduct(other)

private operator fun Vector3D.times(scale: Double) = Vector3D(x = x * scale, y = y * scale, z = z * scale)

private operator fun Double.times(vector: Vector3D) = vector * this

private infix fun Vector3D.x(other: Vector3D) = crossProduct(other)

data class Ray2D(val start: Vector2D, val direction: Vector2D)

private operator fun Double.times(vector: Vector2D) = Vector2D(x = this * vector.x, y = this * vector.y)

fun Ray2D.intersect(other: Ray2D): Vector2D? {
    val denominator = direction * other.direction
    if (abs(denominator) < 1.0e-6) {
        return null
    }
    val t = (other.start - start) * other.direction / denominator
    val u = (other.start - start) * direction / denominator
    return if (t >= 0.0 && u >= 0.0) start + t * direction else null
}

data class Ray3D(val start: Vector3D, val direction: Vector3D)

infix fun Double.eq(other: Double) = abs(other - this) < 1.0e-6

infix fun Double.ne(other: Double) = abs(other - this) >= 1.0e-6

fun Ray3D.intersect(b: Ray3D): Vector3D? {
    val dc = b.start - start
    val cp = direction x b.direction

    if (dc * cp ne 0.0) return null // lines are not coplanar

    val t = ((dc x b.direction) * cp) / (cp * cp)
    return start + direction * t
}

data class Hailstone(
    val x: Double,
    val y: Double,
    val z: Double,
    val vx: Double,
    val vy: Double,
    val vz: Double
) {
    val velocity2D get() = Vector2D(vx, vy)
    val velocity3D get() = Vector3D(vx, vy, vz)
    val ray2D get() = Ray2D(start = Vector2D(x, y), direction = velocity2D)
    val ray3D get() = Ray3D(start = Vector3D(x, y, z), direction = velocity3D)
}

fun Hailstone.nextPosition() = positionAt(1.0)

fun Hailstone.positionAt(t: Double) = Vector3D(x = x + vx * t, y = y + vy * t, z = z + vz * t)

fun isCollinear(a: Vector3D, b: Vector3D, c: Vector3D): Boolean {
    val ab = b - a
    val bc = c - b
    val product = ab x bc
    return product.isEmpty
}

private operator fun <E> List<E>.component6() = this[5]

private fun part1(input: List<String>, range: ClosedRange<Double>): Int {
    val stones = input.map { it.allLongs().map(Long::toDouble).toList() }.map { (x, y, z, vx, vy, vz) -> Hailstone(x, y, z, vx, vy, vz) }
    return stones.flatMapIndexed { idx1, stone1 ->
        stones.drop(idx1 + 1).map { stone2 -> stone1 to stone2 }
    }.mapNotNull { (stone1, stone2) -> stone1.ray2D.intersect(stone2.ray2D) }
        .count { pt -> pt.x in range && pt.y in range }
}

fun infinitePairwiseSequence() = sequence {
    var i = 1
    while (true) {
        for (j in 1..i) {
            yield(j to i + 1 - j)
        }
        ++i
    }
}

fun findIntersectingRay(line1: Ray3D, line2: Ray3D, line3: Ray3D): Ray3D {
    val n1 = line1.direction x line2.direction // normal vector for plane 1
    val n2 = (n1 x (line1.start - line2.start)).normalized() // normal vector for plane 2
    val p3 = (n1 * n2) * line3.start - (n1 * line3.direction) * line1.start - (n2 * line3.direction) * line2.start
    return Ray3D(p3, (line1.direction x line2.direction).normalized()) // direction is the normal vector for plane 1 x plane 2
}

fun findIntersectingLine(line1: Ray3D, line2: Ray3D, line3: Ray3D): Ray3D {
    val crossProduct1 = line2.direction x line3.direction
    val crossProduct2 = line1.direction x line3.direction
    val crossProduct3 = line1.direction x line2.direction

    val numerator = Vector3D(line1.start * crossProduct1, line2.start * crossProduct2, line3.start * crossProduct3)
    val denominator = (line1.direction * crossProduct1)
        .let { if (it eq 0.0) line2.direction * crossProduct2 else it }
        .let { if (it eq 0.0) line3.direction * crossProduct3 else it }

    val intersectionPoint = Vector3D(numerator.x / denominator, numerator.y / denominator, numerator.z / denominator)

    return Ray3D(intersectionPoint, crossProduct1 x crossProduct2)
}

private fun part2(input: List<String>): Int {
    val stones = input.map { it.allLongs().map(Long::toDouble).toList() }.map { (x, y, z, vx, vy, vz) -> Hailstone(x, y, z, vx, vy, vz) }
    val (stone1, stone2, stone3) = stones

    findIntersectingLine(stone1.ray3D, stone2.ray3D, stone3.ray3D).let { ray ->
        ray.intersect(stone1.ray3D).println()
        stone1.ray3D.intersect(ray).println()
    }

//    infinitePairwiseSequence().filter { it.first != it.second }.take(100).forEach { (t1, t2) ->
//        val pt1 = stone1.positionAt(t1.toDouble())
//        val pt2 = stone2.positionAt(t2.toDouble())
//        val diff = pt2 - pt1
//        val dist = diff.magnitude
//
//        if (truncate(dist) eq dist) {
//            val start = pt1 - diff.normalized() * t1.toDouble()
//        }
//
//    }
//
//    outer@for (t1 in 0..<10_000) {
//        val pt1 = stone1.positionAt(t1.toDouble())
//        for (t2 in 0..<10_000) {
//            val pt2 = stone2.positionAt(t2.toDouble())
//            for (t3 in 0..<10_000) {
//                val pt3 = stone3.positionAt(t3.toDouble())
//
//                if (isCollinear(pt1, pt2, pt3)) {
//                    println("Collinear at $t1, $t2, $t3")
//                    break@outer
//                }
//            }
//        }
//    }

    return input.size
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(2, part1(testInput, 7.0..27.0), "Part one (sample input)")
    assertEquals(5, part2(testInput), "Part two (sample input)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input, 200_000_000_000_000.0..400_000_000_000_000.0).println()
    part2(input).println()
}
