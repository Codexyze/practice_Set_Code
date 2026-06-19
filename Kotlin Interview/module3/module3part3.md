# Module 3 — Functional Kotlin

## Chapter 3.3 — Higher Order Functions & Function Types Mastery

### Why This Chapter Matters

This chapter is one of the most important chapters in Kotlin.

Because these things:

```kotlin
list.map { }
list.filter { }

Button(
    onClick = {}
)

LaunchedEffect { }

viewModelScope.launch { }

flow.collect { }
```

all rely on:

*   Function Types
*   Higher Order Functions
*   Passing Functions
*   Returning Functions

If you understand this chapter deeply, a huge amount of Kotlin suddenly stops looking like magic.

---

### Part 1 — Function Types Deep Dive

In the previous chapter, we saw: `(Int) -> Int`. Let's understand it properly.

**Theory**

A function type describes:
*   Input Types
*   Output Type

**Example:** `(Int) -> Int` means:
*   Accepts `Int`
*   Returns `Int`

**Visual**

```text
 Input      Output

  Int   ->    Int
```

**Example 1: Single Parameter**

```kotlin
val square: (Int) -> Int = { number ->
    number * number
}

fun main() {
    println(square(5))
}
```

Output: `25`

**Line By Line**
*   `val square:` - A variable.
*   `(Int) -> Int` - The function contract. The compiler expects a function that takes an `Int` and returns an `Int`.
*   `{ number -> ... }` - The lambda that fulfills the contract.

**Example 2: Multiple Parameters**

```kotlin
val add: (Int, Int) -> Int = { a, b ->
    a + b
}
```

**Visual**

```text
(Int, Int)
    |
    |
    V
   Int
```

Usage:

```kotlin
println(add(10, 20)) // Output: 30
```

**Example 3: No Parameters**

```kotlin
val greeting: () -> Unit = {
    println("Hello")
}
```

Meaning:
*   Input: Nothing
*   Output: Nothing (`Unit`)

This is used constantly in Compose. The `onClick` parameter for a `Button` has the type `() -> Unit`.

---

### Part 2 — Functions As Parameters (Higher-Order Functions)

This is where Higher-Order Functions begin.

A normal function has a fixed behavior:

```kotlin
fun add(a: Int, b: Int): Int {
    return a + b // Always adds
}
```

Question: Can we make the behavior configurable? Yes.

**First Higher-Order Function**

```kotlin
fun calculate(
    a: Int,
    b: Int,
    // This parameter is a function itself
    operation: (Int, Int) -> Int
): Int {
    // Execute the function that was passed in
    return operation(a, b)
}
```

**Line By Line**
*   `operation: (Int, Int) -> Int` - The parameter is a function, not an `Int` or `String`.
*   `return operation(a, b)` - We execute the incoming function.

**Usage**

```kotlin
fun main() {
    // Pass addition behavior
    val sum = calculate(10, 20) { x, y -> x + y }
    println(sum) // Output: 30

    // Pass multiplication behavior to the SAME function
    val product = calculate(10, 20) { x, y -> x * y }
    println(product) // Output: 200
}
```

This is extremely powerful. The `calculate` function's logic is decoupled from the specific operation it performs.

**Android Analogy**

A `Button` is a higher-order function. It doesn't know if its `onClick` should log in, log out, delete, or share. It just receives a behavior (`() -> Unit`) and executes it when tapped.

---

### Part 3 — Returning Functions

**Interview favorite.** Most developers never practice this.

**Working Example**

```kotlin
// This function doesn't return a value, it returns another function
fun createMultiplier(): (Int) -> Int {
    return { number ->
        number * 10
    }
}

fun main() {
    // 'multiply' is now a function of type (Int) -> Int
    val multiply = createMultiplier()

    // Now we can call the returned function
    println(multiply(5))
}
```

Output: `50`

**Line By Line**
*   `fun createMultiplier(): (Int) -> Int` - The return type is a function.
*   `return { number -> ... }` - We return a lambda that matches the required function type.

**Visual Flow**

`createMultiplier()` -> returns a function `(Int) -> Int` -> `multiply(5)` -> executes the returned function -> `50`

This concept is used internally by Flow operators, Compose, and other advanced libraries.

---

### Part 4 — `typealias` for Cleaner Code

**Interview topic.** Sometimes function types become ugly and hard to read.

**Problem:**

```kotlin
fun processUser(callback: (Int, String, Boolean) -> Unit) { /*...*/ }
```

**Solution with `typealias`:**

```kotlin
// Create a readable alias for the complex function type
typealias UserCallback = (Int, String, Boolean) -> Unit

// Now use the alias
fun processUser(callback: UserCallback) { /*...*/ }
```

This makes APIs much cleaner and more self-documenting.

---

### Part 5 — The `invoke` Operator

**Interview favorite.** Every lambda internally has an `invoke()` method.

This code:

```kotlin
val square = { x: Int -> x * x }
println(square(5))
```

Is actually compiled to this:

```kotlin
println(square.invoke(5))
```

**Custom `invoke`**

We can add this operator to our own classes to make them callable like functions.

```kotlin
class LoginUseCase {
    operator fun invoke(username: String) {
        println("Login $username")
    }
}

fun main() {
    val login = LoginUseCase()
    login("Akshay") // This calls the invoke method!
}
```

Output: `Login Akshay`

This is the foundation of the "UseCase" pattern in Clean Architecture, making them feel like actions rather than objects with an `execute` method.

---

### Part 6 — How `map()` Works Internally

Most developers use `list.map { }` without understanding it. Let's demystify it.

**Usage:**

```kotlin
val numbers = listOf(1, 2, 3)
val result = numbers.map { it * 2 } // [2, 4, 6]
```

**Conceptual Implementation:**

```kotlin
fun <T, R> List<T>.myMap(transform: (T) -> R): List<R> {
    val result = mutableListOf<R>()
    for (item in this) {
        // Apply the transformation to each item
        val transformedItem = transform(item)
        result.add(transformedItem)
    }
    return result
}
```

Suddenly, `map` doesn't look magical anymore. It's just a higher-order function that takes a list and a transformation function.

---

## Common Interview Questions

**What Is a Higher-Order Function?**
A function that accepts a function as a parameter or returns a function.

**What Is a Function Type?**
The signature of a function, e.g., `(Int) -> String`, which defines its input and output types.

**What Is `invoke()`?**
The function call operator. It allows an object to be called as if it were a function.

**Why Does `useCase()` Work?**
Because the `useCase` object has an `operator fun invoke()` defined.

**What Is `typealias`?**
It creates a readable alias (a new name) for an existing complex type, like a function type.

---

## Exercises

**1. Predict Output**

```kotlin
fun execute(action: () -> Unit) {
    action()
}

fun main() {
    execute {
        println("Hello")
    }
}
```

**2. What Is The Type?**

```kotlin
val multiply = { a: Int, b: Int ->
    a * b
}
```

**3. Why Does This Compile?**

```kotlin
class LoginUseCase {
    operator fun invoke() {
        println("Login")
    }
}

// Usage:
LoginUseCase()()
```
Explain completely.