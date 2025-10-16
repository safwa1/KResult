package pro.safwan.kresult.extensions.result

import pro.safwan.kresult.types.*


/**
 * Converts the Ok value to an Option, discarding any error.
 */
fun <T, E> Result<T, E>.ok(): Option<T> = when (this) {
    is Ok -> Some(this.value)
    is Err -> None
}

/**
 * Converts the Err value to an Option, discarding any success value.
 */
fun <T, E> Result<T, E>.err(): Option<E> = when (this) {
    is Err -> Some(this.error)
    is Ok -> None
}

/**
 * Transposes a Result of an Option into an Option of a Result.
 * Ok(None) becomes None.
 * Ok(Some(v)) becomes Some(Ok(v)).
 * Err(e) becomes Some(Err(e)).
 */
fun <T, E> Result<Option<T>, E>.transpose(): Option<Result<T, E>> = when (this) {
    is Ok -> when (val opt = this.value) {
        is Some -> Some(Ok(opt.value))
        is None -> None
    }
    is Err -> Some(Err(this.error))
}

/**
 * Converts the result to a nullable value.
 * Returns the Ok value or null if Err.
 */
fun <T, E> Result<T, E>.toNullable(): T? = when (this) {
    is Ok -> this.value
    is Err -> null
}

/**
 * Converts the result to a Sequence.
 */
fun <T, E> Result<T, E>.asSequence(): Sequence<T> = when (this) {
    is Ok -> sequenceOf(this.value)
    is Err -> emptySequence()
}

/**
 * Converts the result to an Iterable.
 */
fun <T, E> Result<T, E>.asIterable(): Iterable<T> = when (this) {
    is Ok -> listOf(this.value)
    is Err -> emptyList()
}
