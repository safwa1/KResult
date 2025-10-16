package pro.safwan.kresult.extensions.option

import pro.safwan.kresult.types.*

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
