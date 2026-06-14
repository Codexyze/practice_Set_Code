Module 1 — Core Kotlin Language
Chapter 1.2 — Expressions, Control Flow, Smart Casts, Ranges, Loops, Labels

This chapter is where Kotlin starts becoming a modern language.

Many Android developers write Kotlin like Java:

```kotlin
if (isLoggedIn) {
    title = "Home"
} else {
    title = "Login"
}
```

But Kotlin was designed around a different philosophy:

Everything should produce a value whenever possible.

This idea influences:

* Compose
* Coroutines
* Flow
* StateFlow
* DSLs
* Modern Android architecture

Part 1 — Statement vs Expression

This is one of the most important concepts in Kotlin.

Statement

A statement does something.

Example:

```kotlin
println("Hello")
```

It performs an action.

But it doesn't produce a useful value.

Another example:

```kotlin
var age = 22
```

Creates variable.

Action performed.

No value returned.

Expression

An expression produces a value.

Example:

```kotlin
10 + 20
```

Produces:

`30`

Example:

```kotlin
val result = 10 + 20
```

Expression result becomes:

`30`

Why Kotlin Loves Expressions

Java:

```java
String message;

if(age >= 18){
    message = "Adult";
}else{
    message = "Minor";
}
```

Kotlin:

```kotlin
val message =
    if (age >= 18) {
        "Adult"
    } else {
        "Minor"
    }
```

Notice:

```kotlin
if (...)
```

returns a value.

Android Example

Bad:

```kotlin
var buttonText = ""

if(isPlaying){
    buttonText = "Pause"
}else{
    buttonText = "Play"
}
```

Idiomatic Kotlin:

```kotlin
val buttonText =
    if(isPlaying) "Pause"
    else "Play"
```

Cleaner.

Safer.

Less mutable state.

Interview Note
Why does Kotlin prefer expressions?

Expressions reduce mutable variables and encourage immutable code.

Part 2 — if as an Expression

Java developers often miss this.

Example:

```kotlin
val max =
    if (a > b) a
    else b
```

Visual:

```text
if
 │
 ├── a
 │
 └── b
```

Returns one value.

Example:

```kotlin
val greeting =
    if(userName.isNotEmpty())
        "Welcome $userName"
    else
        "Guest"
```

Real Android Example

Music Player:

```kotlin
val icon =
    if(isPlaying)
        Icons.Pause
    else
        Icons.PlayArrow
```

Very common in Compose.

Multi-line if Expression
```kotlin
val result =
    if(score > 90){
        println("Excellent")
        "A"
    } else {
        "B"
    }
```

Result:

`A`

Important:

Last expression becomes return value.

Part 3 — when Expression

This is Kotlin's super-powered switch.

Java:

```java
switch(status){
   case 1:
   ...
}
```

Kotlin:

```kotlin
when(status){
    1 -> "Loading"
    2 -> "Success"
    3 -> "Error"
    else -> "Unknown"
}
```

Why when Is Better

Can use:

* values
* types
* ranges
* multiple conditions

Example 1
```kotlin
val result =
    when(day){
        1 -> "Monday"
        2 -> "Tuesday"
        else -> "Unknown"
    }
```

Example 2

Multiple conditions

```kotlin
when(score){
    100 -> println("Perfect")
    90,91,92 -> println("Excellent")
}
```

Example 3

Ranges

```kotlin
when(score){
    in 90..100 -> "A"
    in 80..89 -> "B"
    else -> "Fail"
}
```

Android Example

Network State

```kotlin
val message =
    when(state){
        Loading -> "Loading..."
        Success -> "Success"
        Error -> "Error"
    }
```

Compose Example
```kotlin
when(uiState){
    is LoadingUiState -> LoadingScreen()

    is SuccessUiState -> SuccessScreen()

    is ErrorUiState -> ErrorScreen()
}
```

This pattern appears everywhere.

Part 4 — Smart Casts

One of Kotlin's best features.

Java:

```java
if(user != null){
   user.getName();
}
```

Kotlin:

```kotlin
if(user != null){
    println(user.name)
}
```

Notice:

No cast.

No extra checks.

Compiler thinks:

Inside this block

user cannot be null

Automatically.

Example
```kotlin
val text: String? = "Hello"

if(text != null){
    println(text.length)
}
```

Works.

Compiler smart-casts:

`String?`

to

