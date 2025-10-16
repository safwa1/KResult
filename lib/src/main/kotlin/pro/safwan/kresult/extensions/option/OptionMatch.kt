package pro.safwan.kresult.extensions.option

import pro.safwan.kresult.types.*

/**
 * Pattern matching on Option values.
 */
inline fun <T, R> Option<T>.match(
    onSome: (T) -> R,
    onNone: () -> R
): R = when (this) {
    is Some -> onSome(this.value)
    is None -> onNone()
}
