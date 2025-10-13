package pro.safwan.kresult

// ============================================================================
// Transforming Options
// ============================================================================

/**
 * Maps an Option<T> to Option<U> by applying a function to a contained value.
 */
inline fun <T, U> Option<T>.map(transform: (T) -> U): Option<U> = when (this) {
    is Some -> Some(transform(this.value))
    is None -> None
}

/**
 * Returns the provided default result (if None), or applies a function to the contained value (if Some).
 */
inline fun <T, U> Option<T>.mapOr(defaultValue: U, mapper: (T) -> U): U =
    when (this) {
        is Some -> mapper(this.value)
        None -> defaultValue
    }

/**
 * Computes a default function result (if None), or applies a different function to the contained value (if Some).
 */
inline fun <T, U> Option<T>.mapOrElse(defaultFactory: () -> U, mapper: (T) -> U): U =
    when (this) {
        is Some -> mapper(this.value)
        None -> defaultFactory()
    }

/**
 * Returns the contained Some value or throws an exception with a custom message.
 */
fun <T> Option<T>.expect(message: String): T = when (this) {
    is Some -> this.value
    None -> throw IllegalStateException(message)
}

/**
 * Returns the contained Some value or throws an exception.
 */
fun <T> Option<T>.unwrap(): T = when (this) {
    is Some -> this.value
    None -> throw IllegalStateException("Called unwrap on a None value")
}

/**
 * Returns the contained Some value or a provided default.
 */
fun <T> Option<T>.unwrapOr(default: T): T = when (this) {
    is Some -> this.value
    is None -> default
}

/**
 * Returns the contained Some value or computes it from a closure.
 */
inline fun <T> Option<T>.unwrapOrElse(defaultFactory: () -> T): T = when (this) {
    is Some -> this.value
    None -> defaultFactory()
}

/**
 * Converts from Option<Option<T>> to Option<T>.
 */
fun <T> Option<Option<T>>.flatten(): Option<T> = when (this) {
    is Some -> this.value
    None -> None
}

/**
 * Returns None if the option is None, otherwise calls predicate with the wrapped value and returns:
 * - Some(t) if predicate returns true
 * - None if predicate returns false
 */
inline fun <T> Option<T>.filter(predicate: (T) -> Boolean): Option<T> = when (this) {
    is Some -> if (predicate(this.value)) this else None
    None -> None
}

// ============================================================================
// Combining Options
// ============================================================================

/**
 * Zips this and another Option into a pair.
 */
fun <T, U> Option<T>.zip(other: Option<U>): Option<Pair<T, U>> = when {
    this is Some && other is Some -> Some(this.value to other.value)
    else -> None
}

/**
 * Zips this and another Option with a custom zipper function.
 */
inline fun <T1, T2, T3> Option<T1>.zipWith(other: Option<T2>, zipper: (T1, T2) -> T3): Option<T3> =
    when {
        this is Some && other is Some -> Some(zipper(this.value, other.value))
        else -> None
    }

/**
 * Returns None if the option is None, otherwise returns other.
 */
fun <T, U> Option<T>.and(other: Option<U>): Option<U> =
    if (this is Some) other else None

/**
 * Returns the option if it contains a value, otherwise returns other.
 */
fun <T> Option<T>.or(other: Option<T>): Option<T> =
    this as? Some ?: other

/**
 * Returns the option if it contains a value, otherwise calls the function.
 */
inline fun <T> Option<T>.orElse(elseFn: () -> Option<T>): Option<T> =
    this as? Some ?: elseFn()

/**
 * Returns Some if exactly one of this or other is Some, otherwise returns None.
 */
fun <T> Option<T>.xor(other: Option<T>): Option<T> = when {
    this is Some && other is Some -> None
    this is Some -> this
    other is Some -> other
    else -> None
}

/**
 * Returns None if the option is None, otherwise calls transform with the wrapped value and returns the result.
 */
inline fun <T, U> Option<T>.andThen(transform: (T) -> Option<U>): Option<U> = when (this) {
    is Some -> transform(this.value)
    is None -> None
}

// ============================================================================
// Boolean Operations
// ============================================================================

/**
 * Returns true if the option is None or the value inside matches the predicate.
 */
inline fun <T> Option<T>.isNoneOr(predicate: (T) -> Boolean): Boolean =
    when (this) {
        is Some -> predicate(this.value)
        None -> true
    }

