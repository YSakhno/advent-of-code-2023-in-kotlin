/*
 * --- Day 19: Aplenty ---
 *
 * To start, each part is rated in each of four categories:
 *
 * - x: Extremely cool looking
 * - m: Musical (it makes a noise when you hit it)
 * - a: Aerodynamic
 * - s: Shiny
 *
 * Then, each part is sent through a series of workflows that will ultimately accept or reject the part. Each workflow
 * has a name and contains a list of rules; each rule specifies a condition and where to send the part if the condition
 * is true. The first rule that matches the part being considered is applied immediately, and the part moves on to the
 * destination described by the rule. (The last rule in each workflow has no condition and always applies if reached.)
 *
 * Consider the workflow ex{x>10:one,m<20:two,a>30:R,A}. This workflow is named ex and contains four rules. If workflow
 * ex were considering a specific part, it would perform the following steps in order:
 *
 * - Rule "x>10:one": If the part's x is more than 10, send the part to the workflow named one.
 * - Rule "m<20:two": Otherwise, if the part's m is less than 20, send the part to the workflow named two.
 * - Rule "a>30:R": Otherwise, if the part's a is more than 30, the part is immediately rejected (R).
 * - Rule "A": Otherwise, because no other rules matched the part, the part is immediately accepted (A).
 *
 * If a part is sent to another workflow, it immediately switches to the start of that workflow instead and never
 * returns. If a part is accepted (sent to A) or rejected (sent to R), the part immediately stops any further
 * processing.
 *
 * The system works, but it's not keeping up with the torrent of weird metal shapes. The Elves ask if you can help sort
 * a few parts and give you the list of workflows and some part ratings (your puzzle input). For example:
 *
 *     px{a<2006:qkq,m>2090:A,rfg}
 *     pv{a>1716:R,A}
 *     lnx{m>1548:A,A}
 *     rfg{s<537:gd,x>2440:R,A}
 *     qs{s>3448:A,lnx}
 *     qkq{x<1416:A,crn}
 *     crn{x>2662:A,R}
 *     in{s<1351:px,qqz}
 *     qqz{s>2770:qs,m<1801:hdj,R}
 *     gd{a>3333:R,R}
 *     hdj{m>838:A,pv}
 *     ---
 *     {x=787,m=2655,a=1222,s=2876}
 *     {x=1679,m=44,a=2067,s=496}
 *     {x=2036,m=264,a=79,s=2244}
 *     {x=2461,m=1339,a=466,s=291}
 *     {x=2127,m=1623,a=2188,s=1013}
 *
 * The workflows are listed first, followed by a line containing just the three dashes (---), then the ratings of the
 * parts the Elves would like you to sort. All parts begin in the workflow named in. In this example, the five listed
 * parts go through the following workflows:
 *
 * - {x=787,m=2655,a=1222,s=2876}: in -> qqz -> qs -> lnx -> A
 * - {x=1679,m=44,a=2067,s=496}: in -> px -> rfg -> gd -> R
 * - {x=2036,m=264,a=79,s=2244}: in -> qqz -> hdj -> pv -> A
 * - {x=2461,m=1339,a=466,s=291}: in -> px -> qkq -> crn -> R
 * - {x=2127,m=1623,a=2188,s=1013}: in -> px -> rfg -> A
 *
 * Ultimately, three parts are accepted. Adding up the x, m, a, and s rating for each of the accepted parts gives 7540
 * for the part with x=787, 4623 for the part with x=2036, and 6951 for the part with x=2127. Adding all ratings for all
 * accepted parts gives the sum total of 19114.
 *
 * Sort through all the parts you've been given; what do you get if you add together all the rating numbers for all
 * parts that ultimately get accepted?
 *
 * --- Part Two ---
 *
 * Even with your help, the sorting process still isn't fast enough.
 *
 * One of the Elves comes up with a new plan: rather than sort parts individually through all of these workflows, maybe
 * you can figure out in advance, which combinations of ratings will be accepted or rejected.
 *
 * Each of the four ratings (x, m, a, s) can have an integer value ranging from a minimum of 1 to a maximum of 4000. Of
 * all possible distinct combinations of ratings, your job is to figure out which ones will be accepted.
 *
 * In the above example, there are 167409079868000 distinct combinations of ratings that will be accepted.
 *
 * Consider only your list of workflows; the list of part ratings that the Elves wanted you to sort is no longer
 * relevant. How many distinct combinations of ratings will be accepted by the Elves' workflows?
 */
package io.ysakhno.adventofcode2023.day19

import io.ysakhno.adventofcode2023.util.ProblemInput
import io.ysakhno.adventofcode2023.util.println
import org.junit.jupiter.api.Assertions.assertEquals

private val problemInput = object : ProblemInput {}

private data class MachinePart(val params: Map<Char, Int>)

private enum class ComparisonOperation(val ch: String, val op: (Int, Int) -> Boolean) {
    LESS_THAN("<", { opr1, opr2 -> opr1 < opr2 }), GREATER_THAN(">", { opr1, opr2 -> opr1 > opr2 }),
}

private fun String.toComparisonOperation() = ComparisonOperation.entries.first { it.ch == this }

private data class Condition(val param: Char, val op: ComparisonOperation, val value: Int) {
    fun isMet(part: MachinePart) = op.op(part.params.getValue(param), value)
}

private val CONDITION_REGEX = "(?<param>[xmas])(?<operation>[<>])(?<value>\\d+)".toRegex()

