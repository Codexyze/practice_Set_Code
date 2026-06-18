# Module 2 — Object-Oriented Kotlin

## Chapter 2.5 — Objects, Singletons, Companion Objects, Anonymous Objects & Object Expressions

### Why This Chapter Matters

This chapter explains:

* `object`
* `companion object`
* `object : Interface {}`

which you see everywhere in Android:

* `Dispatchers.IO`
* `Color.Red`
* `Retrofit.Builder()`
* `DiffUtil.ItemCallback()`
* `object : OnClickListener {}`

Many developers use them daily but don't understand:

* Why `object` exists
* Why `companion object` exists
* How Kotlin replaced `static`
* How singleton actually works

This chapter fixes that.

### Part 1 — Problem Before object

Let's start with normal classes.

**Working Example**

```kotlin
class UserManager {

    fun login() {
        println("Login")
    }

}

fun main() {

    // Object 1 created
    val manager1 = UserManager()

    // Object 2 created
    val manager2 = UserManager()

    // Checking referential equality (are they the same object in memory?)
    println(manager1 === manager2)

}
```

Output:

`false`

Why?

Let's analyze memory.

```kotlin
val manager1 = UserManager()
```

Object 1 created.

Memory:

```text
Heap

UserManager Object A
```

```kotlin
val manager2 = UserManager()
```

Object 2 created.

Memory:

```text
Heap

UserManager Object A

UserManager Object B
```

Question:

Do we really need multiple `UserManager`s?

Usually:

No.

There should be one. 

**Theory Insight:** When you create multiple objects of a class that has no specific instance state (like a manager doing identical tasks), you waste heap memory and risk state desynchronization. If one manager updates a value, the second manager won't know about it. 

Examples:

* Database Manager
* Analytics Manager
* Player Manager
* Settings Manager
* Cache Manager

One instance is enough.

### Part 2 — Singleton Pattern

Theory:

Singleton = 
Exactly one object exists 
during application lifetime.

**Theory Insight:** Singletons guarantee that every part of your application interacts with the exact same instance in memory. This is crucial for managing shared resources (like a network connection pool or a database). Kotlin's `object` declaration is completely thread-safe by default and initializes lazily (only when you access it for the first time).

Java solution:

Very verbose. Requires private constructors, static variables, and synchronized blocks.

Kotlin solution:

```kotlin
// Defines a class and creates its one and only instance simultaneously
object UserManager {

    fun login() {
        println("Login")
    }

}
```

**First Object Declaration Working Example**

```kotlin
object UserManager {

    fun login() {
        println("Login")
    }

}

fun main() {
    // Accessing the singleton directly using its name
    UserManager.login()
}
```

Output:

`Login`

**Line-by-Line Explanation**

`object UserManager`

Creates:

Class 
+ 
Single Instance

at the same time.

Compiler conceptually generates:

```kotlin
class UserManager {

    companion object {
        val INSTANCE = UserManager()
    }

}
```

Not exact code, but close enough.

**Important**

You cannot do:

```kotlin
UserManager()
```

Compilation error.

Because instance already exists.

**Memory Diagram**

```text
Heap

UserManager
     |
     |
Single Instance
```

Only one object.

**Android Example**

Imagine:

```kotlin
object Analytics {

    fun track(event: String) {
        println(event)
    }

}
```

Usage:

```kotlin
Analytics.track("Login")
```

No need:

```kotlin
Analytics().track(...)
```

**Interview Question**

Difference Between
`class UserManager`

and

`object UserManager`

*Class:*

Multiple instances possible

*Object:*

Exactly one instance

### Part 3 — When NOT To Use Singleton

Many beginners abuse singletons.

Bad:

```kotlin
object UserRepository
```

Problem:

* Hard to test (Singletons carry state between tests)
* Hard to mock (Cannot easily replace `UserRepository` with a fake one)
* Global state (Any part of the app can change it unexpectedly)
* Tight coupling (Classes explicitly depend on the global object, not an interface)

Modern Android prefers:

