package pro.safwan.kresult.extensions.option

import pro.safwan.kresult.core.parseEnumOption
import pro.safwan.kresult.core.parseOption
import pro.safwan.kresult.types.*


/**
 * Wraps this value in a Some.
 */
fun <T> T.some(): Option<T> = Some(this)

/**
 * Returns None.
 */
fun <T> none(): Option<T> = None

/**
 * If this Boolean is true, returns Some(thenFunc()), otherwise None.
 */
inline fun <T> Boolean.then(thenFunc: () -> T): Option<T> =
    if (this) Some(thenFunc()) else None

/**
 * If this Boolean is true, returns Some(value), otherwise None.
 */
fun <T> Boolean.thenSome(value: T): Option<T> =
    if (this) Some(value) else None

/**
 * Parses this string to the specified type.
 */
inline fun <reified T : Any> String.parse(): Option<T> {
    return parseOption(this)
}

/**
 * Parses this string as an enum value.
 */
inline fun <reified T : Enum<T>> String.parseEnum(ignoreCase: Boolean = false): Option<T> {
    return parseEnumOption(this, ignoreCase)
}