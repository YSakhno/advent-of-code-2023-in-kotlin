package io.ysakhno.adventofcode2023.day12

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.WithDataTestName
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.stringPattern
import io.kotest.property.forAll

private data class TestInput(val conditions: String, val groupSizes: List<Int>, val expected: Int) : WithDataTestName {
    override fun dataTestName() = "('$conditions', $groupSizes) -> $expected"
}

class Day12KtTest : FunSpec({
    context("Function countExhaustively()") {
        context("should count obvious cases correctly") {
            withData(
                TestInput("#", listOf(1), 1),
                TestInput("##", listOf(2), 1),
                TestInput("##.#", listOf(2, 1), 1),
                TestInput("#.##", listOf(1, 2), 1),
                TestInput("###", listOf(3), 1),
                TestInput("###.....##..#", listOf(3, 2, 1), 1),
                TestInput("..###...##.##...", listOf(3, 2, 2), 1),
            ) { (conditions, groupSizes, expected) ->
                countExhaustively(conditions, groupSizes) shouldBe expected
            }
        }
        context("should count normal cases correctly") {
            withData(
                TestInput("?", listOf(1), 1),
                TestInput("??", listOf(1), 2),
                TestInput("#?", listOf(1), 1),
                TestInput("?#", listOf(1), 1),
                TestInput("???", listOf(1, 1), 1),
                TestInput("????", listOf(1, 1), 3),
                TestInput("#???", listOf(1, 1), 2),
                TestInput("?#??", listOf(1, 1), 1),
                TestInput("??#?", listOf(1, 1), 1),
                TestInput("???#", listOf(1, 1), 2),
            ) { (conditions, groupSizes, expected) ->
                countExhaustively(conditions, groupSizes) shouldBe expected
            }
        }
        context("should count degenerate cases correctly") {
            withData(
                TestInput(".", emptyList(), 1),
                TestInput("..", emptyList(), 1),
                TestInput("?", emptyList(), 1), // possible arrangement: '.'
                TestInput("??", emptyList(), 1), // possible arrangement: '..'
                TestInput("?.", emptyList(), 1),
                TestInput(".?", emptyList(), 1),
                TestInput(".?.", emptyList(), 1),
            ) { (conditions, groupSizes, expected) ->
                countExhaustively(conditions, groupSizes) shouldBe expected
            }
        }
        context("should count impossible cases correctly") {
            withData(
                TestInput("#", emptyList(), 0),
                TestInput("#", listOf(2), 0),
                TestInput("#?", emptyList(), 0),
                TestInput("#?", listOf(3), 0),
                TestInput("?#", emptyList(), 0),
                TestInput("?#", listOf(3), 0),
            ) { (conditions, groupSizes, expected) ->
                countExhaustively(conditions, groupSizes) shouldBe expected
            }
        }
    }
    context("Function countRecursively()") {
        withData(
            "?" to listOf(1),
            "??" to listOf(1),
            "#?" to listOf(1),
            "?#" to listOf(1),
            "???" to listOf(1, 1),
            "????" to listOf(1, 1),
            "?#?" to listOf(2),
            "??#??" to listOf(3),
            "???#???" to listOf(3),
            "???#???" to listOf(3),
            "????#????" to listOf(3),
            "?##?" to listOf(3),
            "??##??" to listOf(3),
            "???##???" to listOf(3),
            "????##????" to listOf(3),
            "?##??.#?" to listOf(3, 1),
            "??.??#?" to listOf(1, 1),
        ) { (input, groupSizes) ->
            val expected = countExhaustively(input, groupSizes)

            countRecursively(input, groupSizes) shouldBe expected
        }
    }
    test("Function countRecursively() - property-based test") {
        forAll(Arb.list(Arb.int(range = 1..5), range = 0..5).flatMap { groupSizes ->
            Arb.bind(
                Arb.stringPattern("\\.{0,2}"),
                Arb.list(Arb.stringPattern("\\.{1,3}"), range = groupSizes.size..groupSizes.size),
                Arb.stringPattern("\\.{0,2}"),
            ) { leadingSpace, spaces, trailingSpace ->
                buildString {
                    append(leadingSpace)
                    for (i in groupSizes.indices) {
                        if (i > 0) append(spaces[i])
                        append("#".repeat(groupSizes[i]))
                    }
                    append(trailingSpace)
                } to groupSizes
            }
        }.flatMap { (pristine, groupSizes) ->
            Arb.list(Arb.boolean(), range = pristine.length..pristine.length).map { unknowns ->
                pristine.mapIndexed { i, ch -> if (unknowns[i]) '?' else ch }.joinToString(separator = "") to groupSizes
            }
        }) { (conditions, groupSizes) ->
            countRecursively(conditions, groupSizes) == countExhaustively(conditions, groupSizes)
        }
    }
})
