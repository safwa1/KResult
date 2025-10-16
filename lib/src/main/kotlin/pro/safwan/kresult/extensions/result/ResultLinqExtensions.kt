package pro.safwan.kresult.extensions.result

import pro.safwan.kresult.types.*

/**
 * LINQ-compatible select function for result values.
 * Alias for map.
 */
inline fun <T, U, E> Result<T, E>.select(transform: (T) -> U): Result<U, E> = map(transform)

/**
 * LINQ-compatible selectMany for chaining result operations.
 */
inline fun <T, U, V, E> Result<T, E>.selectMany(
    binder: (T) -> Result<U, E>,
    projector: (T, U) -> V
): Result<V, E> = andThen { t ->
    binder(t).map { u -> projector(t, u) }
}

/**
 * Filters a result based on a predicate.
 * If the predicate fails, returns an Err with the provided error.
 */
inline fun <T, E> Result<T, E>.filter(
    predicate: (T) -> Boolean,
    error: E
): Result<T, E> = when (this) {
    is Ok -> if (predicate(this.value)) this else Err(error)
    is Err -> this
}

/**
 * Filters a result based on a predicate with a lazy error factory.
 */
inline fun <T, E> Result<T, E>.filter(
    predicate: (T) -> Boolean,
    errorFactory: () -> E
): Result<T, E> = when (this) {
    is Ok -> if (predicate(this.value)) this else Err(errorFactory())
    is Err -> this
}
