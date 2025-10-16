package pro.safwan.kresult.extensions.option

import pro.safwan.kresult.types.*

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
