# Module 2 — Object-Oriented Kotlin

## Chapter 2.4 — Inheritance, Open Classes, Abstract Classes, Interfaces, Polymorphism, Upcasting, Downcasting & Sealed Classes

### Why This Chapter Matters

This chapter is the foundation of OOP.

Almost every Android interview asks something from here:

* What is inheritance?
* Difference between abstract class and interface?
* What is polymorphism?
* Why does Google recommend composition over inheritance?
* Why are sealed classes used for UiState?

If you understand this chapter deeply:

* ViewModel
* Repository
* Navigator
* UseCase
* UiState
* Network Results
* Compose Screens

become much easier.

### Part 1 — Why Inheritance Exists

Imagine Spotify.

You have:

* Song
* Podcast
* Audiobook

All have:

* `title`
* `duration`
* `play()`
* `pause()`

Repeating code everywhere is bad.

Instead:

```text
Media
 ↑
 |
 ├── Song
 ├── Podcast
 └── Audiobook
```

Common behavior moves upward.

**First Inheritance Example**

**Working Code**

```kotlin
open class Media(

    val title: String

) {

    fun play() {

        println("Playing $title")

    }

}

class Song(

    title: String

) : Media(title)

fun main() {

    val song = Song("Believer")

    song.play()

}
```

Output:

`Playing Believer`

**Line-by-Line Explanation**

`open class Media(`

Normally Kotlin classes cannot be inherited.

Why?

Kotlin prefers composition.

To allow inheritance:

`open`

must be added.

`val title: String`

Property of `Media`.

Every child receives it.

`class Song(`

Child class.

`: Media(title)`

Calls parent constructor.

Meaning:

Before creating `Song`

Create `Media` part first.

`song.play()`

`play()` belongs to `Media`.

`Song` inherited it.

**Memory Diagram**

```kotlin
val song = Song("Believer")
```

Memory:

```text
Song Object

title = Believer

play()
```

The `Song` object contains everything inherited from `Media`.

**Why Classes Are Final By Default**

Java:

```java
class User {}
```

Can be inherited.

Kotlin:

```kotlin
class User
```

Cannot be inherited.

Need:

```kotlin
open class User
```

Why?

Because inheritance can create fragile code.

Kotlin forces you to explicitly allow it.

### Part 2 — Overriding Functions

Suppose parent has:

```kotlin
open class Media {

    open fun play() {

        println("Playing Media")

    }

}
```

Notice:

`open fun`

Functions are also final by default.

Child:

```kotlin
class Song : Media() {

    override fun play() {

        println("Playing Song")

    }

}
```

**Working Example**

```kotlin
open class Media {

    open fun play() {

        println("Playing Media")

    }

}

class Song : Media() {

    override fun play() {

        println("Playing Song")

    }

}

fun main() {

    val song = Song()

    song.play()

}
```

Output:

`Playing Song`

**Line-by-Line**

`open fun play()`

Allows subclasses to replace implementation.

`override fun play()`

Replace parent behavior.

Compiler verifies:

A matching parent method exists.

**Common Mistake**

Wrong:

```kotlin
class Song : Media() {

    fun play() {

    }

}
```

Compilation error.

Must use:

`override`

### Part 3 — super Keyword

Access parent implementation.

**Example**

```kotlin
open class Media {

    open fun play() {

        println("Media Play")

    }

}

class Song : Media() {

    override fun play() {

        super.play()

        println("Song Play")

    }

}
```

**Output**

```text
Media Play
Song Play
```

**Explanation**

`super.play()`

Call parent implementation first.

Used frequently in Android.

Example:

`super.onCreate()`

inside `Activity`.

### Part 4 — Abstract Classes

Sometimes parent shouldn't be creatable.

Example:

`Media`

is a concept.

Not actual object.

Does this make sense?

```kotlin
val media = Media()
```

Not really.

Solution:

`abstract class Media`

**Working Example**

```kotlin
abstract class Media {

    abstract fun play()

}
```

Cannot do:

```kotlin
val media = Media()
```

Compilation error.

Must create child:

```kotlin
class Song : Media() {

    override fun play() {

        println("Song Playing")

    }

}
```

**Theory**

Abstract class:

* Partial implementation

Can contain:

* Properties
* Functions
* Implemented methods
* Abstract methods

**Example**

```kotlin
abstract class Media {

    fun pause() {

        println("Paused")

    }

    abstract fun play()

}
```

Child gets:

`pause()`

for free.

Must implement:

`play()`

### Part 5 — Interfaces

One of the most important topics.

Modern Android heavily prefers interfaces.

Think:

* Can Play
* Can Download
* Can Share

These are capabilities.

Not object types.

**Example**

```kotlin
interface Playable {

    fun play()

}
```

**Working Example**

```kotlin
interface Playable {

    fun play()

}

class Song : Playable {

    override fun play() {

        println("Playing Song")

    }

}
```

Output:

`Playing Song`

**Why Interfaces?**

A class can inherit:

* One Class

but implement:

* Many Interfaces

**Example**

```kotlin
interface Playable
interface Downloadable
interface Shareable

class Song :
    Playable,
    Downloadable,
    Shareable
```

Perfectly valid.

**Abstract Class vs Interface**

*Abstract Class*

Represents:

