package pro.safwan.kresult.extensions.option

import pro.safwan.kresult.types.*


operator fun <T> Option<T>.iterator(): Iterator<T> = when (this) {
    is Some -> listOf(this.value).iterator()
    is None -> emptyList<T>().iterator()
}

/**
 * Destructuring: isSome
 */
operator fun <T> Option<T>.component1(): Boolean = this is Some

/**
 * Destructuring: value or null
 */
operator fun <T> Option<T>.component2(): T? = when (this) {
    is Some -> value
    is None -> null
}

/**
 * Returns true if Option is None (unary not operator).
 */
operator fun <T> Option<T>.not(): Boolean = isNone
