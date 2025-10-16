package pro.safwan.kresult.extensions.result

import pro.safwan.kresult.types.*

/**
 * Returns other if the result is Ok, otherwise returns the Err value of this.
 */
fun <T, U, E> Result<T, E>.and(other: Result<U, E>): Result<U, E> = when (this) {
    is Ok -> other
    is Err -> this
}

/**
 * Calls the function if the result is Ok, otherwise returns the Err value of this.
 * Also known as flatMap or bind.
 */
inline fun <T, U, E> Result<T, E>.andThen(transform: (T) -> Result<U, E>): Result<U, E> =
    when (this) {
        is Ok -> transform(this.value)
        is Err -> this
    }

/**
 * Returns this result if it contains a value, otherwise returns other.
 */
fun <T, E, F> Result<T, E>.or(other: Result<T, F>): Result<T, F> = when (this) {
    is Ok -> this
    is Err -> other
}

/**
 * Calls the function if the result is Err, otherwise returns the Ok value of this.
 */
inline fun <T, E, F> Result<T, E>.orElse(transform: (E) -> Result<T, F>): Result<T, F> =
    when (this) {
        is Ok -> this
        is Err -> transform(this.error)
    }
