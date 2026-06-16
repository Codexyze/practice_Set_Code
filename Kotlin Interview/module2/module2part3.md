# Module 2 — Classes, Objects, Properties & Constructors

## Chapter 2.3 — Data Classes, copy(), equals(), hashCode(), toString(), componentN(), Destructuring

### Why Data Classes Exist

Let's start with a problem.

Suppose you're building a music app.

You create:

```kotlin
class Song(
    val id: Int,
    val title: String,
    val artist: String
)
```

Now:

```kotlin
fun main() {

    val song = Song(
        1,
        "Believer",
        "Imagine Dragons"
    )

    println(song)
}
```

Output:

`Song@4e25154f`

Question:

Is that useful?

No.

You wanted:

```text
Song(
    id=1,
    title=Believer,
    artist=Imagine Dragons
)
```

Another problem:

```kotlin
val song1 =
    Song(1, "Believer", "Imagine Dragons")

val song2 =
    Song(1, "Believer", "Imagine Dragons")

println(song1 == song2)
```

Expected:

`true`

Actual:

`false`

Why?

Because normal classes compare references.

Memory:

```text
song1 ----> Object A

song2 ----> Object B
```

Different objects.

Kotlin realized:

Many classes only hold data.

Examples:

* Room Entity
* Retrofit DTO
* UiState
* Domain Model
* Navigation Arguments
* API Response

So Kotlin introduced:

### Data Classes

**First Data Class**

```kotlin
data class Song(

    val id: Int,

    val title: String,

    val artist: String

)
```

Notice:

`data`

keyword.

### What Compiler Generates

When compiler sees:

```kotlin
data class Song(
    val id: Int,
    val title: String
)
```

Compiler automatically generates:

* `equals()`
* `hashCode()`
* `toString()`
* `copy()`
* `component1()`
* `component2()`

and more.

### Generated toString()

**Working Example**

```kotlin
data class Song(

    val id: Int,

    val title: String

)

fun main() {

    val song =
        Song(
            1,
            "Believer"
        )

    println(song)
}
```

Output:

`Song(id=1, title=Believer)`

**Line By Line**

`data class Song(`

Tell compiler:

This class primarily represents data.

`println(song)`

Looks simple.

Actually calls:

`song.toString()`

Generated automatically.

**Why Android Developers Love This**

Imagine logging:

```kotlin
println(uiState)
```

Output:

```text
PlayerUiState(
    isPlaying=true,
    songName=Believer
)
```

Super useful.

### equals()

This is huge.

**Normal Class**

```kotlin
class User(
    val id: Int
)
```

Example:

```kotlin
fun main() {

    val user1 = User(1)

    val user2 = User(1)

    println(user1 == user2)

}
```

Output:

`false`

Because:

Reference comparison

Memory:

```text
user1 --> Object A

user2 --> Object B
```

Different objects.

**Data Class Version**

```kotlin
data class User(
    val id: Int
)
```

Example:

```kotlin
fun main() {

    val user1 = User(1)

    val user2 = User(1)

    println(user1 == user2)

}
```

Output:

`true`

Why?

Generated:

`override fun equals(...)`

compares properties.

**Mental Model**

Instead of:

*Same object?*

It asks:

*Same data?*

**Interview Question**

Difference between:

`==`

and

`equals()`

Answer:

`a == b`

internally calls:

`a?.equals(b)`

### hashCode()

Most developers memorize.

Few understand.

**Theory**

HashMap uses:

`hashCode()`

to locate objects efficiently.

Suppose:

```kotlin
val users =
    hashSetOf<User>()
```

Without proper hashCode:

* Duplicate objects
* Lookup failures
* Performance issues

can happen.

Data classes automatically generate:

`hashCode()`

based on properties.

**Interview Rule**

Always remember:

If `equals` changes

`hashCode` must change too

Data classes handle this automatically.

### copy()

This is one of the most important features for Compose.

**Working Example**

```kotlin
data class User(

    val name: String,

    val age: Int

)

fun main() {

    val user =
        User(
            "Akshay",
            22
        )

    val updatedUser =
        user.copy(
            age = 23
        )

    println(user)

    println(updatedUser)

}
```

Output:

```text
User(name=Akshay, age=22)

User(name=Akshay, age=23)
```

**Line By Line**

`val updatedUser =`

Create NEW object.

`user.copy(`

Copy existing object.

`age = 23`

Replace only age.

Everything else reused.

**Important**

Original remains unchanged.

`user.age`

still:

`22`

**Why Compose Loves copy()**

Suppose:

```kotlin
data class PlayerUiState(

    val songName: String,

    val isPlaying: Boolean

)
```

