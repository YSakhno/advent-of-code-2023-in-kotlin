/*
 * --- Day 20: Pulse Propagation ---
 *
 * Modules communicate using pulses. Each pulse is either a high pulse or a low pulse. When a module sends a pulse, it
 * sends that type of pulse to each module in its list of destination modules.
 *
 * There are several different types of modules:
 *
 * Flip-flop modules (prefix %) are either on or off; they are initially off. If a flip-flop module receives a high
 * pulse, it is ignored and nothing happens. However, if a flip-flop module receives a low pulse, it flips between on
 * and off. If it was off, it turns on and sends a high pulse. If it was on, it turns off and sends a low pulse.
 *
 * Conjunction modules (prefix &) remember the type of the most recent pulse received from each of their connected input
 * modules; they initially default to remembering a low pulse for each input. When a pulse is received, the conjunction
 * module first updates its memory for that input. Then, if it remembers high pulses for all inputs, it sends a low
 * pulse; otherwise, it sends a high pulse.
 *
 * There is a single broadcast module (named broadcaster). When it receives a pulse, it sends the same pulse to all of
 * its destination modules.
 *
 * There is a module with a single button on it called, aptly, the button module. When you push the button, a single low
 * pulse is sent directly to the broadcaster module.
 *
 * After pushing the button, you must wait until all pulses have been delivered and fully handled before pushing it
 * again. Never push the button if modules are still processing pulses.
 *
 * Pulses are always processed in the order they are sent. So, if a pulse is sent to modules a, b, and c, and then
 * module a processes its pulse and sends more pulses, the pulses sent to modules b and c would have to be handled
 * first.
 *
 * The module configuration (your puzzle input) lists each module. The name of the module is preceded by a symbol
 * identifying its type, if any. The name is then followed by an arrow and a list of its destination modules. For
 * example:
 *
 *     broadcaster -> a, b, c
 *     %a -> b
 *     %b -> c
 *     %c -> inv
 *     &inv -> a
 *
 * In this module configuration, the broadcaster has three destination modules named a, b, and c. Each of these modules
 * is a flip-flop module (as indicated by the % prefix). a outputs to b which outputs to c which outputs to another
 * module named inv. inv is a conjunction module (as indicated by the & prefix) which, because it has only one input,
 * acts like an inverter (it sends the opposite of the pulse type it receives); it outputs to a.
 *
 * By pushing the button once, the following pulses are sent:
 *
 *     button -low-> broadcaster
 *     broadcaster -low-> a
 *     broadcaster -low-> b
 *     broadcaster -low-> c
 *     a -high-> b
 *     b -high-> c
 *     c -high-> inv
 *     inv -low-> a
 *     a -low-> b
 *     b -low-> c
 *     c -low-> inv
 *     inv -high-> a
 *
 * After this sequence, the flip-flop modules all end up off, so pushing the button again repeats the same sequence.
 *
 * Here's a more interesting example:
 *
 *     broadcaster -> a
 *     %a -> inv, con
 *     &inv -> b
 *     %b -> con
 *     &con -> output
 *
 * This module configuration includes the broadcaster, two flip-flops (named a and b), a single-input conjunction module
 * (inv), a multi-input conjunction module (con), and an untyped module named output (for testing purposes). The
 * multi-input conjunction module con watches the two flip-flop modules and, if they're both on, sends a low pulse to
 * the output module.
 *
 * Here's what happens if you push the button once:
 *
 *     button -low-> broadcaster
 *     broadcaster -low-> a
 *     a -high-> inv
 *     a -high-> con
 *     inv -low-> b
 *     con -high-> output
 *     b -high-> con
 *     con -low-> output
 *
 * Both flip-flops turn on and a low pulse is sent to output! However, now that both flip-flops are on and con remembers
 * a high pulse from each of its two inputs, pushing the button a second time does something different:
 *
 *     button -low-> broadcaster
 *     broadcaster -low-> a
 *     a -low-> inv
 *     a -low-> con
 *     inv -high-> b
 *     con -high-> output
 *
 * Flip-flop a turns off! Now, con remembers a low pulse from module a, and so it sends only a high pulse to output.
 *
 * Push the button a third time:
 *
 *     button -low-> broadcaster
 *     broadcaster -low-> a
 *     a -high-> inv
 *     a -high-> con
 *     inv -low-> b
 *     con -low-> output
 *     b -low-> con
 *     con -high-> output
 *
 * This time, flip-flop a turns on, then flip-flop b turns off. However, before b can turn off, the pulse sent to con is
 * handled first, so it briefly remembers all high pulses for its inputs and sends a low pulse to output. After that,
 * flip-flop b turns off, which causes con to update its state and send a high pulse to output.
 *
 * Finally, with a on and b off, push the button a fourth time:
 *
 *     button -low-> broadcaster
 *     broadcaster -low-> a
 *     a -low-> inv
 *     a -low-> con
 *     inv -high-> b
 *     con -high-> output
 *
 * This completes the cycle: a turns off, causing con to remember only low pulses and restoring all modules to their
 * original states.
 *
 * To get the cables warmed up, the Elves have pushed the button 1000 times. How many pulses got sent as a result
 * (including the pulses sent by the button itself)?
 *
 * In the first example, the same thing happens every time the button is pushed: 8 low pulses and 4 high pulses are
 * sent. So, after pushing the button 1000 times, 8000 low pulses and 4000 high pulses are sent. Multiplying these
 * together gives 32000000.
 *
 * In the second example, after pushing the button 1000 times, 4250 low pulses and 2750 high pulses are sent.
 * Multiplying these together gives 11687500.
 *
 * Consult your module configuration; determine the number of low pulses and high pulses that would be sent after
 * pushing the button 1000 times, waiting for all pulses to be fully handled after each push of the button. What do you
 * get if you multiply the total number of low pulses sent by the total number of high pulses sent?
 *
 * --- Part Two ---
 *
 * The final machine responsible for moving the sand down to Island Island has a module attached named rx. The machine
 * turns on when a single low pulse is sent to rx.
 *
 * Reset all modules to their default states. Waiting for all pulses to be fully handled after each button press, what
 * is the fewest number of button presses required to deliver a single low pulse to the module named rx?
 */
