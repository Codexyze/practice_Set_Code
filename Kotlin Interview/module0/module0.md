# Module 0 — Programming Model Foundations

## Chapter 0.1 — What Is a Program Really?

Before Kotlin.  
Before Android.  
Before Compose.  
Before Coroutines.

Let's understand what we're actually building.

Most developers think:  
Program = Code

Not exactly.

A program is:  
**Data + Rules + State Changes**

### Example: Instagram

**Data:**
* Users
* Posts
* Comments
* Likes

**Rules:**
* User can like a post
* User can follow another user

**State Changes:**
* Like count increases
* New comment added

Everything in software follows this model.

### Why This Matters For Kotlin

Almost every Kotlin feature exists to solve one problem:  
**Managing state safely**

Examples:
* `val` reduces accidental state changes.
* `data class` helps model state.
* `StateFlow` helps observe state.
* `Coroutine` helps update state safely.

### Mental Model

Imagine a music player.

State:
```kotlin
Song = Believer.mp3
isPlaying = false
position = 0
```

User clicks play.  
State becomes:
```kotlin
Song = Believer.mp3
isPlaying = true
position = 0
```

User drags seekbar.  
State becomes:
```kotlin
Song = Believer.mp3
isPlaying = true
position = 95s
```

The app is just changing state. Nothing more.

### Everything Is State

In Android:
`TextField` State:
```kotlin
var text = ""
```

Counter app:
```kotlin
var count = 0
```

Chat app:
```kotlin
val messages = listOf(...)
```

Music Player:
* `isPlaying`
* `currentSong`
* `currentPosition`

ML App:
* `prediction`
* `confidence`
* `isLoading`

**First Principle**  
A software application is a machine that transforms state.

## Chapter 0.2 — Values vs Variables

This is where most bugs begin.

Imagine:
```kotlin
var age = 22
```

**Question:** What changes?  
**Answer:** The value stored by the variable.

```kotlin
age = 23
```

Now: `22 -> 23`  
State changed.

### Why Bugs Happen

Suppose:
```kotlin
var balance = 1000
```

Function A:
```kotlin
balance -= 100
```

Function B:
```kotlin
balance += 500
```

Function C:
```kotlin
balance -= 50
```

Now: Who changed balance? When? Why?  
Difficult to track.

This is called: **Mutable State**  
State that can change. Mutable state causes most software bugs.

## Chapter 0.3 — Immutability

One of Kotlin's core philosophies.

Instead of:
```kotlin
var age = 22
age = 23
```

Create a new value:
```kotlin
val age = 22
```

Need another value?
```kotlin
val updatedAge = 23
```

Now: `22` never changed

**Benefits:**
* Predictable
* Thread Safe
* Easy to Debug
* Easy to Test

### Android Example

Bad:
```kotlin
var userName = "Akshay"
```
Many places can modify it.

Better:
```kotlin
data class UserUiState(
    val userName: String
)
```

When state changes:
```kotlin
state = state.copy(userName = "Akshay")
```

Old state remains untouched.

This is exactly why Compose, StateFlow, Redux, MVI love immutable state.

## Chapter 0.4 — References

This is where many developers get confused.

Code:
```kotlin
val numbers = mutableListOf(1, 2, 3)
```

People think: `val` means immutable.  
Wrong.

Let's visualize.
`numbers | V [1,2,3]`

Reference: `numbers`  
Object: `[1,2,3]`

This works:
```kotlin
numbers.add(4)
```

Result: `[1,2,3,4]`

Because: Object changed. Reference did not.

This fails:
```kotlin
numbers = mutableListOf()
```

Because: Reference changed.

### Interview Question

Why does this compile?
```kotlin
val list = mutableListOf(1)
list.add(2)
```

**Answer:** `val` protects reference not object.

## Chapter 0.5 — Value vs Reference Thinking

This concept will become extremely important later.

Imagine:
```kotlin
data class User(
    val name: String
)
```

Create:
```kotlin
val user1 = User("Akshay")
```

