@file:Suppress("unused") // it's a library of utility functions; not all of them need to be used right away

import java.math.BigDecimal
import java.math.BigInteger

/** Returns a list of differences between every two adjacent elements of this iterable. */
@JvmName("differencesOfInts") // to resolve "platform declaration clash"
fun Iterable<Int>.differences(): List<Int> = zipWithNext { a, b -> b - a }

/** Returns a list of differences between every two adjacent elements of this iterable. */
@JvmName("differencesOfLongs") // to resolve "platform declaration clash"
fun Iterable<Long>.differences(): List<Long> = zipWithNext { a, b -> b - a }

/** Returns a list of differences between every two adjacent elements of this iterable. */
@JvmName("differencesOfFloats") // to resolve "platform declaration clash"
fun Iterable<Float>.differences(): List<Float> = zipWithNext { a, b -> b - a }

/** Returns a list of differences between every two adjacent elements of this iterable. */
@JvmName("differencesOfDoubles") // to resolve "platform declaration clash"
fun Iterable<Double>.differences(): List<Double> = zipWithNext { a, b -> b - a }

/** Returns a list of differences between every two adjacent elements of this iterable. */
@JvmName("differencesOfBigIntegers") // to resolve "platform declaration clash"
fun Iterable<BigInteger>.differences(): List<BigInteger> = zipWithNext { a, b -> b - a }

/** Returns a list of differences between every two adjacent elements of this iterable. */
@JvmName("differencesOfBigDecimals") // to resolve "platform declaration clash"
fun Iterable<BigDecimal>.differences(): List<BigDecimal> = zipWithNext { a, b -> b - a }

/**
 * Transposes the two-dimensional structure represented by this iterable of lists.
 *
 * Each List in the input iterable represents a row of a two-dimensional structure, and this function transposes the
 * rows and columns, creating a new two-dimensional list where the columns become rows and vice versa.  Transposition is
 * performed such that elements at each position of each list are combined into a single list that is placed at a
 * corresponding position in the resulting list.
 */
fun <E> Iterable<List<E>>.transpose(): List<List<E>> = firstOrNull()?.indices?.map { i -> map { it[i] } } ?: emptyList()