Current state:

```kotlin
PlayerUiState(
    songName="Believer",
    isPlaying=false
)
```

User presses play.

Bad:

```kotlin
state.isPlaying = true
```

Mutable state.

Good:

```kotlin
state =
    state.copy(
        isPlaying = true
    )
```

Immutable state.

Predictable state.

This pattern appears everywhere:

* Compose
* MVI
* Redux
* StateFlow
* KMP

### componentN()

Most developers never use this directly.

But they use it indirectly.

Generated automatically.

Example:

```kotlin
data class User(

    val name: String,

    val age: Int

)
```

Compiler generates:

* `component1()`
* `component2()`

Meaning:

`user.component1()`

returns:

`name`

`user.component2()`

returns:

`age`

### Destructuring

Uses `componentN` functions.

**Working Example**

```kotlin
data class User(

    val name: String,

    val age: Int

)

fun main() {

    val user =
        User(
            "Akshay",
            22
        )

    val (name, age) = user

    println(name)

    println(age)

}
```

Output:

```text
Akshay
22
```

**What Compiler Actually Does**

This:

```kotlin
val (name, age) = user
```

becomes:

```kotlin
val name =
    user.component1()

val age =
    user.component2()
```

**Android Example**

Suppose:

```kotlin
data class NetworkResult(

    val code: Int,

    val body: String
)
```

You can do:

```kotlin
val (code, body) = result
```

instead of:

```kotlin
val code = result.code
val body = result.body
```

**Real Compose Example**

```kotlin
data class LoginUiState(

    val email: String = "",

    val password: String = "",

    val isLoading: Boolean = false,

    val error: String? = null

)
```

Update:

```kotlin
_uiState.update {

    it.copy(
        isLoading = true
    )

}
```

This is literally one of the most common patterns in modern Android.

### Data Class Restrictions

Data classes cannot be:

* abstract
* sealed
* open
* inner

Need at least:

one constructor property

Valid:

```kotlin
data class User(
    val name: String
)
```

Invalid:

```kotlin
data class User()
```

### Common Mistakes

**Mistake 1**

Using var everywhere.

Bad:

```kotlin
data class User(

    var name: String,

    var age: Int

)
```

Prefer:

```kotlin
data class User(

    val name: String,

    val age: Int

)
```

Why?

Immutable models.

**Mistake 2**

Manually modifying state.

Bad:

```kotlin
state.isLoading = true
```

Good:

```kotlin
state =
    state.copy(
        isLoading = true
    )
```

**Mistake 3**

Using normal classes for DTOs.

Bad:

```kotlin
class UserResponse(...)
```

Better:

```kotlin
data class UserResponse(...)
```

### JVM View

This:

```kotlin
data class User(

    val name: String,

    val age: Int

)
```

roughly generates:

```java
public final class User {

    private final String name;
    private final int age;

    public User(...) {}

    public String getName(){}

    public int getAge(){}

    public String toString(){}

    public boolean equals(...){}

    public int hashCode(){}

    public User copy(...){}

}
```

Huge amount of boilerplate removed.

## Interview Notes

**Why Data Class?**

Represents data.

Compiler generates useful methods automatically.

**What Does copy() Do?**

Creates new object while reusing existing values.

**Why Is copy() Important For Compose?**

Supports immutable state updates.

**What Is Destructuring?**

Extracting properties into variables.

**How Does Destructuring Work?**

Uses generated:

`componentN()`

functions.

**Why equals() Works For Data Classes?**

Generated based on constructor properties.

**Why hashCode() Matters?**

Required for collections like:

`HashSet`
`HashMap`

**Difference Between Normal Class And Data Class?**

Normal class:

* Reference equality
* No generated methods

Data class:

* Value equality
* Generated methods
  * `copy()`
  * `toString()`
  * `hashCode()`
  * `componentN()`

**Senior Android Takeaway**

If you understand:

* `data class`
* `copy()`
* `equals()`
* `destructuring`

you understand the foundation of:

* Compose State
* UiState
* StateFlow
* MVI
* Redux
* Offline First Models
* KMP Shared Models

Almost every modern Android architecture relies on immutable data classes.

## Exercises

**1. Predict Output**

```kotlin
data class User(
    val name: String
)

fun main() {

    val u1 = User("Akshay")

    val u2 = User("Akshay")

    println(u1 == u2)
}
```

**2. Predict Output**

```kotlin
data class User(
    val name: String,
    val age: Int
)

fun main() {

    val user =
        User("Akshay", 22)

    val updated =
        user.copy(age = 23)

    println(user)
    println(updated)
}
```

**3. What does compiler generate for a data class?**

List all major generated functions.