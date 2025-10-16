package pro.safwan.kresult.extensions.result

import pro.safwan.kresult.types.*


/**
 * Flattens a sequence of Results into a sequence containing all Ok values.
 * Err values are discarded.
 */
fun <T, E> Sequence<Result<T, E>>.values(): Sequence<T> =
    mapNotNull { it.toNullable() }

/**
 * Flattens an iterable of Results into a list containing all Ok values.
 * Err values are discarded.
 */
fun <T, E> Iterable<Result<T, E>>.values(): List<T> =
    mapNotNull { it.toNullable() }

/**
 * Flattens a sequence of Results into a sequence containing all Err values.
 * Ok values are discarded.
 */
fun <T, E> Sequence<Result<T, E>>.errors(): Sequence<E> =
    mapNotNull { if (it is Err) it.error else null }

/**
 * Flattens an iterable of Results into a list containing all Err values.
 * Ok values are discarded.
 */
fun <T, E> Iterable<Result<T, E>>.errors(): List<E> =
    mapNotNull { if (it is Err) it.error else null }

/**
 * Applies a function to each element and returns a sequence of values inside any Ok results.
 */
inline fun <T, U, E> Sequence<T>.mapNotErr(crossinline transform: (T) -> Result<U, E>): Sequence<U> =
    mapNotNull { transform(it).toNullable() }

/**
 * Applies a function to each element and returns a list of values inside any Ok results.
 */
inline fun <T, U, E> Iterable<T>.mapNotErr(transform: (T) -> Result<U, E>): List<U> =
    mapNotNull { transform(it).toNullable() }

/**
 * Partitions a sequence of Results into two lists: Ok values and Err values.
 */
fun <T, E> Sequence<Result<T, E>>.partition(): Pair<List<T>, List<E>> {
    val oks = mutableListOf<T>()
    val errs = mutableListOf<E>()
    forEach { result ->
        when (result) {
            is Ok -> oks.add(result.value)
            is Err -> errs.add(result.error)
        }
    }
    return oks to errs
}

/**
 * Partitions an iterable of Results into two lists: Ok values and Err values.
 */
fun <T, E> Iterable<Result<T, E>>.partition(): Pair<List<T>, List<E>> {
    val oks = mutableListOf<T>()
    val errs = mutableListOf<E>()
    forEach { result ->
        when (result) {
            is Ok -> oks.add(result.value)
            is Err -> errs.add(result.error)
        }
    }
    return oks to errs
}

/**
 * Collects all Ok values if the sequence contains no Err values.
 * If any Err is found, returns the first Err.
 */
fun <T, E> Sequence<Result<T, E>>.collectOk(): Result<List<T>, E> {
    val values = mutableListOf<T>()
    for (result in this) {
        when (result) {
            is Ok -> values.add(result.value)
            is Err -> return result
        }
    }
    return Ok(values)
}

/**
 * Collects all Ok values if the iterable contains no Err values.
 * If any Err is found, returns the first Err.
 */
fun <T, E> Iterable<Result<T, E>>.collectOk(): Result<List<T>, E> {
    val values = mutableListOf<T>()
    for (result in this) {
        when (result) {
            is Ok -> values.add(result.value)
            is Err -> return result
        }
    }
    return Ok(values)
}
