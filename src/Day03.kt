/*
 * --- Day 3: Gear Ratios ---
 *
 * The engine schematic (the puzzle input) consists of a visual representation of the engine. There are lots of numbers
 * and symbols you don't really understand, but apparently any number adjacent to a symbol, even diagonally, is a "part
 * number" and should be included in your sum. (Periods (.) do not count as a symbol.)
 *
 * Here is an example engine schematic:
 *
 *     467..114..
 *     ...*......
 *     ..35..633.
 *     ......#...
 *     617*......
 *     .....+.58.
 *     ..592.....
 *     ......755.
 *     ...$.*....
 *     .664.598..
 *
 * In this schematic, two numbers are not part numbers because they are not adjacent to a symbol: 114 (top right) and 58
 * (middle right). Every other number is adjacent to a symbol and so is a part number; their sum is 4361.
 *
 * Of course, the actual engine schematic is much larger. What is the sum of all of the part numbers in the engine
 * schematic?
 *
 * --- Part Two ---
 *
 * One of the gears in the engine is wrong. A gear is any * symbol that is adjacent to exactly two part numbers. Its
 * gear ratio is the result of multiplying those two numbers together.
 *
 * This time, you need to find the gear ratio of every gear and add them all up so that the engineer can figure out
 * which gear needs to be replaced.
 *
 * Consider the same engine schematic again:
 *
 *     467..114..
 *     ...*......
 *     ..35..633.
 *     ......#...
 *     617*......
 *     .....+.58.
 *     ..592.....
 *     ......755.
 *     ...$.*....
 *     .664.598..
 *
 * In this schematic, there are two gears. The first is in the top left; it has part numbers 467 and 35, so its gear
 * ratio is 16345. The second gear is in the lower right; its gear ratio is 451490. (The * adjacent to 617 is not a gear
 * because it is only adjacent to one part number.) Adding up all of the gear ratios produces 467835.
 *
 * What is the sum of all of the gear ratios in your engine schematic?
 */

private val filename = object {}

private val SYMBOLS = setOf('#', '$', '%', '&', '*', '+', '-', '/', '=', '@')

private val NUMBER_REGEX = "\\d+".toRegex()

private val Char.isSymbol
    get() = this in SYMBOLS

private data class GearLoc(val x: Int, val y: Int)

private data class PartWithGears(val number: Int, val attachedGears: List<GearLoc>)

private data class Gear(val number1: Int, val number2: Int) {

    val ratio = number1 * number2
}

private data class Engine(private val lines: List<String>) {

    init {
        val check = lines.flatMap { line ->
            line.filterNot { ch -> ch.isSymbol || ch.isDigit() || ch == '.' }.toSet()
        }.toSet()

        require(check.isEmpty()) { "Invalid characters: $check" }
    }

    val partNumbers
        get() = lines.flatMapIndexed { idx, line ->
            NUMBER_REGEX.findAll(line).filter { it.isPartNumber(idx) }.map(MatchResult::value).map(String::toInt)
        }

    val partsWithGears: List<PartWithGears>
        get() = lines.flatMapIndexed { idx, line ->
            NUMBER_REGEX.findAll(line)
                .map { findGears(idx, it) to it }
                .filter { (gears) -> gears.isNotEmpty() }
                .map { (gears, mr) -> PartWithGears(mr.value.toInt(), gears) }
        }

    val gears: List<Gear>
        get() = partsWithGears.flatMap { part -> part.attachedGears.map { gear -> gear to part.number } }
            .groupBy { (gear) -> gear }
            .values
            .filter { it.size == 2 }
            .map { list ->
                val (num1, num2) = list.map { (_, num) -> num }
                Gear(num1, num2)
            }

    private fun MatchResult.isPartNumber(lineIdx: Int): Boolean {
        val top = lineIdx - 1
        val bottom = lineIdx + 1
        val left = range.first - 1
        val right = range.last + 1

        if (top >= 0) {
            for (ch in lines[top].substring(left.coerceAtLeast(0), (right + 1).coerceAtMost(lines[top].length))) {
                if (ch.isSymbol) return true
            }
        }
        if (bottom < lines.size) {
            for (ch in lines[bottom].substring(left.coerceAtLeast(0), (right + 1).coerceAtMost(lines[bottom].length))) {
                if (ch.isSymbol) return true
            }
        }

        return (left >= 0 && lines[lineIdx][left].isSymbol) ||
                (right < lines[lineIdx].length && lines[lineIdx][right].isSymbol)
    }

    private fun findGears(lineIdx: Int, mr: MatchResult): List<GearLoc> {
        val top = lineIdx - 1
        val bottom = lineIdx + 1
        val left = mr.range.first - 1
        val right = mr.range.last + 1
        val gears = mutableListOf<GearLoc>()

        if (top >= 0) {
            for (i in left.coerceAtLeast(0)..right.coerceAtMost(lines[top].length - 1)) {
                if (lines[top][i] == '*') gears.add(GearLoc(i, top))
            }
        }
        if (bottom < lines.size) {
            for (i in left.coerceAtLeast(0)..right.coerceAtMost(lines[bottom].length - 1)) {
                if (lines[bottom][i] == '*') gears.add(GearLoc(i, bottom))
            }
        }

        if (left >= 0 && lines[lineIdx][left] == '*') gears.add(GearLoc(left, lineIdx))
        if (right < lines[lineIdx].length && lines[lineIdx][right] == '*') gears.add(GearLoc(right, lineIdx))

        return gears
    }
}

fun main() {
    fun part1(input: List<String>) = Engine(input).partNumbers.sum()

    fun part2(input: List<String>) = Engine(input).gears.sumOf(Gear::ratio)

    // Test if implementation meets criteria from the description
    val testInput = readInput("${filename.dayNumber}_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput(filename.dayNumber)
    part1(input).println()
    part2(input).println()
}
