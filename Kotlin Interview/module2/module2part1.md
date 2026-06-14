# Module 2 — Classes, Objects, Properties & Constructors

## Chapter 2.1 — Classes, Objects, Properties, Constructors, init Blocks

This chapter is where Kotlin starts feeling like a real application language.

Everything you build in Android is made from classes:

* Activity
* ViewModel
* Repository
* UseCase
* Room Entity
* Retrofit DTO
* UiState
* Navigation Models

If Module 1 taught us how to write functions, Module 2 teaches us how to model real-world things.

### What Is A Class?

Imagine you're building Spotify.

You have:

* Song
* Playlist
* User
* Album
* Artist

These are not actions.

These are things.

A class describes a thing.

Example:

```kotlin
class Song
```

Think:

*Blueprint*

Not actual object.

### What Is An Object?

Class:

```kotlin
class Song
```

Object:

```kotlin
val song = Song()
```

Now actual memory is allocated.

**Real World Example**

Blueprint:

*House Plan*

House:

*Actual House*

Class:

```kotlin
class User
```

Object:

```kotlin
val user = User()
```

### First Working Example

```kotlin
class User

fun main() {

    val user1 = User()

    val user2 = User()

    println(user1)

    println(user2)
}
```

Output:

`User@3f99bd52`

`User@7adf9f5f`

(Memory references differ)

Why?

Because:

`user1` → Object A

`user2` → Object B

Different objects.

### Adding Properties

A class without data is not useful.

Let's add data.

```kotlin
class User {

    val name = "Akshay"

}
```

Usage:

```kotlin
fun main() {

    val user = User()

    println(user.name)
}
```

Output:

`Akshay`

### What Is A Property?

Property = Data belonging to an object.

Example:

```kotlin
class User {

    val name = "Akshay"

    val age = 22

}
```

Properties:

* `name`
* `age`

**Android Example**

```kotlin
class Song {

    val title = "Believer"

    val duration = 180

}
```

### Memory Visualization

```kotlin
val user = User()
```

Memory:

```text
user
  |
  v

User Object
------------
name = Akshay
age = 22
------------
```

### Constructor Introduction

Hardcoded values are useless.

Bad:

```kotlin
class User {

    val name = "Akshay"

}
```

Every user becomes Akshay.

Need dynamic values.

### Primary Constructor

```kotlin
class User(
    val name: String
)
```

Usage:

```kotlin
fun main() {

    val user =
        User("Akshay")

    println(user.name)
}
```

Output:

`Akshay`

Another Object:

```kotlin
val user =
    User("Rahul")
```

Output:

`Rahul`

**Mental Model**

`User("Akshay")`

means:

*Create User*

*Store name = Akshay*

### Multiple Constructor Properties

```kotlin
class User(

    val name: String,

    val age: Int

)
```

Usage:

```kotlin
fun main() {

    val user =
        User(
            "Akshay",
            22
        )

    println(user.name)

    println(user.age)
}
```

Output:

```text
Akshay
22
```

### Why val In Constructor?

Notice:

```kotlin
class User(
    val name: String
)
```

Not:

```kotlin
class User(
    name: String
)
```

Without `val`:

```kotlin
class User(
    name: String
)
```

`name` is only constructor parameter.

Not property.

**Example**

```kotlin
class User(
    name: String
)

fun main() {

    val user =
        User("Akshay")

    println(user.name)
}
```

Compilation error.

Because:

Parameter ≠ Property

### Constructor Parameter vs Property

Example:

```kotlin
class User(
    name: String
)
```

`name` exists only during construction.

Example:

```kotlin
class User(
    val name: String
)
```

Compiler creates:

```kotlin
class User(
    name: String
){

    val name = name

}
```

Conceptually.

**Interview Question**

*Difference Between*

```kotlin
class User(
    name: String
)
```

*and*

```kotlin
class User(
    val name: String
)
```

Answer:

First is constructor parameter.

Second becomes property of object.

### init Block

One of Kotlin's most important features.

Imagine:

```kotlin
class User(
    val name: String
)
```

You want code to run during creation.

Use:

`init`

**Example**

```kotlin
class User(
    val name: String
) {

    init {

        println("User Created")

    }
}
```

Usage:

```kotlin
fun main() {

    User("Akshay")

}
```

Output:

`User Created`

### Why init Exists

Perform setup.

Validation.

Logging.

Calculations.

**Example**

```kotlin
class User(
    val age: Int
){

    init {

        require(age >= 0)

    }
}
```

Usage:

```kotlin
User(-5)
```

Crash:

`IllegalArgumentException`

Good.

Invalid object prevented.

### Constructor Execution Order

**Example**

```kotlin
class User(
    val name: String
){

    init {

        println(name)

    }
}
```

Process:

1. Constructor receives value
2. Property assigned
3. `init` runs

### Multiple init Blocks

Allowed.

```kotlin
class User(
    val name: String
){

    init {
        println("Block 1")
    }

    init {
        println("Block 2")
    }
}
```

Output:

```text
Block 1
Block 2
```

Executed top to bottom.

### Secondary Constructor

Less common but important.

Primary:

```kotlin
class User(
    val name: String
)
```

Secondary:

```kotlin
class User(
    val name: String
){

    constructor():
        this("Unknown")

}
```

Usage:

```kotlin
fun main() {

    val user =
        User()

    println(user.name)
}
```

Output:

`Unknown`

### Why Kotlin Prefers Primary Constructors

Cleaner.

Less boilerplate.

Most Android code uses primary constructors.

### Named Arguments

Huge Kotlin feature.

**Example**

```kotlin
class User(

    val name: String,

    val age: Int

)
```

Usage:

```kotlin
val user =
    User(
        name = "Akshay",
        age = 22
    )
```

More readable.

### Default Constructor Values

Very common in Android.

**Example**

```kotlin
class User(

    val name: String,

    val isPremium: Boolean = false

)
```

Usage:

```kotlin
val user =
    User("Akshay")
```

Compiler uses:

`false`

automatically.

**Android Example**

`UiState`

```kotlin
class PlayerUiState(

    val isPlaying: Boolean = false,

    val songName: String = ""

)
```

Usage:

```kotlin
val state =
    PlayerUiState()
```

No need to provide values.

### Common Mistakes

**Mistake 1**

Using `var` everywhere.

Bad:

```kotlin
class User(

    var name: String,

    var age: Int

)
```

Prefer:

```kotlin
class User(

    val name: String,

    val age: Int

)
```

until mutation required.

**Mistake 2**

Heavy logic in `init`.

Bad:

```kotlin
init {

    networkCall()

    databaseQuery()

}
```

Object creation becomes expensive.

Keep `init` lightweight.

## Interview Notes

**What Is A Class?**

Blueprint describing an object.

**What Is An Object?**

Runtime instance of a class.

**What Is A Property?**

Data belonging to an object.

**Difference Between Constructor Parameter And Property?**

`name: String` -> Parameter only.

`val name: String` -> Property.

**What Is init?**

Initialization block executed during object creation.

**Execution Order?**

Constructor Parameters -> Property Initialization -> `init` Block

**Why Use Secondary Constructors?**

Provide alternate ways to create objects.

**Why Prefer Primary Constructors?**

Cleaner and more idiomatic.

## Exercises

**Exercise 1**

Predict output.

```kotlin
class User(
    val name: String
)

fun main() {

    val user =
        User("Akshay")

    println(user.name)
}
```

**Exercise 2**

Will this compile?

```kotlin
class User(
    name: String
)

fun main() {

    val user =
        User("Akshay")

    println(user.name)
}
```

Why?

**Exercise 3**

Predict output.

```kotlin
class User(
    val age: Int
){

    init {

        println(age)

    }
}

fun main() {

    User(22)

}
```

**Exercise 4**

What runs first?

```kotlin
class User(
    val name: String
){

    init {
        println("Init")
    }
}
```

Constructor parameter assignment? `init` block?

Explain.