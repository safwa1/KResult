package pro.safwan.kresult.extensions.option

import pro.safwan.kresult.types.*

/**
 * Converts the Option to a Sequence.
 */
fun <T> Option<T>.asSequence(): Sequence<T> = when (this) {
    is Some -> sequenceOf(this.value)
    is None -> emptySequence()
}

/**
 * Converts the Option to an Iterable.
 */
fun <T> Option<T>.asIterable(): Iterable<T> = when (this) {
    is Some -> listOf(this.value)
    is None -> emptyList()
}

/**
 * Creates a nullable value from the Option.
 */
fun <T> Option<T>.toNullable(): T? = when (this) {
    is Some -> value
    is None -> null
}

/**
 * Creates an Option from a nullable value.
 */
fun <T> T?.toOption(): Option<T> = if (this != null) Some(this) else None

/**
 * Alias for toOption().
 */
fun <T> T?.asOption(): Option<T> = toOption()