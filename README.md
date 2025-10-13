# KResult: Expressive Result and Option Types for Kotlin ✨

[GitHub Repository](https://github.com/safwa1/KResult)  
[Maven Central](https://search.maven.org/artifact/pro.safwan/kresult)  
[MIT License](LICENSE)

**KResult** is a lightweight, zero-dependency Kotlin library that brings the power and elegance of Rust's `Result<T, E>` and `Option<T>` types to your Kotlin projects. It provides a robust and intuitive way to handle success, failure, and the absence of values without relying on exceptions or nullable types.

This library encourages a more functional, explicit, and predictable coding style, making your error handling and data flows easier to reason about and maintain.

## Table of Contents

- [Why KResult?](#why-kresult-)
- [Core Concepts](#core-concepts-)
    - [`Option<T>`: Handling Absence](#optiont-handling-absence)
    - [`Result<T, E>`: Handling Errors](#resultt-e-handling-errors)
- [Installation](#installation-)
- [Usage & Examples](#usage--examples-)
    - [Working with `Option<T>`](#working-with-optiont)
    - [Working with `Result<T, E>`](#working-with-resultt-e)
    - [Interoperability](#interoperability)
- [API Highlights](#api-highlights)
- [Contributing](#contributing-)
- [License](#license)

-----

## Why KResult? 🚀

While Kotlin's nullable types (`?`) and `try-catch` blocks are powerful, they can sometimes lead to verbose or unclear code. KResult offers an alternative with several key benefits:

* **Explicit by Design**: The type system forces you to handle potential failures (`Err`) or missing values (`None`), preventing `NullPointerException`s and unexpected exceptions at runtime.
* **Chainable & Fluent**: A rich set of extension functions (`map`, `andThen`, `orElse`, etc.) allows you to build clean, expressive, and resilient data processing pipelines.
* **Clear Intent**: Using `Result` makes it immediately obvious that a function can fail, and its signature tells you exactly what kind of error to expect.
* **No Overhead**: `Some`, `Ok`, and `Err` are implemented as `value class`es, meaning they are often unboxed at compile time, eliminating performance overhead in most cases.

-----

## Core Concepts 💡

### `Option<T>`: Handling Absence

An `Option<T>` represents a value that might be absent. It's an alternative to using nullable types (`T?`).

* `Some<T>`: Represents the presence of a value.
* `None`: Represents the absence of a value.

**Before (Nullable):**

```kotlin
fun findUser(id: Int): User? {
    // ... logic to find a user
}

val user = findUser(1)
val username = user?.name?.uppercase() ?: "GUEST"
```

**After (Option):**

```kotlin
fun findUser(id: Int): Option<User> {
    // ... logic to find a user
}

val username = findUser(1)
    .map { it.name.uppercase() }
    .unwrapOr("GUEST")
```

This approach allows you to chain operations on the potential value without manual null checks.

### `Result<T, E>`: Handling Errors

A `Result<T, E>` represents the outcome of an operation that can either succeed or fail. It's a powerful alternative to `try-catch` blocks for recoverable errors.

* `Ok<T>`: Represents a successful result, containing a value of type `T`.
* `Err<E>`: Represents a failure, containing an error of type `E`.

**Before (Exceptions):**

```kotlin
fun parsePort(portStr: String): Int {
    try {
        val port = portStr.toInt()
        if (port < 0 || port > 65535) {
            throw IllegalArgumentException("Port out of range")
        }
        return port
    } catch (e: NumberFormatException) {
        throw e // Or wrap it
    }
}
```

**After (Result):**

```kotlin
sealed interface PortError {
    data class NotANumber(val value: String) : PortError
    data class OutOfRange(val value: Int) : PortError
}

fun parsePort(portStr: String): Result<Int, PortError> {
    return portStr.toIntOrNull().toResult(PortError.NotANumber(portStr))
        .andThen { port ->
            if (port in 0..65535) port.ok()
            else PortError.OutOfRange(port).err()
        }
}
```

The function signature `Result<Int, PortError>` clearly communicates all possible outcomes.

-----

## Installation 📦

Add KResult to your `build.gradle.kts` or `build.gradle` file:

**Kotlin DSL (`build.gradle.kts`)**

```kotlin
dependencies {
    implementation("pro.safwan:kresult:0.1.0")
}
```

**Groovy DSL (`build.gradle`)**

```groovy
dependencies {
    implementation 'pro.safwan:kresult:0.1.0'
}
```

-----

## Usage & Examples 📚

### Working with `Option<T>`

#### Creating an `Option`

```kotlin
val someValue = 10.some() // Some(10)
val noValue = none<Int>() // None

// From nullable types
val user: User? = findUserById(1)
val userOption = user.toOption() // or user.asOption()
```

#### Accessing the Value

Avoid `unwrap()` in production code unless you're certain a value exists. Prefer safer alternatives.

```kotlin
val option = "hello".some()

// ✅ Safest: Provide a default value
val value = option.unwrapOr("default") // "hello"

// ✅ Also safe: Compute a default value lazily
val lazyValue = option.unwrapOrElse { computeDefault() }

// ✅ Pattern matching
option.match(
    onSome = { println("Value is $it") },
    onNone = { println("No value found") }
)

// 🚨 Unsafe: Throws if None
val riskyValue = option.unwrap() // "hello"
val riskyNone = none<String>().unwrap() // Throws IllegalStateException
```

#### Transforming and Chaining

`map` and `andThen` (also known as `flatMap`) are your best friends for building fluent pipelines.

```kotlin
// Example: Get the first even number from a list and return its string representation.
val numbers = listOf(1, 3, 5, 8, 10)

val result: Option<String> = numbers
    .firstOrNone { it % 2 == 0 } // Some(8)
    .map { "The first even number is $it" } // Some("The first even number is 8")
```

### Working with `Result<T, E>`

#### Creating a `Result`

```kotlin
fun processData(data: String): Result<Int, String> {
    if (data.isBlank()) {
        return "Data cannot be blank".err()
    }
    return data.length.ok()
}
```

You can also wrap existing code that might throw exceptions:

```kotlin
val result = Result.tryGet { "123".toInt() } // Ok(123)
val failedResult = Result.tryGet { "abc".toInt() } // Err(NumberFormatException)
```

#### Handling `Ok` and `Err`

`match` is the most idiomatic way to handle both cases.

```kotlin
val configResult = loadConfig() // Returns Result<Config, ConfigError>

configResult.match(
    ok = { config -> startServer(config) },
    err = { error -> log.error("Failed to load config: $error") }
)

if (configResult.isOk) {
    println("Config loaded!")
}
```

#### Transforming and Chaining

```kotlin
// A pipeline that reads, parses, and validates a user ID.
fun findUsername(userIdStr: String): Result<String, String> {
    return userIdStr.parse<Int>() // String.parse() is a handy extension
        .okOrElse { "ID is not a number" }
        .andThen { id -> findUser(id).okOr("User not found") }
        .map { user -> user.name.uppercase() }
}

findUsername("123").match(
    ok = { name -> println("Username: $name") }, // USERNAME: SAFWAN
    err = { error -> println("Error: $error") }
)
```

### Interoperability

KResult provides seamless conversions between `Option` and `Result`.

```kotlin
// Option -> Result
val option: Option<Int> = 42.some()
val result = option.okOr("Value was missing") // Ok(42)

val noOption: Option<Int> = none()
val resultFromNone = noOption.okOrElse { generateError() } // Err(...)

// Result -> Option
val resultOk: Result<Int, String> = 42.ok()
val optionFromOk = resultOk.ok() // Some(42)

val resultErr: Result<Int, String> = "Error".err()
val optionFromErr = resultErr.ok() // None
```

-----

## API Highlights

The library is packed with useful extension functions to cover a wide range of use cases.

| Category                | `Option<T>` Functions                                 | `Result<T, E>` Functions                               |
| ----------------------- | ----------------------------------------------------- | ------------------------------------------------------ |
| **Transformation** | `map`, `mapOr`, `mapOrElse`, `andThen`, `filter`      | `map`, `mapErr`, `mapOr`, `mapOrElse`, `andThen`, `orElse` |
| **Unwrapping** | `unwrap`, `unwrapOr`, `unwrapOrElse`, `expect`        | `unwrap`, `unwrapOr`, `unwrapOrElse`, `expect`, `unwrapErr`  |
| **Combining** | `zip`, `zipWith`, `or`, `and`, `xor`                  | `or`, `and`                                            |
| **Boolean Checks** | `isSome`, `isNone`, `isSomeAnd`, `contains`           | `isOk`, `isErr`, `isOkAnd`, `isErrAnd`, `contains`      |
| **Conversions** | `toNullable`, `asSequence`, `toOption`                | `toNullable`, `ok` (to Option), `err` (to Option)        |
| **Interoperability** | `okOr`, `okOrElse`                                    | `fromOption`                                           |
| **Collection Extensions** | `firstOrNone`, `findOrNone`, `getOrNone`, `values`    | `values`, `errors`, `partition`, `collectOk`           |
| **Validation** | -                                                     | `validate`, `validateAll`, `filter`                      |

-----

## Contributing 🤝

Contributions are welcome! If you find a bug, have a feature request, or want to improve the documentation, please feel free to open an issue or submit a pull request.

-----

## License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