private fun parseCondition(condition: String): Condition {
    val groups = CONDITION_REGEX.matchEntire(condition)?.groups ?: error("Unable to match condition $condition")
    return Condition(
        groups["param"]!!.value[0],
        groups["operation"]!!.value.toComparisonOperation(),
        groups["value"]!!.value.toInt()
    )
}

private data class Rule(val destination: String, val condition: Condition? = null) {
    val isAccepted = destination == "A"
    val isRejected = destination == "R"
}

@JvmName("simplifiedRules")
private fun List<Rule>.simplified() =
    if (groupBy(Rule::destination).size == 1) listOf(Rule(first().destination)) else this

private data class Workflow(val name: String, val rules: List<Rule>)

@JvmName("simplifiedWorkflows")
private fun List<Workflow>.simplified(): List<Workflow> {
    var emptyWorkflows = filter { it.rules.size == 1 && it.rules.first().condition == null }.associateBy(Workflow::name)
    var result: List<Workflow> = this

    while (emptyWorkflows.isNotEmpty()) {
        result = result.filter { it.name !in emptyWorkflows }
            .map { workflow ->
                if (workflow.rules.any { rule -> rule.destination in emptyWorkflows }) {
                    workflow.copy(rules = workflow.rules.map { rule ->
                        if (rule.destination in emptyWorkflows) {
                            Rule(emptyWorkflows.getValue(rule.destination).rules.first().destination, rule.condition)
                        } else rule
                    }.simplified())
                } else workflow
            }
        emptyWorkflows =
            result.filter { it.rules.size == 1 && it.rules.first().condition == null }.associateBy(Workflow::name)
    }

    return result
}

private fun getWorkflowsAndMachineParts(input: List<String>): Pair<Map<String, Workflow>, List<MachinePart>> {
    val lists = input.fold(mutableListOf(mutableListOf<String>())) { acc, s ->
        if (s == "---") {
            acc.add(mutableListOf())
        } else {
            acc.last().add(s)
        }
        acc
    }
    val workflows = lists[0].map { desc ->
        val (name, rules) = desc.split('{', limit = 2)
        val ruleList = rules.removeSuffix("}").split(',').map {
            val split = it.split(':', limit = 2)
            if (split.size > 1) Rule(split[1], parseCondition(split[0])) else Rule(split[0])
        }
        Workflow(name, ruleList.simplified())
    }.simplified().associateBy(Workflow::name)
    val machineParts = lists[1].map { part ->
        val params = part.substringAfter('{').substringBefore('}').split(',').map {
            val (param, value) = it.split('=', limit = 2)
            param[0] to value.toInt()
        }.associate { it }
        MachinePart(params)
    }

    return workflows to machineParts
}

private fun Map<String, Workflow>.isAccepted(machinePart: MachinePart): Boolean {
    var workflow = getValue("in")
    while (true) {
        val rule = workflow.rules.first { it.condition?.isMet(machinePart) != false }
        if (rule.isAccepted) return true
        if (rule.isRejected) return false
        workflow = getValue(rule.destination)
    }
}

private fun part1(input: List<String>): Int {
    val (workflows, machineParts) = getWorkflowsAndMachineParts(input)
    return machineParts.filter(workflows::isAccepted).sumOf { it.params.values.sum() }
}

private data class Subrange(val truthMap: Map<Char, BooleanArray>) {
    val possibilitiesNum
        get() = truthMap.values
            .map { truths -> truths.count { it }.toLong() }
            .reduce { product, count -> product * count }

    fun subdivideBy(condition: Condition): Pair<Subrange, Subrange> {
        val truths = truthMap.getValue(condition.param)
        val truths1 = truths.copyOf()
        val truths2 = truths.copyOf()
        val newTruthMap = truthMap.toMutableMap()

        for (i in truths.indices) {
            if (condition.op.op(i, condition.value)) truths2[i] = false else truths1[i] = false
        }

        newTruthMap[condition.param] = truths1
        val subrange1 = Subrange(newTruthMap.toMap())
        newTruthMap[condition.param] = truths2
        val subrange2 = Subrange(newTruthMap.toMap())

        return subrange1 to subrange2
    }
}

private fun createInitialSubrange() = Subrange(
    mapOf(
        'x' to BooleanArray(4001) { it > 0 },
        'm' to BooleanArray(4001) { it > 0 },
        'a' to BooleanArray(4001) { it > 0 },
        's' to BooleanArray(4001) { it > 0 },
    ),
)

private fun computePossibilities(workflows: Map<String, Workflow>, rules: List<Rule>, subrange: Subrange): Long {
    val rule = rules.first()

    if (rule.condition == null) {
        return when {
            rule.isAccepted -> subrange.possibilitiesNum
            rule.isRejected -> 0L
            else -> computePossibilities(workflows, workflows.getValue(rule.destination).rules, subrange)
        }
    }

    val (subrange1, subrange2) = subrange.subdivideBy(rule.condition)
    val count1 = when {
        rule.isRejected -> 0L
        rule.isAccepted -> subrange1.possibilitiesNum
        else -> computePossibilities(workflows, workflows.getValue(rule.destination).rules, subrange1)
    }
    val count2 = computePossibilities(workflows, rules.drop(1), subrange2)

    return count1 + count2
}

private fun Map<String, Workflow>.computePossibilities() =
    computePossibilities(this, getValue("in").rules, createInitialSubrange())

private fun part2(input: List<String>) = getWorkflowsAndMachineParts(input).first.computePossibilities()

fun main() {
    // Test if implementation meets criteria from the description
    val testInput = problemInput.readTest()
    assertEquals(19_114, part1(testInput), "Part one (sample input)")
    assertEquals(167_409_079_868_000L, part2(testInput), "Part two (sample input)")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