/**
 * Returns true if the option is Some and the value inside matches the predicate.
 */
inline fun <T> Option<T>.isSomeAnd(predicate: (T) -> Boolean): Boolean =
    when (this) {
        is Some -> predicate(this.value)
        None -> false
    }

// ============================================================================
// Pattern Matching
// ============================================================================

/**
 * Pattern matching on Option values.
 */
inline fun <T, R> Option<T>.match(
    onSome: (T) -> R,
    onNone: () -> R
): R = when (this) {
    is Some -> onSome(this.value)
    is None -> onNone()
}

// ============================================================================
// Conversions
// ============================================================================

/**
 * Converts the Option to a Sequence.
 */
fun <T> Option<T>.asSequence(): Sequence<T> = when (this) {
    is Some -> sequenceOf(this.value)
    is None -> emptySequence()
}

/**
 * Converts the Option to an Iterable.
 */
fun <T> Option<T>.asIterable(): Iterable<T> = when (this) {
    is Some -> listOf(this.value)
    is None -> emptyList()
}

/**
 * Creates a nullable value from the Option.
 */
fun <T> Option<T>.toNullable(): T? = when (this) {
    is Some -> value
    is None -> null
}

/**
 * Creates an Option from a nullable value.
 */
fun <T> T?.toOption(): Option<T> = if (this != null) Some(this) else None

/**
 * Alias for toOption().
 */
fun <T> T?.asOption(): Option<T> = toOption()

// ============================================================================
// Operators
// ============================================================================

operator fun <T> Option<T>.iterator(): Iterator<T> = when (this) {
    is Some -> listOf(this.value).iterator()
    is None -> emptyList<T>().iterator()
}

/**
 * Destructuring: isSome
 */
operator fun <T> Option<T>.component1(): Boolean = this is Some

/**
 * Destructuring: value or null
 */
operator fun <T> Option<T>.component2(): T? = when (this) {
    is Some -> value
    is None -> null
}

/**
 * Returns true if Option is None (unary not operator).
 */
operator fun <T> Option<T>.not(): Boolean = isNone

// ============================================================================
// Extension Functions
// ============================================================================

/**
 * Wraps this value in a Some.
 */
fun <T> T.some(): Option<T> = Some(this)

/**
 * Returns None.
 */
fun <T> none(): Option<T> = None

/**
 * If this Boolean is true, returns Some(thenFunc()), otherwise None.
 */
inline fun <T> Boolean.then(thenFunc: () -> T): Option<T> =
    if (this) Some(thenFunc()) else None

/**
 * If this Boolean is true, returns Some(value), otherwise None.
 */
fun <T> Boolean.thenSome(value: T): Option<T> =
    if (this) Some(value) else None

/**
 * Parses this string to the specified type.
 */
inline fun <reified T : Any> String.parse(): Option<T> {
    return Option.parse(this)
}

/**
 * Parses this string as an enum value.
 */
inline fun <reified T : Enum<T>> String.parseEnum(ignoreCase: Boolean = false): Option<T> {
    return Option.parseEnum(this, ignoreCase)
}

// ============================================================================
// Collection Extensions
// ============================================================================

/**
 * Flattens a sequence of Options into a sequence containing all inner values.
 * None elements are discarded.
 */
fun <T> Sequence<Option<T>>.values(): Sequence<T> = mapNotNull { it.toNullable() }

/**
 * Flattens an iterable of Options into a sequence containing all inner values.
 * None elements are discarded.
 */
fun <T> Iterable<Option<T>>.values(): List<T> = mapNotNull { it.toNullable() }

/**
 * Gets the value associated with the given key from the map as an Option.
 * Returns Some(value) if the key exists, None otherwise.
 */
fun <K, V> Map<K, V>.getOrNone(key: K): Option<V> = get(key).toOption()

/**
 * Returns the first element of a sequence as an Option.
 * Returns Some(element) if present, None if the sequence is empty.
 */
fun <T> Sequence<T>.firstOrNone(): Option<T> = firstOrNull().toOption()

/**
 * Returns the first element of an iterable as an Option.
 * Returns Some(element) if present, None if the collection is empty.
 */
fun <T> Iterable<T>.firstOrNone(): Option<T> = firstOrNull().toOption()

