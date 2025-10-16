package pro.safwan.kresult.extensions.option

import pro.safwan.kresult.types.*

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
