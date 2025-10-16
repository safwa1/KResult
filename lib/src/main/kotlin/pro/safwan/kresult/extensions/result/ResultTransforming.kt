package pro.safwan.kresult.extensions.result

import pro.safwan.kresult.types.*

/**
 * Maps a Result<T, E> to Result<U, E> by applying a function to the contained Ok value.
 * Leaves an Err value untouched.
 */
inline fun <T, U, E> Result<T, E>.map(transform: (T) -> U): Result<U, E> = when (this) {
    is Ok -> Ok(transform(this.value))
    is Err -> this
}

/**
 * Maps a Result<T, E> to Result<T, F> by applying a function to the contained Err value.
 * Leaves an Ok value untouched.
 */
inline fun <T, E, F> Result<T, E>.mapErr(transform: (E) -> F): Result<T, F> = when (this) {
    is Ok -> this
    is Err -> Err(transform(this.error))
}

/**
 * Returns the provided default (if Err), or applies a function to the contained value (if Ok).
 */
inline fun <T, U, E> Result<T, E>.mapOr(defaultValue: U, transform: (T) -> U): U = when (this) {
    is Ok -> transform(this.value)
    is Err -> defaultValue
}

/**
 * Maps a Result by applying fallback function to an Err value, or transform function to an Ok value.
 */
inline fun <T, U, E> Result<T, E>.mapOrElse(defaultFactory: (E) -> U, transform: (T) -> U): U =
    when (this) {
        is Ok -> transform(this.value)
        is Err -> defaultFactory(this.error)
    }

/**
 * Returns the contained Ok value or throws an exception with a custom message.
 */
fun <T, E> Result<T, E>.expect(message: String): T = when (this) {
    is Ok -> this.value
    is Err -> when (val err = this.error) {
        is Exception -> throw IllegalStateException(message, err)
        else -> throw IllegalStateException("$message: $err")
    }
}

/**
 * Returns the contained Ok value or throws an exception.
 */
fun <T, E> Result<T, E>.unwrap(): T = when (this) {
    is Ok -> this.value
    is Err -> when (val err = this.error) {
        is Exception -> throw IllegalStateException("Could not unwrap a Result in the Err state.", err)
        else -> throw IllegalStateException("Could not unwrap a Result in the Err state: $err")
    }
}

/**
 * Returns the contained Err value or throws an exception with a custom message.
 */
fun <T, E> Result<T, E>.expectErr(message: String): E = when (this) {
    is Err -> this.error
    is Ok -> throw IllegalStateException("$message: ${this.value}")
}

/**
 * Returns the contained Err value or throws an exception.
 */
fun <T, E> Result<T, E>.unwrapErr(): E = when (this) {
    is Err -> this.error
    is Ok -> throw IllegalStateException("Expected the result to be in the Err state, but it was Ok: ${this.value}")
}

/**
 * Returns the contained Ok value or a provided default.
 */
fun <T, E> Result<T, E>.unwrapOr(default: T): T = when (this) {
    is Ok -> this.value
    is Err -> default
}

/**
 * Returns the contained Ok value or computes it from the error using the provided function.
 */
inline fun <T, E> Result<T, E>.unwrapOrElse(defaultFactory: (E) -> T): T = when (this) {
    is Ok -> this.value
    is Err -> defaultFactory(this.error)
}

/**
 * Returns the contained Ok value or null.
 */
fun <T, E> Result<T, E>.unwrapOrDefault(): T? = when (this) {
    is Ok -> this.value
    is Err -> null
}

/**
 * Converts from Result<Result<T, E>, E> to Result<T, E>.
 */
fun <T, E> Result<Result<T, E>, E>.flatten(): Result<T, E> = when (this) {
    is Ok -> this.value
    is Err -> this
}
