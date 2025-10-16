package pro.safwan.kresult.extensions.result

import pro.safwan.kresult.types.*

operator fun <T, E> Result<T, E>.iterator(): Iterator<T> = when (this) {
    is Ok -> listOf(this.value).iterator()
    is Err -> emptyList<T>().iterator()
}

/**
 * Destructuring: isOk
 */
operator fun <T, E> Result<T, E>.component1(): Boolean = this is Ok

/**
 * Destructuring: value or null
 */
operator fun <T, E> Result<T, E>.component2(): T? = when (this) {
    is Ok -> value
    is Err -> null
}

/**
 * Destructuring: error or null
 */
operator fun <T, E> Result<T, E>.component3(): E? = when (this) {
    is Err -> error
    is Ok -> null
}

/**
 * Returns true if Result is Err (unary not operator).
 */
operator fun <T, E> Result<T, E>.not(): Boolean = isErr
