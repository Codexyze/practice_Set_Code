# Module 1 — Core Kotlin Language

## Chapter 1.1 — var, val, Type System, Equality, and Memory

This chapter is extremely important.

Many Android developers know:

```kotlin
var count = 0
val name = "Akshay"
```

But cannot explain:
* Why `val` exists
* What happens in memory
* What type inference actually does
* Difference between `==` and `===`
* Boxing and unboxing
* Why immutable state became the foundation of Compose

Let's fix that.

### Part 1 — Kotlin Type System

Before Kotlin code.

Let's understand a huge idea.

In some languages:
```javascript
let x = 10
x = "Hello"
x = true
```
Same variable. Different types.

This is called **Dynamic Typing**. Type checked at runtime.

Kotlin does NOT do this. Kotlin is **Statically Typed**.

Meaning: Compiler knows types before execution.

Example:
```kotlin
val age = 22
```

Compiler immediately knows:
```kotlin
val age: Int = 22
```

Trying:
```kotlin
var age = 22
age = "Akshay" // Compilation error
```

**Why Does Kotlin Prefer Static Typing?**

Imagine:
```kotlin
fun calculateTax(price: Double): Double
```
Compiler knows:
* Input = `Double`
* Output = `Double`

Can catch mistakes before app runs.

**Android Example:**
```kotlin
fun loadUser(userId: String)
```
Compiler prevents:
```kotlin
loadUser(123)
```
This safety saves thousands of bugs.

### Part 2 — Type Inference

Kotlin hates unnecessary typing.

Java:
```java
String name = "Akshay";
```

Kotlin:
```kotlin
val name = "Akshay"
```
Compiler infers:
```kotlin
val name: String = "Akshay"
```

Another example:
```kotlin
val isPremium = true
```
Compiler:
```kotlin
val isPremium: Boolean = true
```

**Important Interview Question**
*Is Kotlin dynamically typed because it infers types?*

No. Type inference is compile-time. Kotlin remains statically typed.

**When Should We Explicitly Write Types?**

Junior:
```kotlin
val user = repository.fetchUser()
```
Good.

Public APIs:
```kotlin
fun getUser(): User
```
Better than:
```kotlin
fun getUser() = User(...)
```
because intent becomes clearer.

**Android Architecture Example**

Repository:
```kotlin
interface UserRepository {
    suspend fun getUser(id: String): User
}
```
Explicit types improve readability.

### Part 3 — var vs val (Real Understanding)

Most tutorials:
* `var` = mutable
* `val` = immutable

This explanation is incomplete.

Consider:
```kotlin
var name = "Akshay"
```
Memory:
```text
name ----> "Akshay"
```

Later:
```kotlin
name = "John"
```
Now:
```text
name ----> "John"
```
Reference changed.

With `val`:
```kotlin
val name = "Akshay"
```

Trying:
```kotlin
name = "John" // Fails. Reference cannot change.
```

**Important Distinction**

Interviewers love this.
`val` protects the **Reference**, NOT the **Object**.

Example:
```kotlin
val songs = mutableListOf(
    "Believer",
    "Enemy"
)
```
This works:
```kotlin
songs.add("Bones")
```

Result:
```text
Believer
Enemy
Bones
```
Why? Because the **reference is unchanged**, but the **object changed**.

Visual:
```text
songs
   |
   V
MutableList
```
Reference same. List contents changed.

**Android Example**

Compose:
```kotlin
val state = mutableStateOf(0)
```
This works:
```kotlin
state.value = 5
```
because the `state` reference is the same, but the internal value changed.

**Senior Engineering Rule**

Default to: `val`
Always.
Only use `var` when absolutely necessary.

Why? Because every mutable variable becomes a possible bug source.

### Part 4 — Why Immutability Matters

Suppose:
```kotlin
var currentUser = User(...)
```
100 different classes can modify it. Eventually:
* Who changed it?
* When?
* Why?

No idea.

Instead:
```kotlin
val currentUser = User(...)
```
State becomes predictable.

**Compose Architecture Example**

Bad:
```kotlin
var username = "Akshay"
```

Good:
```kotlin
data class UserUiState(
    val username: String
)
```

Update:
```kotlin
state = state.copy(
    username = "John"
)
```
Old state remains. New state created.

