package pro.safwan.kresult


// ============================================================================
// Transforming Results
// ============================================================================

/**
 * Maps a Result<T, E> to Result<U, E> by applying a function to the contained Ok value.
 * Leaves an Err value untouched.
 */
inline fun <T, U, E> Result<T, E>.map(transform: (T) -> U): Result<U, E> = when (this) {
    is Ok -> Ok(transform(this.value))
    is Err -> this
}

/**
 * Maps a Result<T, E> to Result<T, F> by applying a function to the contained Err value.
 * Leaves an Ok value untouched.
 */
inline fun <T, E, F> Result<T, E>.mapErr(transform: (E) -> F): Result<T, F> = when (this) {
    is Ok -> this
    is Err -> Err(transform(this.error))
}

/**
 * Returns the provided default (if Err), or applies a function to the contained value (if Ok).
 */
inline fun <T, U, E> Result<T, E>.mapOr(defaultValue: U, transform: (T) -> U): U = when (this) {
    is Ok -> transform(this.value)
    is Err -> defaultValue
}

/**
 * Maps a Result by applying fallback function to an Err value, or transform function to an Ok value.
 */
inline fun <T, U, E> Result<T, E>.mapOrElse(defaultFactory: (E) -> U, transform: (T) -> U): U =
    when (this) {
        is Ok -> transform(this.value)
        is Err -> defaultFactory(this.error)
    }

/**
 * Returns the contained Ok value or throws an exception with a custom message.
 */
fun <T, E> Result<T, E>.expect(message: String): T = when (this) {
    is Ok -> this.value
    is Err -> when (val err = this.error) {
        is Exception -> throw IllegalStateException(message, err)
        else -> throw IllegalStateException("$message: $err")
    }
}

/**
 * Returns the contained Ok value or throws an exception.
 */
fun <T, E> Result<T, E>.unwrap(): T = when (this) {
    is Ok -> this.value
    is Err -> when (val err = this.error) {
        is Exception -> throw IllegalStateException("Could not unwrap a Result in the Err state.", err)
        else -> throw IllegalStateException("Could not unwrap a Result in the Err state: $err")
    }
}

/**
 * Returns the contained Err value or throws an exception with a custom message.
 */
fun <T, E> Result<T, E>.expectErr(message: String): E = when (this) {
    is Err -> this.error
    is Ok -> throw IllegalStateException("$message: ${this.value}")
}

/**
 * Returns the contained Err value or throws an exception.
 */
fun <T, E> Result<T, E>.unwrapErr(): E = when (this) {
    is Err -> this.error
    is Ok -> throw IllegalStateException("Expected the result to be in the Err state, but it was Ok: ${this.value}")
}

/**
 * Returns the contained Ok value or a provided default.
 */
fun <T, E> Result<T, E>.unwrapOr(default: T): T = when (this) {
    is Ok -> this.value
    is Err -> default
}

/**
 * Returns the contained Ok value or computes it from the error using the provided function.
 */
inline fun <T, E> Result<T, E>.unwrapOrElse(defaultFactory: (E) -> T): T = when (this) {
    is Ok -> this.value
    is Err -> defaultFactory(this.error)
}

/**
 * Returns the contained Ok value or null.
 */
fun <T, E> Result<T, E>.unwrapOrDefault(): T? = when (this) {
    is Ok -> this.value
    is Err -> null
}

/**
 * Converts from Result<Result<T, E>, E> to Result<T, E>.
 */
fun <T, E> Result<Result<T, E>, E>.flatten(): Result<T, E> = when (this) {
    is Ok -> this.value
    is Err -> this
}

// ============================================================================
// Combining Results
// ============================================================================

/**
 * Returns other if the result is Ok, otherwise returns the Err value of this.
 */
fun <T, U, E> Result<T, E>.and(other: Result<U, E>): Result<U, E> = when (this) {
    is Ok -> other
    is Err -> this
}

/**
 * Calls the function if the result is Ok, otherwise returns the Err value of this.
 * Also known as flatMap or bind.
 */
inline fun <T, U, E> Result<T, E>.andThen(transform: (T) -> Result<U, E>): Result<U, E> =
    when (this) {
        is Ok -> transform(this.value)
        is Err -> this
    }

/**
 * Returns this result if it contains a value, otherwise returns other.
 */
fun <T, E, F> Result<T, E>.or(other: Result<T, F>): Result<T, F> = when (this) {
    is Ok -> this
    is Err -> other
}

/**
 * Calls the function if the result is Err, otherwise returns the Ok value of this.
 */
inline fun <T, E, F> Result<T, E>.orElse(transform: (E) -> Result<T, F>): Result<T, F> =
    when (this) {
        is Ok -> this
        is Err -> transform(this.error)
    }

