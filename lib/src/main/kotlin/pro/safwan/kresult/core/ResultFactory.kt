package pro.safwan.kresult.core

import pro.safwan.kresult.types.*

/**
 * Creates an Ok variant containing the given value.
 */
fun <T> success(value: T): Result<T, Nothing> = Ok(value)

/**
 * Creates an Err variant containing the given error.
 */
fun <E> failure(error: E): Result<Nothing, E> = Err(error)

/**
 * Attempts to execute the given function and returns a Result.
 * Returns Ok if successful, Err with the exception if it throws.
 */
inline fun <T> runCatchingResult(block: () -> T): Result<T, Exception> {
    return try {
        Ok(block())
    } catch (e: Exception) {
        Err(e)
    }
}

/**
 * Converts an Option to a Result, using the provided error if None.
 */
fun <T, E> optionToResult(option: Option<T>, error: E): Result<T, E> =
    when (option) {
        is Some -> Ok(option.value)
        is None -> Err(error)
    }

/**
 * Converts an Option to a Result, using a lazy error factory if None.
 */
inline fun <T, E> optionToResult(option: Option<T>, errorFactory: () -> E): Result<T, E> =
    when (option) {
        is Some -> Ok(option.value)
        is None -> Err(errorFactory())
    }