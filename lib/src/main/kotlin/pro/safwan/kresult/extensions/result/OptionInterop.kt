package pro.safwan.kresult.extensions.result

import pro.safwan.kresult.types.*

/**
 * Transforms an Option<T> into a Result<T, E>, mapping Some to Ok and None to Err.
 */
fun <T, E> Option<T>.okOr(error: E): Result<T, E> = when (this) {
    is Some -> Ok(this.value)
    is None -> Err(error)
}

/**
 * Transforms an Option<T> into a Result<T, E> with a lazy error factory.
 */
inline fun <T, E> Option<T>.okOrElse(errorFactory: () -> E): Result<T, E> = when (this) {
    is Some -> Ok(this.value)
    is None -> Err(errorFactory())
}

/**
 * Transposes an Option<Result<T, E>> into a Result<Option<T>, E>.
 * None becomes Ok(None).
 * Some(Ok(v)) becomes Ok(Some(v)).
 * Some(Err(e)) becomes Err(e).
 */
fun <T, E> Option<Result<T, E>>.transpose(): Result<Option<T>, E> = when (this) {
    is Some -> when (val result = this.value) {
        is Ok -> Ok(Some(result.value))
        is Err -> Err(result.error)
    }
    is None -> Ok(None)
}