// ============================================================================
// Inspecting Results
// ============================================================================

/**
 * Calls the provided function with the Ok value if present, and returns the result unchanged.
 * Useful for side effects like logging.
 */
inline fun <T, E> Result<T, E>.inspect(action: (T) -> Unit): Result<T, E> {
    if (this is Ok) {
        action(this.value)
    }
    return this
}

/**
 * Calls the provided function with the Err value if present, and returns the result unchanged.
 * Useful for side effects like logging errors.
 */
inline fun <T, E> Result<T, E>.inspectErr(action: (E) -> Unit): Result<T, E> {
    if (this is Err) {
        action(this.error)
    }
    return this
}

/**
 * Returns true if the result is Ok and the value matches the predicate.
 */
inline fun <T, E> Result<T, E>.isOkAnd(predicate: (T) -> Boolean): Boolean = when (this) {
    is Ok -> predicate(this.value)
    is Err -> false
}

/**
 * Returns true if the result is Err and the error matches the predicate.
 */
inline fun <T, E> Result<T, E>.isErrAnd(predicate: (E) -> Boolean): Boolean = when (this) {
    is Err -> predicate(this.error)
    is Ok -> false
}

// ============================================================================
// Pattern Matching
// ============================================================================

/**
 * Pattern matching on Result values.
 * Applies the ok function if Ok, or the err function if Err.
 */
inline fun <T, E, R> Result<T, E>.match(
    ok: (T) -> R,
    err: (E) -> R
): R = when (this) {
    is Ok -> ok(this.value)
    is Err -> err(this.error)
}

/**
 * Pattern matching for side effects only.
 */
inline fun <T, E> Result<T, E>.match(
    ok: (T) -> Unit,
    err: (E) -> Unit
) {
    when (this) {
        is Ok -> ok(this.value)
        is Err -> err(this.error)
    }
}

// ============================================================================
// Conversions
// ============================================================================

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

// ============================================================================
// Operators
// ============================================================================

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

// ============================================================================
// Extension Functions
// ============================================================================

/**
 * Wraps this value in an Ok.
 */
fun <T> T.ok(): Result<T, Nothing> = Ok(this)

/**
 * Wraps this value in an Err.
 */
fun <E> E.err(): Result<Nothing, E> = Err(this)

/**
 * Converts a nullable value to a Result.
 * Non-null values become Ok, null becomes Err with the provided error.
 */
fun <T, E> T?.toResult(error: E): Result<T, E> =
    if (this != null) Ok(this) else Err(error)

/**
 * Converts a nullable value to a Result using a lazy error factory.
 */
inline fun <T, E> T?.toResult(errorFactory: () -> E): Result<T, E> =
    if (this != null) Ok(this) else Err(errorFactory())

// ============================================================================
// LINQ-style Extensions
// ============================================================================

/**
 * LINQ-compatible select function for result values.
 * Alias for map.
 */
inline fun <T, U, E> Result<T, E>.select(transform: (T) -> U): Result<U, E> = map(transform)

/**
 * LINQ-compatible selectMany for chaining result operations.
 */
inline fun <T, U, V, E> Result<T, E>.selectMany(
    binder: (T) -> Result<U, E>,
    projector: (T, U) -> V
): Result<V, E> = andThen { t ->
    binder(t).map { u -> projector(t, u) }
}

/**
 * Filters a result based on a predicate.
 * If the predicate fails, returns an Err with the provided error.
 */
inline fun <T, E> Result<T, E>.filter(
    predicate: (T) -> Boolean,
    error: E
): Result<T, E> = when (this) {
    is Ok -> if (predicate(this.value)) this else Err(error)
    is Err -> this
}

/**
 * Filters a result based on a predicate with a lazy error factory.
 */
inline fun <T, E> Result<T, E>.filter(
    predicate: (T) -> Boolean,
    errorFactory: () -> E
): Result<T, E> = when (this) {
    is Ok -> if (predicate(this.value)) this else Err(errorFactory())
    is Err -> this
}

// ============================================================================
// Validation Extensions
// ============================================================================

/**
 * Validates a value against multiple validators and collects all errors.
 */
inline fun <T, E> T.validateAll(
    vararg validators: (T) -> Result<T, E>
): Result<T, List<E>> {
    val errors = mutableListOf<E>()
    for (validator in validators) {
        when (val result = validator(this)) {
            is Err -> errors.add(result.error)
            is Ok -> { /* continue */ }
        }
    }
    return if (errors.isEmpty()) {
        Ok(this)
    } else {
        Err(errors)
    }
}

/**
 * Validates a value against a predicate.
 */