package io.ysakhno.adventofcode2023.day20

import io.ysakhno.adventofcode2023.day20.State.OFF
import io.ysakhno.adventofcode2023.day20.State.ON
import io.ysakhno.adventofcode2023.util.ProblemInput
import io.ysakhno.adventofcode2023.util.println
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.LinkedList
import kotlin.LazyThreadSafetyMode.NONE

private val problemInput = object : ProblemInput {}

private enum class State {
    OFF, ON;

    operator fun not() = if (this == ON) OFF else ON
}

private data class Pulse(val from: Module, val state: State, val to: Module) {
    fun send() = to.process(this)
}

private abstract class Module(open val name: String, open var outputs: List<Module>) {
    open val state: State get() = OFF
    abstract fun process(pulse: Pulse): List<Pulse>
    open fun addInput(input: Module) = Unit
}

private data class FlipFlop(override val name: String, override var outputs: List<Module>) : Module(name, outputs) {
    override var state: State = OFF
    private val cachedLowPulses by lazy(NONE) { outputs.map { Pulse(this, OFF, it) } }
    private val cachedHighPulses by lazy(NONE) { outputs.map { Pulse(this, ON, it) } }
    override fun process(pulse: Pulse): List<Pulse> = if (pulse.state == OFF) {
        state = !state
        if (state == OFF) cachedLowPulses else cachedHighPulses
    } else emptyList()
}

private data class Conjunction(override val name: String, override var outputs: List<Module>) : Module(name, outputs) {
    private val inputs = mutableMapOf<String, Pulse>()
    private val cachedLowPulses by lazy(NONE) { outputs.map { Pulse(this, OFF, it) } }
    private val cachedHighPulses by lazy(NONE) { outputs.map { Pulse(this, ON, it) } }
    override fun process(pulse: Pulse): List<Pulse> {
        inputs[pulse.from.name] = pulse
        return if (inputs.values.all { it.state == ON }) cachedLowPulses else cachedHighPulses
    }
    override fun addInput(input: Module) {
        inputs[input.name] = Pulse(input, OFF, this)
    }
}

