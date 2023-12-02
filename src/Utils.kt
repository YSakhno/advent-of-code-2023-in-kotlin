import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/** Reads lines from the given input txt file. */
fun readInput(name: String) = Path("src/$name.txt").readLines().filter(String::isNotBlank)

/** Converts string to MD5 hash. */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/** The cleaner shorthand for printing output. */
fun <T> T.println() = also { println(this) }

/** A regexp to extracts day number from the class name. */
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
