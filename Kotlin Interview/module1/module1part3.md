# Module 1 — Core Kotlin Language

## Chapter 1.3 — Functions Deep Dive

This chapter is one of the most important chapters in Kotlin.

Why?

Because when you look at modern Android architecture:

* Repository
* UseCase
* ViewModel
* Navigator
* Mapper
* Validator
* Formatter

Almost everything is built around functions.

A senior engineer often thinks:

*What function should exist?*

instead of

*What class should I create?*

### Part 1 — What Is a Function?

A function is:

A reusable block of behavior that can accept input and produce output.

Think of it like a machine.

Input:

`2`
`3`

Machine:

```kotlin
fun add(a: Int, b: Int): Int {
    return a + b
}
```

Output:

`5`

Visual:

```text
2
 \
  \
   --> add() --> 5
  /
 /
3
```

**Why Functions Exist**

Without functions:

```kotlin
fun main() {

    val total1 = 10 + 20

    println(total1)

    val total2 = 5 + 8

    println(total2)
}
```

Logic duplicated.

With function:

```kotlin
fun add(
    a: Int,
    b: Int
): Int {

    return a + b
}
```

Usage:

```kotlin
println(add(10,20))
println(add(5,8))
```

Reusable.

**Android Example**

Without function:

```kotlin
button.text = "Loading..."
button.isEnabled = false
```

Repeated everywhere.

Create:

```kotlin
fun showLoadingButton(
    button: Button
) {
    button.text = "Loading..."
    button.isEnabled = false
}
```

Reuse.

**Function Anatomy**

Example:

```kotlin
fun add(
    a: Int,
    b: Int
): Int {

    return a + b
}
```

Let's dissect.

`fun`

Keyword for function declaration.

`fun`

means:

*I am declaring behavior*

`add`

Function name.

`add`

Should describe behavior.

Bad:

```kotlin
fun process()
```

Good:

```kotlin
fun calculateDiscount()
```

Better:

```kotlin
fun calculateFinalPrice()
```

**Parameters**

`a: Int`
`b: Int`

These are parameters.

Think:

*Inputs expected by function*

**Return Type**

`: Int`

Meaning:

*Function returns Int*

**Return Statement**

`return a + b`

Value sent back to caller.

Visual:

```text
Input
   |
   V

Function

   |
   V

Output
```

**Parameter vs Argument**

Interview favorite.

Function:

```kotlin
fun greet(
    name: String
)
```

Here:

`name`

is parameter.

Call:

```kotlin
greet("Akshay")
```

Here:

`"Akshay"`

is argument.

**Easy Rule**

Definition:

*Parameter*

Execution:

*Argument*

### Part 2 — Unit

Java:

`void`

Kotlin:

`Unit`

Example:

```kotlin
fun showMessage() {
    println("Hello")
}
```

Compiler sees:

```kotlin
fun showMessage(): Unit {
    println("Hello")
}
```

**Why Unit Exists**

Unlike Java's void:

`Unit`

is an actual type.

Can be stored.
Can be passed.
Can be returned.

**Interview Question**

*Is Unit equivalent to void?*

Mostly yes.

But Unit is a real type.

void is not.

### Part 3 — Single Expression Functions

Traditional:

```kotlin
fun square(
    number: Int
): Int {

    return number * number
}
```

Short form:

```kotlin
fun square(
    number: Int
) = number * number
```

Compiler infers:

`Int`

Used heavily in production.

Example:

```kotlin
fun isAdult(age: Int) =
    age >= 18
```

Compiler:

`Boolean`

**Android Example**

Mapper:

```kotlin
fun UserDto.toUser() =
    User(
        id = id,
        name = name
    )
```

Very common.

**When NOT To Use It**

Bad:

```kotlin
fun calculateSomething() =
    veryLongCode()
        .map {}
        .filter {}
        .groupBy {}
```

Can become unreadable.

Rule:

Use when behavior is simple.

### Part 4 — Default Arguments

Java often needs overloads.

Java:

```java
showMessage("Hello");
showMessage("Hello", true);
showMessage("Hello", true, 5);
```

Many overloads.

Kotlin:

```kotlin
fun showMessage(
    text: String,
    isImportant: Boolean = false,
    retries: Int = 0
) {

}
```

Usage:

```kotlin
showMessage("Hello")
```

Compiler uses:

`false`
`0`

Usage:

```kotlin
showMessage(
    "Hello",
    true
)
```

Usage:

```kotlin
showMessage(
    "Hello",
    true,
    5
)
```

**Android Example**

Navigation.

```kotlin
fun navigateToProfile(
    userId: String,
    clearBackStack: Boolean = false
)
```

Most callers:

```kotlin
navigateToProfile("123")
```

Only special cases:

```kotlin
navigateToProfile(
    "123",
    true
)
```

### Part 5 — Named Arguments

One of Kotlin's best features.

Suppose:

```kotlin
fun createUser(
    name: String,
    age: Int,
    isPremium: Boolean
)
```

Call:

```kotlin
createUser(
    "Akshay",
    22,
    true
)
```

Question:

What is `true`?

Not obvious.

Named arguments:

```kotlin
createUser(
    name = "Akshay",
    age = 22,
    isPremium = true
)
```

Much clearer.

**Android Example**

Compose uses this heavily.

Example:

```kotlin
Text(
    text = "Hello",
    maxLines = 1
)
```

Without named arguments:

```kotlin
Text(
    "Hello",
    1
)
```

Confusing.

### Part 6 — Top-Level Functions

Java mindset:

```java
class Utils {

   static int add(){}
}
```

Kotlin:

```kotlin
fun add(
    a: Int,
    b: Int
): Int {

    return a + b
}
```

No class needed.

This is called:

**Top-Level Function**

**Why Kotlin Added This**

Many utility functions don't belong to objects.

**Android Example**

File: `DateUtils.kt`

```kotlin
fun formatDate(
    timestamp: Long
): String {

}
```

No unnecessary class.

### Part 7 — Local Functions

Function inside function.

Example:

```kotlin
fun registerUser() {

    fun validateName(
        name: String
    ): Boolean {

        return name.isNotBlank()
    }

}
```

Useful when helper logic is only needed inside one function.

**Android Example**

```kotlin
fun submitForm() {

    fun validateEmail(): Boolean {
        return email.contains("@")
    }

    fun validatePassword(): Boolean {
        return password.length > 6
    }

}
```

Cleaner.

### Part 8 — Infix Functions

Allows:

`a plus b`

instead of:

`a.plus(b)`

Create:

```kotlin
infix fun Int.multiplyBy(
    value: Int
): Int {

    return this * value
}
```

Usage:

```kotlin
val result =
    5 multiplyBy 3
```

Output:

`15`

Rare in Android apps.

Common in DSLs.

### Part 9 — Operator Functions

Kotlin lets classes behave like operators.

Example:

```kotlin
operator fun plus(
    other: Money
): Money
```

Usage:

```kotlin
wallet1 + wallet2
```

Internally:

```kotlin
wallet1.plus(wallet2)
```

Compose and libraries use operator overloading heavily.

### Part 10 — Tail Recursion

Advanced.

Normal recursion:

```kotlin
fun countdown(
    n: Int
) {

    if(n == 0)
        return

    countdown(n - 1)
}
```

Large input:

`StackOverflowError`

possible.

Tail recursive:

```kotlin
tailrec fun countdown(
    n: Int
) {

    if(n == 0)
        return

    countdown(n - 1)
}
```

Compiler converts recursion into loop.

Performance improves.

### Part 11 — The Famous invoke() Pattern

This is everywhere in Clean Architecture.

Traditional:

```kotlin
class LoginUseCase {

    fun execute(
        email: String,
        password: String
    ) {

    }
}
```

Usage:

```kotlin
loginUseCase.execute(
    email,
    password
)
```

Idiomatic Kotlin:

```kotlin
class LoginUseCase {

    operator fun invoke(
        email: String,
        password: String
    ) {

    }
}
```

Usage:

```kotlin
loginUseCase(
    email,
    password
)
```

Looks like function.

**Why Senior Teams Like It**

Because UseCases represent actions.

Actions feel natural as functions.

Visual:

`loginUseCase(...)`

instead of:

`loginUseCase.execute(...)`

Cleaner API.

**Android Architecture Example**

Domain Layer:

```kotlin
class GetSongsUseCase(
    private val repository: MusicRepository
) {

    suspend operator fun invoke():
        List<Song> {

        return repository.getSongs()
    }
}
```

ViewModel:

```kotlin
val songs =
    getSongsUseCase()
```

Beautiful.

## Interview Notes

**Function**

Reusable block of behavior.

**Parameter**

Variable declared in function definition.

```kotlin
fun greet(name: String)
```

**Argument**

Actual value passed.

```kotlin
greet("Akshay")
```

**Unit**

Kotlin equivalent of void.

But Unit is a real type.

**Single Expression Function**

```kotlin
fun square(x: Int) = x * x
```

**Default Argument**

Provides fallback value.

**Named Argument**

Improves readability.

```kotlin
createUser(
    age = 22
)
```

**Top-Level Function**

Function declared outside class.

**Local Function**

Function declared inside another function.

**Infix Function**

Allows:

`a plus b`

syntax.

**Operator Function**

Allows operator overloading.

**Tail Recursion**

Compiler optimization converting recursion into loop.

**Why Use operator invoke()?**

Makes objects callable like functions.

Frequently used in Clean Architecture UseCases.

## Exercises

**1. Parameter or argument?**

```kotlin
fun greet(name: String)

greet("Akshay")
```

Identify both.

**2. Convert:**

```kotlin
fun square(
    x: Int
): Int {

    return x * x
}
```

into a single-expression function.

**3. Why does this work?**

```kotlin
loginUseCase()
```

even though `loginUseCase` is an object?

**4. When would you choose a top-level function over a class method?**