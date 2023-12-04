/*
 * <PUZZLE-DESCRIPTION-HERE>
 */

private val filename = object {}

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // Test if implementation meets criteria from the description
    val testInput = readInput("${filename.dayNumber}_test")
    check(part1(testInput) == 0)
    check(part2(testInput) == 0)

    val input = readInput(filename.dayNumber)
    part1(input).println()
    part2(input).println()
}
