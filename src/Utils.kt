@file:Suppress("unused") // it's a library of utility functions; not all of them need to be used right away

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/** Reads lines from the given input txt file. */
fun readInput(filename: String) = Path("src/$filename.txt").readLines().filter(String::isNotBlank)

/** Converts string to MD5 hash. */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/** The cleaner shorthand for printing output. */
fun <T> T.println() = also { println(this) }

/** A regex to extract day number from the class name. */
private val DAY_NAME_REGEX =
    "(?:[A-Za-z][0-9A-Za-z]+\\.)*(?<day>[A-Za-z][0-9A-Za-z]+?)(?:Kt)?(?:\\$[0-9A-Za-z]*)*".toRegex()

/**
 * Determines the day number (i.e. that basename of the source file) from this object.
 *
 * For file named `Day12.kt`, this property will return the day number `"Day12"`.
 *
 * @receiver the object that must be defined in the target source file, whose base filename should be determined.  If
 * the top-level object is defined like so:
 *
 * ```kotlin
 * private val filename = object {}
 * ```
 * then the day number can be accessed through the expression `filename.dayNumber` anywhere in that source file.
 */
val Any.dayNumber: String
    get() = DAY_NAME_REGEX.matchEntire(this.javaClass.name)?.groups?.get("day")?.value
        ?: error("Unknown day (cannot determine day from class name ${this.javaClass.name})")

/** A regex to find integer numbers in a string of text. */
private val INTEGER_NUMBER_REGEX = "[-+]?\\b[0-9]+\\b".toRegex()

/** A regex to find real numbers in a string of text. */
private val REAL_NUMBER_REGEX = "[-+]?\\b[0-9]+(?:\\.[0-9]+\\b)?".toRegex()

/** A regex to find words in a string of text. */
private val WORD_REGEX = "\\b[A-Za-z]+\\b".toRegex()

/** Finds all integer numbers in this string and returns them as a sequence of `Int`s. */
fun String.allInts(): Sequence<Int> = INTEGER_NUMBER_REGEX.findAll(this).map(MatchResult::value).map(String::toInt)

/** Finds all integer numbers in this string and returns them as a sequence of `Long`s. */
fun String.allLongs(): Sequence<Long> = INTEGER_NUMBER_REGEX.findAll(this).map(MatchResult::value).map(String::toLong)

/**
 * Finds all real numbers in this string and returns them as a sequence of `Float`s.
 *
 * Note that this function does not handle numbers with scientific notation.  For example, `"1e-3"` will not be
 * recognized as a valid number.
 */
fun String.allFloats(): Sequence<Float> = REAL_NUMBER_REGEX.findAll(this).map(MatchResult::value).map(String::toFloat)

/**
 * Finds all real numbers in this string and returns them as a sequence of `Double`s.
 *
 * Note that this function does not handle numbers with scientific notation.  For example, `"1e-3"` will not be
 * recognized as a valid number.
 */
fun String.allDoubles(): Sequence<Double> =
    REAL_NUMBER_REGEX.findAll(this).map(MatchResult::value).map(String::toDouble)

/**
 * Finds all words (character sequences consisting of only Latin letters) in this string and returns them as a sequence
 * of `String`s.
 *
 * Note that this function does not handle numbers.  For example, none of the `"0"`, `"42"`, `"3.14"` will be recognized
 * as a valid word.  Also, this function does not handle words with apostrophes.  For example, `"don't"` will not be
 * recognized as a valid word.
 */
fun String.allWords(): Sequence<String> = WORD_REGEX.findAll(this).map(MatchResult::value)
