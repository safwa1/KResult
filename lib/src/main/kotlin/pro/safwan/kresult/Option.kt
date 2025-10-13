package pro.safwan.kresult

import java.text.NumberFormat

fun interface TryGet<T> {
    operator fun invoke(): Pair<Boolean, T>
}

fun interface TryGetValue<K, V> {
    operator fun invoke(key: K): Pair<Boolean, V>
}

/**
 * Represents an optional value: every Option is either Some and contains a value, or None, and does not.
 * Inspired by Rust's Option<T> type.
 */
sealed interface Option<out T> {

    companion object {
        /**
         * Creates a Some variant containing the given value.
         */
        fun <T> some(value: T): Option<T> = Some(value)

        /**
         * Creates an Option from a nullable value.
         * Returns Some if value is non-null, None otherwise.
         */
        fun <T> create(value: T?): Option<T> =
            value?.let { Some(it) } ?: None

        /**
         * Returns a None variant.
         */
        fun <T> none(): Option<T> = None

        /**
         * Attempts to get a value using the provided function.
         * Returns Some if successful, None if the function returns false or throws.
         */
        fun <T> tryGet(tryGet: TryGet<T>): Option<T> {
            return try {
                val (success, value) = tryGet()
                if (success) Some(value) else None
            } catch (_: Exception) {
                None
            }
        }

        /**
         * Binds a dictionary-style lookup function to return Options.
         */
        fun <K : Any, V : Any> bind(tryGetValue: TryGetValue<K, V>): (K) -> Option<V> = { key ->
            val (success, value) = tryGetValue(key)
            if (success) Some(value) else None
        }

        /**
         * Parses a string to the specified type.
         * Supports: Int, Long, Double, Float, Short, Byte, Boolean
         */
        inline fun <reified T : Any> parse(value: String): Option<T> {
            return try {
                val parsed: T = when (T::class) {
                    Int::class -> value.toInt() as T
                    Long::class -> value.toLong() as T
                    Double::class -> value.toDouble() as T
                    Float::class -> value.toFloat() as T
                    Short::class -> value.toShort() as T
                    Byte::class -> value.toByte() as T
                    Boolean::class -> value.toBoolean() as T
                    else -> throw IllegalArgumentException("Unsupported parse type: ${T::class.simpleName}")
                }
                Some(parsed)
            } catch (e: Exception) {
                None
            }
        }

        /**
         * Parses a string to the specified numeric type using a NumberFormat.
         * Supports: Int, Long, Double, Float, Short, Byte
         */
        inline fun <reified T : Any> parse(value: String, format: NumberFormat): Option<T> {
            return try {
                val number = format.parse(value) ?: return None
                val parsed: T = when (T::class) {
                    Int::class -> number.toInt() as T
                    Long::class -> number.toLong() as T
                    Double::class -> number.toDouble() as T
                    Float::class -> number.toFloat() as T
                    Short::class -> number.toShort() as T
                    Byte::class -> number.toByte() as T
                    else -> throw IllegalArgumentException("Unsupported parse type: ${T::class.simpleName}")
                }
                Some(parsed)
            } catch (_: Exception) {
                None
            }
        }

        /**
         * Parses an enum value from a string.
         * @param ignoreCase If true, performs case-insensitive matching
         */
        inline fun <reified T : Enum<T>> parseEnum(value: String, ignoreCase: Boolean = false): Option<T> {
            return runCatching {
                if (ignoreCase) {
                    enumValues<T>().firstOrNull {
                        it.name.equals(value, ignoreCase = true)
                    } ?: throw IllegalArgumentException()
                } else {
                    enumValueOf<T>(value)
                }
            }.getOrNull()?.let { Some(it) } ?: None
        }
    }

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
