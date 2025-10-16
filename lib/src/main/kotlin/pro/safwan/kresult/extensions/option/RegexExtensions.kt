package pro.safwan.kresult.extensions.option

import pro.safwan.kresult.types.*

/**
 * Returns the first match of the regex as an Option.
 */
fun Regex.findOrNone(input: CharSequence, startIndex: Int = 0): Option<MatchResult> =
    find(input, startIndex).toOption()

/**
 * Returns the entire match result as an Option if the regex matches.
 */
fun Regex.matchEntireOrNone(input: CharSequence): Option<MatchResult> =
    matchEntire(input).toOption()