Copy:
```kotlin
val user2 = user1
```

Now:
```
user1 -> User("Akshay") <- user2
```
Both point to same object.

Interviewers love this concept.

## Chapter 0.6 — Stack vs Heap

You don't need JVM engineer level knowledge. But you need enough.

When:
```kotlin
val age = 22
```
Small values are stored efficiently.

When:
```kotlin
val user = User("Akshay")
```
Object lives in heap memory. Reference points to it.

Think: `Reference -> Address -> Object`

Example:
```kotlin
val user = User("Akshay")
```

Visual:
```
Stack       Heap
user -----> User("Akshay")
```

### Why Android Developers Care

Large objects: Bitmap, List, Flow, StateFlow, ExoPlayer live on heap.

Heap misuse:
* Memory leaks
* OOM
* Slow GC

## Chapter 0.7 — Side Effects

One of the most important concepts.

Function:
```kotlin
fun add(a: Int, b: Int): Int {
    return a + b
}
```

Output depends only on input. Always.

This is called: **Pure Function**

Input: `2,3`  
Output: `5`  
Always.

Now:
```kotlin
var total = 0

fun addToTotal(value: Int) {
    total += value
}
```

This changes external state.

Called: **Side Effect**  
Because function affects outside world.

Other side effects:
* `println()` writes console.
* `saveToDatabase()` writes database.
* `networkCall()` uses network.
* `playSong()` uses speaker.

### Android Example

Pure:
```kotlin
fun calculateDiscount(price: Double): Double {
    return price * 0.9
}
```

Impure:
```kotlin
fun saveUser() {
    database.insert(...)
}
```

### Why Functional Programming Loves Purity

Pure functions are:
* Predictable
* Easy to Test
* Easy to Reuse
* Thread Safe

### Compose Connection

Compose loves pure functions.

Composable:
```kotlin
@Composable
fun UserCard(name: String)
```

Should ideally behave like: `Input -> UI`  
Same input. Same output.

## Chapter 0.8 — Why Kotlin Exists

Java was powerful. But had problems:

* **NullPointerException**
  ```java
  user.getName() // Crash.
  ```
* **Verbose Code**
  ```java
  new User(...)
  ```
* **Mutable by Default**
* **Weak Functional Support**

Kotlin solves these with:
* `val` (Immutability preference)
* `String?` (Null safety)
* `data class` (Data modeling)
* `map()`, `filter()` (Functional style)
* `coroutines` (Concurrency)

### Android Example

Java:
```java
if (user != null) {
    if (user.getName() != null) {
        // ...
    }
}
```

Kotlin:
```kotlin
user?.name
```
Cleaner. Safer.

## Interview Notes (Save These)

**What is a Program?**  
A program is a system that stores data and transforms state according to rules.

**What is State?**  
Current condition of application data.  
Examples: Login status, Song position, Messages, Theme.

**What is Mutable State?**  
State that can change.
```kotlin
var count = 0
```

**What is Immutable State?**  
State that never changes after creation.
```kotlin
val count = 0
```

**What is a Reference?**  
A variable that points to an object in memory.

**Difference Between Object and Reference?**  
Reference:
```kotlin
val user
```
Object:
```kotlin
User(...)
```

**What is a Side Effect?**  
Any operation affecting external state.  
Examples: Database write, Network call, Logging, File write.

**What is a Pure Function?**  
A function that produces same output for same input and has no side effects.

**Why Compose Prefers Immutable State?**  
Because immutable state allows:
* Easier recomposition
* Predictable UI
* Easier debugging
* Safer concurrency

## Exercises

### Exercise 1
Is this mutable or immutable?
```kotlin
val userName = "Akshay"
```
Explain why.

### Exercise 2
Why does this compile?
```kotlin
val list = mutableListOf(1)
list.add(2)
```

### Exercise 3
Pure or impure?
```kotlin
fun square(x: Int) = x * x
```

### Exercise 4
Pure or impure?
```kotlin
fun logMessage() {
    println("Hello")
}
```