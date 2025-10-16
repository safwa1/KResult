package pro.safwan.kresult.extensions.option

import pro.safwan.kresult.types.*

/**
 * Returns a substring as an Option.
 * Returns None if the indices are out of bounds.
 */
fun String.substringOrNone(startIndex: Int, endIndex: Int = length): Option<String> =
    try {
        substring(startIndex, endIndex).toOption()
    } catch (e: IndexOutOfBoundsException) {
        None
    }

/**
 * Returns a substring before the first occurrence of delimiter as an Option.
 * Returns None if the delimiter is not found.
 */
fun String.substringBeforeOrNone(delimiter: String, missingDelimiterValue: String? = null): Option<String> {
    val index = indexOf(delimiter)
    return if (index == -1) {
        missingDelimiterValue.toOption()
    } else {
        substring(0, index).toOption()
    }
}

/**
 * Returns a substring after the first occurrence of delimiter as an Option.
 * Returns None if the delimiter is not found.
 */
fun String.substringAfterOrNone(delimiter: String, missingDelimiterValue: String? = null): Option<String> {
    val index = indexOf(delimiter)
    return if (index == -1) {
        missingDelimiterValue.toOption()
    } else {
        substring(index + delimiter.length).toOption()
    }
}