`String`

inside block.

Android Example
```kotlin
val song = selectedSong

if(song != null){
    player.play(song)
}
```

No manual casting.

Part 5 — Smart Casts with Types

Suppose:

```kotlin
sealed interface UiState

Implementations:

data class Success(
    val data: String
): UiState

object Loading: UiState
```

Now:

```kotlin
when(state){

    is Success -> {
        println(state.data)
    }

    Loading -> {}
}
```

Compiler knows:

`state` is `Success`

inside block.

No casting required.

This is heavily used in Compose.

Part 6 — Ranges

Range creates a sequence of values.

Example:

```kotlin
1..5
```

Represents:

`1 2 3 4 5`

Store:

```kotlin
val range = 1..5
```

Check:

```kotlin
println(3 in range)
```

Output:

`true`

Android Example

OTP Validation

```kotlin
if(code.length in 4..6){
    submit()
}
```

Readable.

until

Exclusive upper bound.

Example:

```kotlin
1 until 5
```

Produces:

`1 2 3 4`

Not 5.

Why Android Developers Use until

Arrays start at zero.

Bad:

```kotlin
for(i in 0..list.size)
```

Can crash.

Good:

```kotlin
for(i in 0 until list.size)
```

Part 7 — Loops
for Loop
```kotlin
for(i in 1..5){
    println(i)
}
```

Output:

```text
1
2
3
4
5
```

Step
```kotlin
for(i in 1..10 step 2){
    println(i)
}
```

Output:

```text
1
3
5
7
9
```

DownTo
```kotlin
for(i in 10 downTo 1){
    println(i)
}
```

Android Example

Music Queue

```kotlin
for(song in playlist){
    println(song.title)
}
```

Iterating with Index
```kotlin
playlist.forEachIndexed { index, song ->
    println("$index ${song.title}")
}
```

Used often in Compose lists.

Part 8 — while Loop

Use when count unknown.

Example:

```kotlin
var count = 0

while(count < 5){
    println(count)
    count++
}
```

Android Example

Polling:

```kotlin
while(player.isPlaying){
    updateSeekBar()
}
```

Although coroutines are usually preferred.

Part 9 — Labels

Advanced Kotlin topic.

Interviewers occasionally ask.

Without label:

```kotlin
for(i in 1..3){
    for(j in 1..3){
        break
    }
}
```

Break exits only inner loop.

With label:

```kotlin
outer@
for(i in 1..3){

    for(j in 1..3){

        break@outer
    }
}
```

Exits outer loop.

Visual:

```text
outer loop
   |
inner loop
   |
break@outer
```

Leaves everything.

Compose Connection

Labels appear more often with lambdas.

Example:

```kotlin
list.forEach {

    if(it == 3)
        return@forEach

    println(it)
}
```

This skips only current iteration.

Without label:

Could accidentally return from surrounding function.

Part 10 — Nothing Type

One of Kotlin's most advanced foundational concepts.

Function:

```kotlin
fun fail(): Nothing {
    throw Exception()
}
```

Meaning:

This function never returns

Compiler understands:

Execution stops here

Android Example

```kotlin
val user =
    repository.getUser()
        ?: error("User missing")
```

error() returns:

`Nothing`

Compiler knows:

Either user exists

or app stops

After this point:

`user`

is guaranteed non-null.

Interview Notes
Statement vs Expression

Statement:

performs action

Expression:

produces value

Why Is if Special in Kotlin?

Because it returns a value.

It is an expression.

Why Is when Better Than switch?

Supports:

* values
* types
* ranges
* expressions

What Is Smart Cast?

Compiler automatically converts a nullable or parent type into a safer type after checks.

Difference Between .. and until
```kotlin
1..5
```

includes 5

```kotlin
1 until 5
```

excludes 5

What Is Nothing?

A type representing a function that never returns.

Usually:

* throws exception
* terminates execution

Senior Android Takeaway

Modern Kotlin code tries to move from:

```kotlin
var result = ""

if(condition){
    result = "A"
}else{
    result = "B"
}
```

toward:

```kotlin
val result =
    if(condition) "A"
    else "B"
```

And from:

```java
switch(...)
```

toward:

```kotlin
when(...)
```

And from:

null checks everywhere

toward:

smart casts

This shift—from mutation to expressions—is one of the biggest philosophical differences between Java-style code and idiomatic Kotlin.