/**
 * Returns the first element matching the predicate as an Option.
 * Returns Some(element) if found, None otherwise.
 */
inline fun <T> Sequence<T>.firstOrNone(predicate: (T) -> Boolean): Option<T> =
    firstOrNull(predicate).toOption()

/**
 * Returns the first element matching the predicate as an Option.
 * Returns Some(element) if found, None otherwise.
 */
inline fun <T> Iterable<T>.firstOrNone(predicate: (T) -> Boolean): Option<T> =
    firstOrNull(predicate).toOption()

/**
 * Returns the last element of a sequence as an Option.
 * Returns Some(element) if present, None if the sequence is empty.
 */
fun <T> Sequence<T>.lastOrNone(): Option<T> = lastOrNull().toOption()

/**
 * Returns the last element of an iterable as an Option.
 * Returns Some(element) if present, None if the collection is empty.
 */
fun <T> Iterable<T>.lastOrNone(): Option<T> = lastOrNull().toOption()

/**
 * Returns the last element matching the predicate as an Option.
 * Returns Some(element) if found, None otherwise.
 */
inline fun <T> Sequence<T>.lastOrNone(predicate: (T) -> Boolean): Option<T> =
    lastOrNull(predicate).toOption()

/**
 * Returns the last element matching the predicate as an Option.
 * Returns Some(element) if found, None otherwise.
 */
inline fun <T> Iterable<T>.lastOrNone(predicate: (T) -> Boolean): Option<T> =
    lastOrNull(predicate).toOption()

/**
 * Returns a single element from a sequence as an Option.
 * Returns Some(element) if the sequence contains exactly one element, None otherwise.
 */
fun <T> Sequence<T>.singleOrNone(): Option<T> = singleOrNull().toOption()

/**
 * Returns a single element from an iterable as an Option.
 * Returns Some(element) if the collection contains exactly one element, None otherwise.
 */
fun <T> Iterable<T>.singleOrNone(): Option<T> = singleOrNull().toOption()

/**
 * Returns a single element matching the predicate as an Option.
 * Returns Some(element) if exactly one element matches, None otherwise.
 */
inline fun <T> Sequence<T>.singleOrNone(predicate: (T) -> Boolean): Option<T> =
    singleOrNull(predicate).toOption()

/**
 * Returns a single element matching the predicate as an Option.
 * Returns Some(element) if exactly one element matches, None otherwise.
 */
inline fun <T> Iterable<T>.singleOrNone(predicate: (T) -> Boolean): Option<T> =
    singleOrNull(predicate).toOption()

/**
 * Returns an element at the specified index as an Option.
 * Returns Some(element) if the index is valid, None otherwise.
 */
fun <T> Sequence<T>.elementAtOrNone(index: Int): Option<T> = elementAtOrNull(index).toOption()

/**
 * Returns an element at the specified index as an Option.
 * Returns Some(element) if the index is valid, None otherwise.
 */
fun <T> Iterable<T>.elementAtOrNone(index: Int): Option<T> = elementAtOrNull(index).toOption()

/**
 * Returns an element at the specified index as an Option.
 * Returns Some(element) if the index is valid, None otherwise.
 */
fun <T> List<T>.elementAtOrNone(index: Int): Option<T> = getOrNull(index).toOption()

/**
 * Applies a function to each element and returns a sequence of values inside any Some results.
 * Similar to map + filter, but more efficient. Also known as filterMap.
 */
inline fun <T, R> Sequence<T>.mapNotNone(crossinline transform: (T) -> Option<R>): Sequence<R> =
    mapNotNull { transform(it).toNullable() }

/**
 * Applies a function to each element and returns a list of values inside any Some results.
 * Similar to map + filter, but more efficient. Also known as filterMap.
 */
inline fun <T, R> Iterable<T>.mapNotNone(transform: (T) -> Option<R>): List<R> =
    mapNotNull { transform(it).toNullable() }

/**
 * Finds an element in the sequence matching the predicate and returns it as an Option.
 * Returns Some(element) if found, None otherwise.
 */
inline fun <T> Sequence<T>.findOrNone(predicate: (T) -> Boolean): Option<T> =
    find(predicate).toOption()

/**
 * Finds an element in the iterable matching the predicate and returns it as an Option.
 * Returns Some(element) if found, None otherwise.
 */
