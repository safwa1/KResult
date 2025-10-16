package pro.safwan.kresult.extensions.result

import pro.safwan.kresult.types.*

/**
 * Wraps this value in an Ok.
 */
fun <T> T.ok(): Result<T, Nothing> = Ok(this)

/**
 * Wraps this value in an Err.
 */
fun <E> E.err(): Result<Nothing, E> = Err(this)

/**
 * Converts a nullable value to a Result.
 * Non-null values become Ok, null becomes Err with the provided error.
 */
fun <T, E> T?.toResult(error: E): Result<T, E> =
    if (this != null) Ok(this) else Err(error)

/**
 * Converts a nullable value to a Result using a lazy error factory.
 */
inline fun <T, E> T?.toResult(errorFactory: () -> E): Result<T, E> =
    if (this != null) Ok(this) else Err(errorFactory())