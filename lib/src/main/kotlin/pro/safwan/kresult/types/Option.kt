package pro.safwan.kresult.types

/**
 * Represents an optional value: every Option is either Some and contains a value, or None, and does not.
 * Inspired by Rust's Option<T> type.
 */
sealed interface Option<out T> {

    val isNone: Boolean get() = this is None
    val isSome: Boolean get() = this is Some

    /**
     * Returns true if the option is a Some value containing the given value.
     */
    fun contains(value: @UnsafeVariance T): Boolean = when (this) {
        is Some -> this.value == value
        None -> false
    }
}

/**
 * Some value of type T.
 */
@JvmInline
value class Some<T>(val value: T) : Option<T> {
    override fun toString(): String = "Some($value)"
}

/**
 * No value.
 */
object None : Option<Nothing> {
    override fun toString(): String = "None"
}
