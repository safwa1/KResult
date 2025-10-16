package pro.safwan.kresult.extensions.result

import pro.safwan.kresult.types.*

/**
 * Validates a value against multiple validators and collects all errors.
 */
fun <T, E> T.validateAll(
    vararg validators: (T) -> Result<T, E>
): Result<T, List<E>> {
    val errors = mutableListOf<E>()
    for (validator in validators) {
        when (val result = validator(this)) {
            is Err -> errors.add(result.error)
            is Ok -> { /* continue */ }
        }
    }
    return if (errors.isEmpty()) {
        Ok(this)
    } else {
        Err(errors)
    }
}

/**
 * Validates a value against a predicate.
 */
inline fun <T> T.validate(
    predicate: (T) -> Boolean,
    errorMessage: String
): Result<T, String> =
    if (predicate(this)) Ok(this) else Err(errorMessage)

/**
 * Validates a value against a predicate with a custom error.
 */
inline fun <T, E> T.validate(
    predicate: (T) -> Boolean,
    error: E
): Result<T, E> =
    if (predicate(this)) Ok(this) else Err(error)