```kotlin
// Dependency Injection handles the lifecycle and instance count
@Inject constructor(...)
```

with DI (like Hilt or Dagger). DI allows you to configure a class as a `@Singleton`, but you still inject it as a normal object, keeping it testable and decoupled.

Use `object` (singleton) only when truly global and stateless.

Examples:

* Logger
* Constants
* Analytics
* Feature Flags

### Part 4 — Companion Object

This is Kotlin's replacement for:

`static`

**Java Example**

```java
class User {
    // Attached to the class itself, not an instance
    static final String APP_NAME = "Spotify";
}
```

Usage:

```java
User.APP_NAME
```

Kotlin has NO `static` keyword.

Instead:

```kotlin
class User {

    // An object scoped to the User class
    companion object {
        const val APP_NAME = "Spotify"
    }

}
```

**Usage**

```kotlin
fun main() {
    println(User.APP_NAME)
}
```

Output:

`Spotify`

**Line-by-Line**

`companion object`

Creates object attached to class.

Think:

```text
User Class
      |
      |
 Companion Object
```

Usage:

`User.APP_NAME`

without creating `User`.

**Theory Insight:** Why did Kotlin remove `static`? Kotlin promotes true Object-Oriented principles. A `companion object` is an *actual runtime object* inside the class. Unlike Java's `static`, a companion object can implement interfaces, extend classes, and even be passed around as a variable.

**Why Does Companion Exist?**

Because sometimes behavior belongs to class.

Not instance.

Example:

`User.createGuest()`

Makes sense.

`user.createGuest()`

Doesn't.

**Factory Example**

Very common interview topic.

```kotlin
class User(
    val name: String
) {

    // Factory pattern hidden inside the companion object
    companion object {
        fun createGuest(): User {
            return User("Guest")
        }
    }

}
```

**Usage**

```kotlin
fun main() {
    // Generating a specific type of user without calling the constructor directly
    val guest = User.createGuest()
    println(guest.name)
}
```

Output:

`Guest`

**Why Is This Better?**

Instead of:

`User("Guest")`

we provide intent.

`User.createGuest()`

explains WHY object exists.

**Android Example**

Retrofit uses builders.

Example:

`Retrofit.Builder()`

Factory patterns often rely on similar ideas.

### Part 5 — const val

Interview favorite.

Inside companion:

```kotlin
class User {
    companion object {
        const val MAX_USERS = 100
    }
}
```

Usage:

```kotlin
println(User.MAX_USERS)
```

**Why const?**

Compiler replaces value at compile time.

Example:

```kotlin
const val APP_NAME = "Lythm"
```

Compiler literally inserts:

`"Lythm"`

where needed.

**Theory Insight:** Without `const`, a standard `val` creates a hidden getter function in the background. By using `const val`, you bypass the getter completely. The compiler inlines the raw value right where the variable is called, making it slightly more performant and perfectly suited for constants like `Intent` keys or Room table names.

Restrictions:

Must be:

* Primitive (Int, Boolean, etc.)
* String
* Top-level
* Object
* Companion Object

### Part 6 — Object Expressions

Very important.

You see these constantly in Android.

Example:

```kotlin
// We don't need a named class, just the implementation right here
object : Runnable {
    override fun run() {
        println("Running")
    }
}
```

Question:

Why no name?

Because we need object only once.

**Working Example**

```kotlin
interface Player {
    fun play()
}

fun main() {

    // Creating a one-time anonymous object that implements Player
    val player = object : Player {
        override fun play() {
            println("Playing")
        }
    }

    player.play()

}
```

Output:

`Playing`

**Line-by-Line**

`object :`

Create anonymous object.

`Player`

Implement Player interface.

`override fun play()`

Provide implementation.

No class name needed. 

**Theory Insight:** Object expressions can also access and capture variables from the surrounding scope (called a closure). In Java, captured variables had to be `final`, but Kotlin allows you to modify non-final variables inside an object expression, making them extremely flexible for event handlers.

**Android Example**

Old Android:

```kotlin
button.setOnClickListener(
    object : View.OnClickListener {
        override fun onClick(v: View?) {
            // Action here
        }
    }
)
```

Anonymous object created.

Used once.

Never reused.

### Part 7 — Object Expression vs Singleton Object

Many interviews ask this.

**Singleton:**

```kotlin
object Analytics
```

Created once.

Lives entire app lifetime. Initialized lazily.

**Object Expression:**

```kotlin
object : Player {}
```

Created at runtime.

New object each execution.

**Example**

```kotlin
fun createPlayer() = 
    object : Player {
        override fun play() {
            // ...
        }
    }
```

Every call:

New object.

### Part 8 — Data Objects

Newer Kotlin feature (introduced to improve sealed hierarchies).

**Example**

```kotlin
sealed interface UiState {
    data object Loading : UiState
}
```

Generated:

* `toString()`
* `equals()`
* `hashCode()`

similar to `data class`.

**Why Compose Uses This**

Without:

```kotlin
object Loading
```

Output:

`Loading@6d03e736` (Ugly memory reference)

With:

```kotlin
data object Loading
```

Output:

`Loading`

Much cleaner.

**Theory Insight:** Before `data object` existed, having a mix of `data class Success(val data: String)` and `object Loading` meant that `Loading` didn't have a pretty `toString()`. By adding `data object`, sealed classes and interfaces became completely symmetric and perfect for state logging.

**Real Android Example**

```kotlin
sealed interface PlayerState {
    
    data object Loading : PlayerState
    
    data object Idle : PlayerState
    
    data class Playing(
        val song: String
    ) : PlayerState

}
```

This pattern is extremely common today.

### JVM View

This:

```kotlin
object Analytics
```

roughly becomes:

```java
// Java decompiled view of a Kotlin object
public final class Analytics {

    // Static reference to the single instance
    public static final Analytics INSTANCE = new Analytics();

    private Analytics() { 
        // Private constructor prevents external instantiation
    }
}
```

Usage:

`Analytics.INSTANCE`

hidden by Kotlin.

### Common Mistakes

**Mistake 1**

Using singleton for everything.

Bad:

```kotlin
object UserRepository
```

Usually use DI (Hilt/Dagger) for repositories.

**Mistake 2**

Using companion object when top-level function is enough.

Bad:

```kotlin
class DateUtils {
    companion object {
        fun formatDate() {}
    }
}
```

Better:

```kotlin
// In a file called DateUtils.kt
fun formatDate() {}
```

### Interview Notes

**What Is Singleton?**
Exactly one instance exists throughout the application lifecycle.

**How Does Kotlin Create Singleton?**
Using the `object` keyword.

**Does Kotlin Have `static`?**
No. Uses `companion object` or top-level functions instead.

**Difference Between `object` and `companion object`?**
* `object`: Standalone singleton
* `companion object`: Singleton attached to a specific class context

**Difference Between `object` and `object : Interface`?**
* `object Analytics`: Singleton (one instance forever).
* `object : Player {}`: Anonymous runtime object (created new every time execution reaches it).

**Why Use Companion Object?**
Factory methods. Constants. Class-level behavior.

**What Is `const val`?**
Compile-time constant. Value is inlined directly at the call site, eliminating getter overhead.

**Senior Android Takeaway**

Modern Android commonly uses:

`companion object`

for:

* Factory methods
* Constants
* Intent keys
* Navigation routes

and:

`object`

for:

* Analytics
* Loggers
* Feature Flags

and:

`object : Listener {}`

for:

* Callbacks
* Listeners
* Adapters
* One-off implementations

### Exercises

**1. Predict Output**

```kotlin
object Analytics

fun main() {
    val a = Analytics
    val b = Analytics

    println(a === b)
}
```

Why?

**2. What Prints?**

```kotlin
class User {
    companion object {
        const val APP = "Lythm"
    }
}

fun main() {
    println(User.APP)
}
```

**3. Difference?**

`object PlayerManager`

vs

`object : Player {}`

Explain completely.