inline fun <T> Iterable<T>.findOrNone(predicate: (T) -> Boolean): Option<T> =
    find(predicate).toOption()

/**
 * Returns the maximum element as an Option.
 * Returns Some(max) if the collection is not empty, None otherwise.
 */
fun <T : Comparable<T>> Iterable<T>.maxOrNone(): Option<T> = maxOrNull().toOption()

/**
 * Returns the maximum element as an Option.
 * Returns Some(max) if the collection is not empty, None otherwise.
 */
fun <T : Comparable<T>> Sequence<T>.maxOrNone(): Option<T> = maxOrNull().toOption()

/**
 * Returns the minimum element as an Option.
 * Returns Some(min) if the collection is not empty, None otherwise.
 */
fun <T : Comparable<T>> Iterable<T>.minOrNone(): Option<T> = minOrNull().toOption()

/**
 * Returns the minimum element as an Option.
 * Returns Some(min) if the collection is not empty, None otherwise.
 */
fun <T : Comparable<T>> Sequence<T>.minOrNone(): Option<T> = minOrNull().toOption()

/**
 * Returns the maximum element by selector as an Option.
 * Returns Some(max) if the collection is not empty, None otherwise.
 */
inline fun <T, R : Comparable<R>> Iterable<T>.maxByOrNone(selector: (T) -> R): Option<T> =
    maxByOrNull(selector).toOption()

/**
 * Returns the minimum element by selector as an Option.
 * Returns Some(min) if the collection is not empty, None otherwise.
 */
inline fun <T, R : Comparable<R>> Iterable<T>.minByOrNone(selector: (T) -> R): Option<T> =
    minByOrNull(selector).toOption()

// ============================================================================
// Array Extensions
// ============================================================================

/**
 * Returns the first element of an array as an Option.
 */
fun <T> Array<T>.firstOrNone(): Option<T> = firstOrNull().toOption()

/**
 * Returns the first element matching the predicate as an Option.
 */
inline fun <T> Array<T>.firstOrNone(predicate: (T) -> Boolean): Option<T> =
    firstOrNull(predicate).toOption()

/**
 * Returns the last element of an array as an Option.
 */
fun <T> Array<T>.lastOrNone(): Option<T> = lastOrNull().toOption()

/**
 * Returns the last element matching the predicate as an Option.
 */
inline fun <T> Array<T>.lastOrNone(predicate: (T) -> Boolean): Option<T> =
    lastOrNull(predicate).toOption()

/**
 * Returns an element at the specified index as an Option.
 */
fun <T> Array<T>.elementAtOrNone(index: Int): Option<T> = getOrNull(index).toOption()

// ============================================================================
// String Extensions
// ============================================================================

/**
 * Returns a substring as an Option.
 * Returns None if the indices are out of bounds.
 */
fun String.substringOrNone(startIndex: Int, endIndex: Int = length): Option<String> =
    try {
        substring(startIndex, endIndex).toOption()
    } catch (e: IndexOutOfBoundsException) {
        None
    }

/**
 * Returns a substring before the first occurrence of delimiter as an Option.
 * Returns None if the delimiter is not found.
 */
fun String.substringBeforeOrNone(delimiter: String, missingDelimiterValue: String? = null): Option<String> {
    val index = indexOf(delimiter)
    return if (index == -1) {
        missingDelimiterValue.toOption()
    } else {
        substring(0, index).toOption()
    }
}

/**
 * Returns a substring after the first occurrence of delimiter as an Option.
 * Returns None if the delimiter is not found.
 */
fun String.substringAfterOrNone(delimiter: String, missingDelimiterValue: String? = null): Option<String> {
    val index = indexOf(delimiter)
    return if (index == -1) {
        missingDelimiterValue.toOption()
    } else {
        substring(index + delimiter.length).toOption()
    }
}

// ============================================================================
// Regex Extensions
// ============================================================================

/**
 * Returns the first match of the regex as an Option.
 */
fun Regex.findOrNone(input: CharSequence, startIndex: Int = 0): Option<MatchResult> =
    find(input, startIndex).toOption()

/**
 * Returns the entire match result as an Option if the regex matches.
 */
fun Regex.matchEntireOrNone(input: CharSequence): Option<MatchResult> =
    matchEntire(input).toOption()