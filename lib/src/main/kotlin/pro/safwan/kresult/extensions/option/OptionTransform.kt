package pro.safwan.kresult.extensions.option

import pro.safwan.kresult.types.*


/**
 * Maps an Option<T> to Option<U> by applying a function to a contained value.
 */
inline fun <T, U> Option<T>.map(transform: (T) -> U): Option<U> = when (this) {
    is Some -> Some(transform(this.value))
    is None -> None
}

/**
 * Returns the provided default result (if None), or applies a function to the contained value (if Some).
 */
inline fun <T, U> Option<T>.mapOr(defaultValue: U, mapper: (T) -> U): U =
    when (this) {
        is Some -> mapper(this.value)
        None -> defaultValue
    }

/**
 * Computes a default function result (if None), or applies a different function to the contained value (if Some).
 */
inline fun <T, U> Option<T>.mapOrElse(defaultFactory: () -> U, mapper: (T) -> U): U =
    when (this) {
        is Some -> mapper(this.value)
        None -> defaultFactory()
    }

/**
 * Returns the contained Some value or throws an exception with a custom message.
 */
fun <T> Option<T>.expect(message: String): T = when (this) {
    is Some -> this.value
    None -> throw IllegalStateException(message)
}

/**
 * Returns the contained Some value or throws an exception.
 */
fun <T> Option<T>.unwrap(): T = when (this) {
    is Some -> this.value
    None -> throw IllegalStateException("Called unwrap on a None value")
}

/**
 * Returns the contained Some value or a provided default.
 */
fun <T> Option<T>.unwrapOr(default: T): T = when (this) {
    is Some -> this.value
    is None -> default
}

/**
 * Returns the contained Some value or computes it from a closure.
 */
inline fun <T> Option<T>.unwrapOrElse(defaultFactory: () -> T): T = when (this) {
    is Some -> this.value
    None -> defaultFactory()
}

/**
 * Converts from Option<Option<T>> to Option<T>.
 */
fun <T> Option<Option<T>>.flatten(): Option<T> = when (this) {
    is Some -> this.value
    None -> None
}

/**
 * Returns None if the option is None, otherwise calls predicate with the wrapped value and returns:
 * - Some(t) if predicate returns true
 * - None if predicate returns false
 */
inline fun <T> Option<T>.filter(predicate: (T) -> Boolean): Option<T> = when (this) {
    is Some -> if (predicate(this.value)) this else None
    None -> None
}