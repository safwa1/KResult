package pro.safwan.kresult.types

/**
 * Represents a result of an operation that can either succeed with a value of type T (Ok),
 * or fail with an error of type E (Err).
 * Inspired by Rust's Result<T, E> type.
 */
sealed interface Result<out T, out E> {

    val isOk: Boolean get() = this is Ok
    val isErr: Boolean get() = this is Err

    /**
     * Returns true if the result is Ok and contains the given value.
     */
    fun contains(value: @UnsafeVariance T): Boolean = when (this) {
        is Ok -> this.value == value
        is Err -> false
    }

    /**
     * Returns true if the result is Err and contains the given error.
     */
    fun containsErr(error: @UnsafeVariance E): Boolean = when (this) {
        is Err -> this.error == error
        is Ok -> false
    }
}

/**
 * Success value of type T.
 */
@JvmInline
value class Ok<T>(val value: T) : Result<T, Nothing> {
    override fun toString(): String = "Ok($value)"
}

/**
 * Error value of type E.
 */
@JvmInline
value class Err<E>(val error: E) : Result<Nothing, E> {
    override fun toString(): String = "Err($error)"
}
