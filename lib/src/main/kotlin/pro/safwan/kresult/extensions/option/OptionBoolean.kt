package pro.safwan.kresult.extensions.option

import pro.safwan.kresult.types.*

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