inline fun <T> T.validate(
    predicate: (T) -> Boolean,
    errorMessage: String
): Result<T, String> =
    if (predicate(this)) Ok(this) else Err(errorMessage)

/**
 * Validates a value against a predicate with a custom error.
 */
inline fun <T, E> T.validate(
    predicate: (T) -> Boolean,
    error: E
): Result<T, E> =
    if (predicate(this)) Ok(this) else Err(error)

// ============================================================================
// Collection Extensions
// ============================================================================

/**
 * Flattens a sequence of Results into a sequence containing all Ok values.
 * Err values are discarded.
 */
fun <T, E> Sequence<Result<T, E>>.values(): Sequence<T> =
    mapNotNull { it.toNullable() }

/**
 * Flattens an iterable of Results into a list containing all Ok values.
 * Err values are discarded.
 */
fun <T, E> Iterable<Result<T, E>>.values(): List<T> =
    mapNotNull { it.toNullable() }

/**
 * Flattens a sequence of Results into a sequence containing all Err values.
 * Ok values are discarded.
 */
fun <T, E> Sequence<Result<T, E>>.errors(): Sequence<E> =
    mapNotNull { if (it is Err) it.error else null }

/**
 * Flattens an iterable of Results into a list containing all Err values.
 * Ok values are discarded.
 */
fun <T, E> Iterable<Result<T, E>>.errors(): List<E> =
    mapNotNull { if (it is Err) it.error else null }

/**
 * Applies a function to each element and returns a sequence of values inside any Ok results.
 */
inline fun <T, U, E> Sequence<T>.mapNotErr(crossinline transform: (T) -> Result<U, E>): Sequence<U> =
    mapNotNull { transform(it).toNullable() }

/**
 * Applies a function to each element and returns a list of values inside any Ok results.
 */
inline fun <T, U, E> Iterable<T>.mapNotErr(transform: (T) -> Result<U, E>): List<U> =
    mapNotNull { transform(it).toNullable() }

/**
 * Partitions a sequence of Results into two lists: Ok values and Err values.
 */
fun <T, E> Sequence<Result<T, E>>.partition(): Pair<List<T>, List<E>> {
    val oks = mutableListOf<T>()
    val errs = mutableListOf<E>()
    forEach { result ->
        when (result) {
            is Ok -> oks.add(result.value)
            is Err -> errs.add(result.error)
        }
    }
    return oks to errs
}

/**
 * Partitions an iterable of Results into two lists: Ok values and Err values.
 */
fun <T, E> Iterable<Result<T, E>>.partition(): Pair<List<T>, List<E>> {
    val oks = mutableListOf<T>()
    val errs = mutableListOf<E>()
    forEach { result ->
        when (result) {
            is Ok -> oks.add(result.value)
            is Err -> errs.add(result.error)
        }
    }
    return oks to errs
}

/**
 * Collects all Ok values if the sequence contains no Err values.
 * If any Err is found, returns the first Err.
 */
fun <T, E> Sequence<Result<T, E>>.collectOk(): Result<List<T>, E> {
    val values = mutableListOf<T>()
    for (result in this) {
        when (result) {
            is Ok -> values.add(result.value)
            is Err -> return result
        }
    }
    return Ok(values)
}

/**
 * Collects all Ok values if the iterable contains no Err values.
 * If any Err is found, returns the first Err.
 */
fun <T, E> Iterable<Result<T, E>>.collectOk(): Result<List<T>, E> {
    val values = mutableListOf<T>()
    for (result in this) {
        when (result) {
            is Ok -> values.add(result.value)
            is Err -> return result
        }
    }
    return Ok(values)
}

// ============================================================================
// Option Interop Extensions
// ============================================================================

/**
 * Transforms an Option<T> into a Result<T, E>, mapping Some to Ok and None to Err.
 */
fun <T, E> Option<T>.okOr(error: E): Result<T, E> = when (this) {
    is Some -> Ok(this.value)
    is None -> Err(error)
}

/**
 * Transforms an Option<T> into a Result<T, E> with a lazy error factory.
 */
inline fun <T, E> Option<T>.okOrElse(errorFactory: () -> E): Result<T, E> = when (this) {
    is Some -> Ok(this.value)
    is None -> Err(errorFactory())
}

/**
 * Transposes an Option<Result<T, E>> into a Result<Option<T>, E>.
 * None becomes Ok(None).
 * Some(Ok(v)) becomes Ok(Some(v)).
 * Some(Err(e)) becomes Err(e).
 */
fun <T, E> Option<Result<T, E>>.transpose(): Result<Option<T>, E> = when (this) {
    is Some -> when (val result = this.value) {
        is Ok -> Ok(Some(result.value))
        is Err -> Err(result.error)
    }
    is None -> Ok(None)
}