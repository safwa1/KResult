package pro.safwan.kresult

/**
 * Represents a result of an operation that can either succeed with a value of type T (Ok),
 * or fail with an error of type E (Err).
 * Inspired by Rust's Result<T, E> type.
 */
sealed interface Result<out T, out E> {

    companion object {
        /**
         * Creates an Ok variant containing the given value.
         */
        fun <T> ok(value: T): Result<T, Nothing> = Ok(value)

        /**
         * Creates an Err variant containing the given error.
         */
        fun <E> err(error: E): Result<Nothing, E> = Err(error)

        /**
         * Attempts to execute the given function and returns a Result.
         * Returns Ok if successful, Err with the exception if it throws.
         */
        inline fun <T> tryGet(block: () -> T): Result<T, Exception> {
            return try {
                Ok(block())
            } catch (e: Exception) {
                Err(e)
            }
        }

        /**
         * Converts an Option to a Result, using the provided error if None.
         */
        fun <T, E> fromOption(option: Option<T>, error: E): Result<T, E> =
            when (option) {
                is Some -> Ok(option.value)
                is None -> Err(error)
            }

        /**
         * Converts an Option to a Result, using a lazy error factory if None.
         */
        inline fun <T, E> fromOption(option: Option<T>, errorFactory: () -> E): Result<T, E> =
            when (option) {
                is Some -> Ok(option.value)
                is None -> Err(errorFactory())
            }
    }

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