private data class Broadcast(override var outputs: List<Module>) : Module("broadcaster", outputs) {
    override fun process(pulse: Pulse): List<Pulse> = outputs.map { Pulse(this, pulse.state, it) }
}

private data class Button(private val broadcast: Broadcast) : Module("<button>", listOf(broadcast)) {
    override fun process(pulse: Pulse): List<Pulse> = listOf(Pulse(this, OFF, broadcast))
}

private class Receiver : Module("rx", emptyList()) {
    var isActivated = false
    override fun process(pulse: Pulse): List<Pulse> {
        isActivated = isActivated || pulse.state == OFF
        return emptyList()
    }
}

private data class Dummy(override val name: String) : Module(name, emptyList()) {
    override fun process(pulse: Pulse): List<Pulse> = emptyList()
}

private val DUMMY_PULSE = Pulse(Dummy("dummy"), ON, Dummy("dummy"))

private fun Button.pressAndCount(): Pair<Long, Long> {
    var numLowPulses = 0L
    var numHighPulses = 0L
    val pulses = LinkedList<Pulse>().apply { addAll(process(DUMMY_PULSE)) }

    while (pulses.isNotEmpty()) {
        val pulse = pulses.poll()
        if (pulse.state == OFF) ++numLowPulses else ++numHighPulses
        pulse.send().let(pulses::addAll)
    }

    return numLowPulses to numHighPulses
}

private fun Button.press() {
    val pulses = LinkedList<Pulse>().apply { addAll(process(DUMMY_PULSE)) }

    while (pulses.isNotEmpty()) {
        pulses.poll().send().let(pulses::addAll)
    }
}

private val CONFIG_LINE_REGEX =
    """\s*(?:(?<bc>broadcaster)|(?<code>[%&])(?<name>[a-z]+))\s*->\s*(?<outputs>[a-z]+(?:\s*,\s*[a-z]+)*)\s*"""
        .toRegex()

private fun parseConfiguration(input: List<String>): Map<String, Module> {
    val modules = input.map { line ->
        val matchResult = CONFIG_LINE_REGEX.matchEntire(line) ?: error("Cannot parse configuration line $line")
        val outputs = matchResult.groups["outputs"]
            ?.value
            ?.split(',')
            ?.map(String::trim)
            ?.map { if (it == "rx") Receiver() else Dummy(it) }
            ?: error("Unexpected state: outputs are absent in $line")

        if (matchResult.groups["bc"] != null) {
            Broadcast(outputs)
        } else {
            val code = matchResult.groups["code"]?.value
            val name = matchResult.groups["name"]?.value ?: error("Unexpected state: name is absent in $line")
            when (code) {
                "%" -> FlipFlop(name, outputs)
                "&" -> Conjunction(name, outputs)
                else -> error("Unexpected state: wrong code ($code) in $line")
            }
        }
    }.associateBy { it.name }

    modules.values.forEach { module ->
        module.outputs = module.outputs.map { modules[it.name] ?: it }.onEach { it.addInput(module) }
    }

    return modules
}

private fun part1(input: List<String>): Long {
    val button = Button(parseConfiguration(input)["broadcaster"] as Broadcast)

    return (1..1000).fold(0L to 0L) { acc, _ ->
        val (numLow, numHigh) = button.pressAndCount()
        acc.first + numLow to acc.second + numHigh
    }.let { it.first * it.second }
}

private fun part2(input: List<String>): Long {
    val modules = parseConfiguration(input)
    val button = Button(modules["broadcaster"] as Broadcast)
    val receiver = modules.values.flatMap(Module::outputs).find { it.name == "rx" } as Receiver
    var count = 0L

    while (!receiver.isActivated) {
        button.press()
        ++count
        receiver.isActivated = true // otherwise takes forever, does not finish
    }

    return count
}

fun main() {
    // Test if implementation meets criteria from the description
    val testInput1 = problemInput.readTest(1)
    val testInput2 = problemInput.readTest(2)
    assertEquals(32_000_000L, part1(testInput1), "Part one (sample input 1)")
    assertEquals(11_687_500L, part1(testInput2), "Part one (sample input 2)")
    println("All tests passed")

    val input = problemInput.read()
    part1(input).println()
    part2(input).println()
}
