package pro.safwan.kresult.extensions.result

import pro.safwan.kresult.types.*

/**
 * Pattern matching on Result values.
 * Applies the ok function if Ok, or the err function if Err.
 */
inline fun <T, E, R> Result<T, E>.match(
    ok: (T) -> R,
    err: (E) -> R
): R = when (this) {
    is Ok -> ok(this.value)
    is Err -> err(this.error)
}

/**
 * Pattern matching for side effects only.
 */
inline fun <T, E> Result<T, E>.match(
    ok: (T) -> Unit,
    err: (E) -> Unit
) {
    when (this) {
        is Ok -> ok(this.value)
        is Err -> err(this.error)
    }
}