This is one reason why Compose, Redux, MVI, and StateFlow love immutable models.

### Part 5 — Numbers in Kotlin

Kotlin provides: `Byte`, `Short`, `Int`, `Long`, `Float`, `Double`
Most used: `Int`, `Long`, `Double`

Example:
```kotlin
val age = 22 // Type: Int
```

Large value:
```kotlin
val views = 5000000000L
```
Notice: `L`
Without it: Compilation error.

**Android Example**

Bad:
```kotlin
val songDuration = 3000
```
when duration may exceed `Int` limits in some systems.

Prefer:
```kotlin
val songDurationMs = 3000L
```
Most Android time values use `Long`.
Examples:
```kotlin
System.currentTimeMillis() // returns Long
```

### Part 6 — Boxing and Unboxing

Interview favorite.

Java has primitive and wrapper:
```java
int age = 22;
Integer age = 22;
```

Kotlin hides this. You write:
```kotlin
val age = 22
```
Compiler decides. Usually `int` on JVM. Fast.

Nullable:
```kotlin
val age: Int? = 22
```
Now primitive impossible. Must become `Integer` because `null` requires an object.

**Why Is Int? More Expensive?**

Because JVM needs object allocation instead of primitive.

**Interview Question**
*Why can Int be faster than Int?*

Answer:
`Int` often compiles to JVM primitive `int`.
`Int?` compiles to boxed `Integer`. Extra memory and overhead.

### Part 7 — Equality

This is one of the most misunderstood Kotlin topics.

Kotlin provides `==` and `===`.
Most developers know syntax. Few know what it means.

**Structural Equality (`==`)**

asks: *Do contents match?*

Example:
```kotlin
val a = "Akshay"
val b = "Akshay"

println(a == b)
```
Output: `true`
Contents same. Behind scenes: `a?.equals(b)`

**Referential Equality (`===`)**

asks: *Same object?*

Example:
```kotlin
val a = User("Akshay")
val b = User("Akshay")

println(a === b)
```
Output: `false`
Two different objects.

Visual:
```text
a ----> User
b ----> User
```
Different addresses.

Example:
```kotlin
val user1 = User("Akshay")
val user2 = user1

println(user1 === user2)
```
Output: `true`
Same object.

**Android Example**

Suppose:
```kotlin
val oldState = state
```

Later:
```kotlin
val newState = oldState.copy()
```

Now:
```kotlin
oldState == newState // Usually true (Same contents)
oldState === newState // false (Different objects)
```

**Why Compose Developers Care**

Compose frequently relies on object identity and state changes.
Understanding `==` and `===` helps understand recomposition behavior.

### Part 8 — Strings Internals

`String` is actually an `Object`.

Example:
```kotlin
val name = "Akshay"
```
Strings are **Immutable**.

This:
```kotlin
val name = "Akshay"
name.uppercase()
```
does NOT modify original. Returns `"AKSHAY"` (New String).

Original `"Akshay"` still exists.

**Why Immutable?**

Benefits:
* Thread Safety
* Caching
* Predictability
* Security

**Android Example**

Bad:
```kotlin
var title = "Music" // modified everywhere
```

Better:
```kotlin
val title = "Music" // New values created when needed
```

---

## Interview Notes

**Difference Between var and val**
* `var`: mutable reference
* `val`: read-only reference

**Is val immutable?**
No. Reference immutable. Object may remain mutable.

**Difference Between == and ===**
* `==`: Structural equality. Compares contents.
* `===`: Referential equality. Compares memory reference.

**What Is Type Inference?**
Compiler automatically determines types at compile time.

**Is Kotlin Dynamically Typed?**
No. Kotlin is statically typed.

**Why Prefer val?**
* safer
* easier debugging
* fewer bugs
* better concurrency

**Why Is Int? More Expensive Than Int?**
Nullable types require boxing into JVM wrapper objects.

---

## Exercises

**1. Predict output:**
```kotlin
val a = "Hello"
val b = "Hello"

println(a == b)
println(a === b)
```
*Why?*

**2. Why does this compile?**
```kotlin
val users = mutableListOf("A")
users.add("B")
```

**3. What is the difference?**
```kotlin
val age = 10
```
and
```kotlin
val age: Int? = 10
```

**4. Why does Compose generally prefer immutable UI state?**
*Try answering before reading ahead.*
