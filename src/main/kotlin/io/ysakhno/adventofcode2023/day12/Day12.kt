/*
 * --- Day 12: Hot Springs ---
 *
 * In the giant field just outside, the springs are arranged into rows. For each row, the condition records show every
 * spring and whether it is operational (.) or damaged (#). This is the part of the condition records that is itself
 * damaged; for some springs, it is simply unknown (?) whether the spring is operational or damaged.
 *
 * However, the engineer that produced the condition records also duplicated some of this information in a different
 * format! After the list of springs for a given row, the size of each contiguous group of damaged springs is listed in
 * the order those groups appear in the row. This list always accounts for every damaged spring, and each number is the
 * entire size of its contiguous group (that is, groups are always separated by at least one operational spring: ####
 * would always be 4, never 2,2).
 *
 * So, condition records with no unknown spring conditions might look like this:
 *
 *     #.#.### 1,1,3
 *     .#...#....###. 1,1,3
 *     .#.###.#.###### 1,3,1,6
 *     ####.#...#... 4,1,1
 *     #....######..#####. 1,6,5
 *     .###.##....# 3,2,1
 *
 * However, the condition records are partially damaged; some of the springs' conditions are actually unknown (?). For
 * example:
 *
 *     ???.### 1,1,3
 *     .??..??...?##. 1,1,3
 *     ?#?#?#?#?#?#?#? 1,3,1,6
 *     ????.#...#... 4,1,1
 *     ????.######..#####. 1,6,5
 *     ?###???????? 3,2,1
 *
 * Equipped with this information, it is your job to figure out how many different arrangements of operational and
 * broken springs fit the given criteria in each row.
 *
 * In the first line (???.### 1,1,3), there is exactly one way separate groups of one, one, and three broken springs (in
 * that order) can appear in that row: the first three unknown springs must be broken, then operational, then broken
 * (#.#), making the whole row #.#.###.
 *
 * The second line is more interesting: .??..??...?##. 1,1,3 could be a total of four different arrangements. The last ?
 * must always be broken (to satisfy the final contiguous group of three broken springs), and each ?? must hide exactly
 * one of the two broken springs. (Neither ?? could be both broken springs or they would form a single contiguous group
 * of two; if that were true, the numbers afterward would have been 2,3 instead.) Since each ?? can either be #. or .#,
 * there are four possible arrangements of springs.
 *
 * The last line is actually consistent with ten different arrangements! Because the first number is 3, the first and
 * second ? must both be . (if either were #, the first number would have to be 4 or higher). However, the remaining run
 * of unknown spring conditions have many different ways they could hold groups of two and one broken springs:
 *
 *     ?###???????? 3,2,1
 *     .###.##.#...
 *     .###.##..#..
 *     .###.##...#.
 *     .###.##....#
 *     .###..##.#..
 *     .###..##..#.
 *     .###..##...#
 *     .###...##.#.
 *     .###...##..#
 *     .###....##.#
 *
 * In this example, the number of possible arrangements for each row is:
 *
 * - ???.### 1,1,3 - 1 arrangement
 * - .??..??...?##. 1,1,3 - 4 arrangements
 * - ?#?#?#?#?#?#?#? 1,3,1,6 - 1 arrangement
 * - ????.#...#... 4,1,1 - 1 arrangement
 * - ????.######..#####. 1,6,5 - 4 arrangements
 * - ?###???????? 3,2,1 - 10 arrangements
 *
 * Adding all possible arrangement counts together produces a total of 21 arrangements.
 *
 * For each row, count all different arrangements of operational and broken springs that meet the given criteria. What
 * is the sum of those counts?
 *
 * --- Part Two ---
 *
 * As you look out at the field of springs, you feel like there are way more springs than the condition records list.
 * When you examine the records, you discover that they were actually folded up this whole time!
 *
 * To unfold the records, on each row, replace the list of spring conditions with five copies of itself (separated by ?)
 * and replace the list of contiguous groups of damaged springs with five copies of itself (separated by ,).
 *
 * So, this row:
 *
 *     .# 1
 *
 * Would become:
 *
 *     .#?.#?.#?.#?.# 1,1,1,1,1
 *
 * The first line of the above example would become:
 *
 *     ???.###????.###????.###????.###????.### 1,1,3,1,1,3,1,1,3,1,1,3,1,1,3
 *
 * In the above example, after unfolding, the number of possible arrangements for some rows is now much larger:
 *
 * - ???.### 1,1,3 - 1 arrangement
 * - .??..??...?##. 1,1,3 - 16384 arrangements
 * - ?#?#?#?#?#?#?#? 1,3,1,6 - 1 arrangement
 * - ????.#...#... 4,1,1 - 16 arrangements
 * - ????.######..#####. 1,6,5 - 2500 arrangements
 * - ?###???????? 3,2,1 - 506250 arrangements
 *
 * After unfolding, adding all possible arrangement counts together produces 525152.
 *
 * Unfold your condition records; what is the new sum of possible arrangement counts?
 */