**IS-A**

relationship.

Example:

`Song IS-A Media`

*Interface*

Represents:

**CAN-DO**

relationship.

Example:

`Song CAN Play`

**Android Example**

```kotlin
interface UserRepository {

    suspend fun getUser(): User

}
```

Implementation:

```kotlin
class UserRepositoryImpl :
    UserRepository
```

ViewModel depends on:

`UserRepository`

not implementation.

This is Dependency Inversion.

### Part 6 — Polymorphism

Interview Favorite.

Definition:

One interface

Many implementations

**Example**

```kotlin
interface Media {

    fun play()

}
```

Implementations:

```kotlin
class Song : Media
class Podcast : Media
class Audiobook : Media
```

**Usage**

```kotlin
fun startPlayback(
    media: Media
) {

    media.play()

}
```

Now:

```kotlin
startPlayback(Song())
```

or

```kotlin
startPlayback(Podcast())
```

Both work.

Why?

Polymorphism.

**Visual**

```text
Media
  ↑
  |
  ├── Song
  ├── Podcast
  └── Audiobook
```

Same API.

Different behavior.

### Part 7 — Upcasting

Very common.

**Example**

```kotlin
val media: Media =
    Song()
```

What happened?

```text
Song
↓
Media
```

Child becomes parent reference.

This is:

Upcasting

Safe.

Always allowed.

**Why Useful?**

Allows polymorphism.

**Example**

```kotlin
val items: List<Media> =
    listOf(
        Song(),
        Podcast()
    )
```

Single list.

Multiple types.

### Part 8 — Downcasting

Opposite direction.

**Example**

```kotlin
val media: Media =
    Song()
```

Now:

```kotlin
val song =
    media as Song
```

Compiler:

```text
Media
↓
Song
```

This is:

Downcasting

Dangerous.

**Example**

```kotlin
val media: Media =
    Podcast()

val song =
    media as Song
```

Runtime crash.

**Safe Cast**

Use:

`as?`

**Example**

```kotlin
val song =
    media as? Song
```

Returns:

`null`

instead of crashing.

### Part 9 — Sealed Classes

This is probably the most important Android topic in this chapter.

Problem:

Network can be:

* Loading
* Success
* Error

Traditional inheritance:

```kotlin
open class Result
```

Anyone can create subclasses.

Dangerous.

Solution:

`sealed class Result`

**Working Example**

```kotlin
sealed class Result {

    data object Loading : Result()

    data class Success(
        val data: String
    ) : Result()

    data class Error(
        val message: String
    ) : Result()

}
```

**Usage**

```kotlin
fun handleResult(
    result: Result
) {

    when(result) {

        Result.Loading ->
            println("Loading")

        is Result.Success ->
            println(result.data)

        is Result.Error ->
            println(result.message)
    }

}
```

**Why Sealed Is Amazing**

Compiler knows:

* Loading
* Success
* Error

are all possibilities.

No need:

`else`

If new state added:

```kotlin
data object Empty
```

Compiler forces update.

**Compose Example**

```kotlin
sealed interface UiState {

    data object Loading : UiState

    data class Success(
        val songs: List<String>
    ) : UiState

    data class Error(
        val message: String
    ) : UiState
}
```

UI:

```kotlin
when(uiState) {

    UiState.Loading -> {}

    is UiState.Success -> {}

    is UiState.Error -> {}
}
```

Compile-time safety.

**Why Modern Android Loves Sealed Classes**

Used for:

* UiState
* Navigation Events
* Network Results
* Error Handling
* One-Time Events

Everywhere.

## Interview Notes

**Why Are Classes Final By Default?**

To prevent accidental inheritance and encourage composition.

**Difference Between open and override?**

`open`

Allows overriding.

`override`

Replaces implementation.

**Abstract Class vs Interface?**

Abstract Class:

* `IS-A` relationship.
* Can have state.

Interface:

* `CAN-DO` relationship.
* Represents capability.

**What Is Polymorphism?**

One type.

Many implementations.

**What Is Upcasting?**

`Song -> Media`

Safe.

**What Is Downcasting?**

`Media -> Song`

Potentially unsafe.

**Why Use Sealed Classes?**

Restricted hierarchy.

Compiler knows all subclasses.

Perfect for state modeling.

**Senior Android Takeaway**

Modern Android architecture generally prefers:

Interfaces + Composition + Sealed Classes

instead of:

Deep Inheritance Hierarchies

A modern ViewModel is more likely to depend on:

* `UserRepository`
* `Navigator`
* `AnalyticsTracker`

interfaces

than inherit from 5 levels of base classes.

And almost every Compose project today uses:

`sealed interface UiState`

for screen state modeling.

## Exercises

**1. Predict Output**

```kotlin
open class Animal {

    open fun sound() {
        println("Animal")
    }
}

class Dog : Animal() {

    override fun sound() {
        println("Dog")
    }
}

fun main() {

    val animal: Animal = Dog()

    animal.sound()
}
```

**2. Is this Upcasting or Downcasting?**

```kotlin
val media: Media = Song()
```

**3. Why is sealed class UiState usually preferred over open class UiState in Compose?**

**4. Abstract Class or Interface?**

A `UserRepository` contract that different implementations can provide.

Explain your choice.
