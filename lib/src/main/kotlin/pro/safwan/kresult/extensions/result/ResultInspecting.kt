package pro.safwan.kresult.extensions.result

import pro.safwan.kresult.types.*


/**
 * Calls the provided function with the Ok value if present, and returns the result unchanged.
 * Useful for side effects like logging.
 */
inline fun <T, E> Result<T, E>.inspect(action: (T) -> Unit): Result<T, E> {
    if (this is Ok) {
        action(this.value)
    }
    return this
}

/**
 * Calls the provided function with the Err value if present, and returns the result unchanged.
 * Useful for side effects like logging errors.
 */
inline fun <T, E> Result<T, E>.inspectErr(action: (E) -> Unit): Result<T, E> {
    if (this is Err) {
        action(this.error)
    }
    return this
}

/**
 * Returns true if the result is Ok and the value matches the predicate.
 */
inline fun <T, E> Result<T, E>.isOkAnd(predicate: (T) -> Boolean): Boolean = when (this) {
    is Ok -> predicate(this.value)
    is Err -> false
}

/**
 * Returns true if the result is Err and the error matches the predicate.
 */
inline fun <T, E> Result<T, E>.isErrAnd(predicate: (E) -> Boolean): Boolean = when (this) {
    is Err -> predicate(this.error)
    is Ok -> false
}