package io.ysakhno.adventofcode2023.day12

import io.ysakhno.adventofcode2023.util.ProblemInput
import io.ysakhno.adventofcode2023.util.allInts
import io.ysakhno.adventofcode2023.util.memoize
import io.ysakhno.adventofcode2023.util.println
import kotlin.math.min

private val problemInput = object : ProblemInput {}

@JvmInline
private value class Condition(val text: String) {
    val containsKnown get() = text.contains('#')
    val indexOfFirstKnown get() = text.indexOfFirst { it == '#' }
    val length get() = text.length
    val softGroups get() = text.split('.').filter(String::isNotBlank).map(::Condition)
    override fun toString() = text
    fun splitAt(index: Int) = if (index == length) this to Condition("")
        else if (text[index] == '?') {
            Condition(text.substring(0, index)) to Condition(text.substring(index + 1))
        } else null
}

private val List<Condition>.containsKnown get() = any(Condition::containsKnown)

private class Counter {
    val countPossibilities: (conditions: List<Condition>, groupSizes: List<Int>) -> Long by
        memoize { conditions: List<Condition>, groupSizes: List<Int> ->
            // Handle edge/degenerate cases first
            if (groupSizes.isEmpty()) return@memoize if (conditions.containsKnown) 0L else 1L
            else if (conditions.isEmpty()) return@memoize 0L

            val firstGroup = conditions.first()
            val firstGroupSize = groupSizes.first()

            if (firstGroup.length < firstGroupSize) {
                if (firstGroup.containsKnown) 0 else countPossibilities(conditions.drop(1), groupSizes)
            } else if (firstGroup.length > firstGroupSize) {
                var count = 0L
                if (firstGroup.containsKnown) {
                    val maxLength = min(firstGroup.indexOfFirstKnown + firstGroupSize, firstGroup.length)
                    for (i in firstGroupSize..maxLength) {
                        val (_, subGroup) = firstGroup.splitAt(i) ?: continue
                        val conditionList = if (subGroup.length > 0) listOf(subGroup) else emptyList()
                        count += countPossibilities(conditionList + conditions.drop(1), groupSizes.drop(1))
                    }
                } else {
                    for (i in firstGroupSize..firstGroup.length) {
                        val (_, subGroup) = firstGroup.splitAt(i) ?: continue
                        val conditionList = if (subGroup.length > 0) listOf(subGroup) else emptyList()
                        count += countPossibilities(conditionList + conditions.drop(1), groupSizes.drop(1))
                    }
                    count += countPossibilities(conditions.drop(1), groupSizes)
                }
                count
            } else {
                var count = countPossibilities(conditions.drop(1), groupSizes.drop(1))
                if (!firstGroup.containsKnown) count += countPossibilities(conditions.drop(1), groupSizes)
                count
            }
        }
}

private fun String.generateStringVariations(): List<String> {
    val positions = "\\?".toRegex().findAll(this).map { it.range.first }.toList()
    return List(1 shl positions.size) { index ->
        positions.filterIndexed { idx, _ -> (index and (1 shl idx)) != 0 }
            .fold(replace('?', '.')) { acc, pos -> acc.replaceRange(pos, pos + 1, "#") }
    }
}

internal fun countExhaustively(conditions: String, groupSizes: List<Int>): Long =
    conditions.generateStringVariations()
        .map { it.split('.') to groupSizes }
        .map { (possibility, groups) -> possibility.filter(String::isNotEmpty) to groups }
        .count { (possibility, groups) -> possibility.map(String::length) == groups }
        .toLong()

internal fun countRecursively(conditions: String, groupSizes: List<Int>): Long =
    Counter().countPossibilities(Condition(conditions).softGroups, groupSizes)

private fun part1(input: List<String>) = input
    .map { line -> line.split(' ') }
    .sumOf { (conditions, groups) -> countRecursively(conditions, groups.allInts().toList()) }

private fun part2(input: List<String>) =
    input.map { line -> line.split(' ') }
        .map { (conditions, groups) ->
            "$conditions?$conditions?$conditions?$conditions?$conditions" to "$groups,$groups,$groups,$groups,$groups"
        }
        .sumOf { (conditions, groups) -> countRecursively(conditions, groups.allInts().toList()) }

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    check(part1(testInput) == 21L)
    check(part2(testInput) == 525152L)

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
