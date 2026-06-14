# Module 2 — Classes, Objects, Properties & Constructors

## Chapter 2.2 — Properties, Getters, Setters, Backing Fields & Backing Properties

**Why This Chapter Is Important**

This chapter is one of the most important chapters for Android developers.

Because these patterns:

```kotlin
private val _state = MutableStateFlow(...)
val state = _state.asStateFlow()

private var _name = ""

var age = 0
    set(value) { ... }

val isValid
    get() = ...
```

all come from understanding:

* Properties
* Getters
* Setters
* Backing Fields
* Backing Properties
* Encapsulation

Most Android developers use these daily.

Very few understand what Kotlin generates behind the scenes.

### Part 1 — Property vs Variable

Most people think:

```kotlin
val name = "Akshay"
```

is just a variable.

Not exactly.

In Kotlin:

Property ≠ Variable

A property is a higher-level language feature.

Example:

```kotlin
class User {

    val name = "Akshay"

}
```

**Theory**

When Kotlin sees:

```kotlin
val name = "Akshay"
```

it doesn't simply create memory.

Compiler may generate:

```java
private final String name = "Akshay";

public String getName() {
    return name;
}
```

Notice:

Field + Getter

are generated.

**First Working Example**

```kotlin
class User {

    val name = "Akshay"

}

fun main() {

    val user = User()

    println(user.name)

}
```

Output:

`Akshay`

**Line By Line**

`class User {` -> Create class.

`val name = "Akshay"` -> Create read-only property.

Compiler infers:

```kotlin
val name: String = "Akshay"
```

`val user = User()` -> Create object.

`println(user.name)` -> Looks like field access.

Actually:

```kotlin
println(user.getName())
```

behind the scenes.

**Important Interview Question**

*When you write:*

```kotlin
user.name
```

*Are you directly accessing the field?*

Answer:

Not necessarily.

Usually getter is called.

### Part 2 — Getter

Every property has a getter.

Example:

```kotlin
class User {

    val name = "Akshay"

}
```

Compiler generates conceptually:

```java
private final String name = "Akshay";

public String getName() {
    return name;
}
```

**Custom Getter**

Now things get interesting.

Example:

```kotlin
class User {

    val fullName: String
        get() {
            return "Akshay Kumar"
        }

}
```

Usage:

```kotlin
fun main() {

    val user = User()

    println(user.fullName)

}
```

Output:

`Akshay Kumar`

**Line By Line**

`val fullName: String` -> Declare property.

`get()` -> Custom getter. Runs every access.

`return "Akshay Kumar"` -> Returned value becomes property value.

**Important Observation**

No field exists here.

Every access executes getter.

Example:

```kotlin
class Counter {

    val value: Int
        get() {
            println("Getter Called")
            return 10
        }

}
```

Usage:

```kotlin
fun main() {

    val counter = Counter()

    println(counter.value)

    println(counter.value)

}
```

Output:

```text
Getter Called
10

Getter Called
10
```

Getter runs every time.

### Part 3 — Setter

Mutable properties have setters.

Example:

```kotlin
class User {

    var age = 22

}
```

Compiler conceptually generates:

```java
private int age = 22;

public int getAge() {
    return age;
}

public void setAge(int value) {
    age = value;
}
```

**Working Example**

```kotlin
class User {

    var age = 22

}

fun main() {

    val user = User()

    user.age = 30

    println(user.age)

}
```

Output:

`30`

**What Happens Internally**

This:

`user.age = 30`

actually becomes:

`user.setAge(30)`

This:

`println(user.age)`

becomes:

`println(user.getAge())`

### Part 4 — Custom Setter

Very common in production code.

Example:

```kotlin
class User {

    var age = 0
        set(value) {
            if(value >= 0) {
                field = value
            }
        }

}
```

Usage:

```kotlin
fun main() {

    val user = User()

    user.age = 25

    println(user.age)

}
```

Output:

`25`

Now:

```kotlin
user.age = -10
```

Ignored.

**Line By Line**

`set(value)` -> `value` contains incoming value.

Example:

`user.age = 25`

Means:

`value = 25`

inside setter.

`field = value` -> stores value.

### Part 5 — What Is field?

Very important.

Interview favorite.

Example:

```kotlin
var age = 0
    set(value) {
        field = value
    }
```

Question:

*What is field?*

Answer:

Special compiler-generated backing field reference.

Think:

```text
Property
  |
  |
  V
field
```

Without `field`:

```kotlin
var age = 0
    set(value) {
        age = value
    }
```

Danger.

Why?

Because:

`age = value`

calls setter again.

Setter calls setter.
Setter calls setter.
Setter calls setter.

Infinite recursion.

Eventually:

`StackOverflowError`

Correct:

`field = value`

Direct storage.

### Part 6 — Backing Field

Now let's define it properly.

Theory:

A backing field is the actual memory location storing property value.

Example:

```kotlin
var age = 22
```

Compiler conceptually creates:

```java
private int age = 22;
```

This private storage is backing field.

Property:
`age`

Field:
`private int age`

generated by compiler.

**When Is Backing Field Created?**

Example:

```kotlin
var age = 22
```

Yes. Backing field created.

Example:

```kotlin
val fullName: String
    get() = "Akshay"
```

No. No value stored.
Getter calculates every time.
No backing field required.

**Interview Question**

*Does every property have a backing field?*

No.
Only when property needs storage.

### Part 7 — Backing Property

One of the most important Android concepts.

Suppose:

```kotlin
class User {
    var name = ""
}
```

Problem: Everyone can modify. Need controlled access.

Solution: Backing Property Pattern.

Example:

```kotlin
class User {

    private var _name = ""

    val name
        get() = _name

}
```

**Line By Line**

`private var _name = ""` -> Actual mutable storage.

`val name` -> Public property. Read only.

`get() = _name` -> Expose value. Hide mutability.

Usage:

```kotlin
fun main() {

    val user = User()

    println(user.name)

}
```

Works.

This fails:

```kotlin
user.name = "Akshay"
```

Because `name` is read-only.

**Android ViewModel Pattern**

Now the famous pattern.

Example:

```kotlin
private val _state =
    MutableStateFlow(0)

val state =
    _state.asStateFlow()
```

Why?

Because:
* `ViewModel` can update state
* `UI` can only observe state

Without backing property:

```kotlin
val state =
    MutableStateFlow(0)
```

UI could modify state.

Dangerous.

With backing property:

Private mutable. Public immutable.

Perfect architecture.

**Memory Diagram**

```text
private val _state
```

Actual storage.

```text
ViewModel
   |
   |
 MutableStateFlow
```

Public:

```text
val state
```

Just safe view.

**Real Android Example**

```kotlin
class PlayerViewModel {

    private val _songName =
        MutableStateFlow("")

    val songName =
        _songName.asStateFlow()

}
```

UI:

```kotlin
viewModel.songName.collect()
```

Allowed.

UI:

```kotlin
viewModel.songName.value = "New"
```

Not allowed. Excellent.

### Common Mistakes

**Mistake 1**

Recursive setter.

Wrong:

```kotlin
set(value) {
    age = value
}
```

Correct:

```kotlin
set(value) {
    field = value
}
```

**Mistake 2**

Exposing `MutableStateFlow` publicly.

Wrong:

```kotlin
val state =
    MutableStateFlow(...)
```

Correct:

```kotlin
private val _state =
    MutableStateFlow(...)

val state =
    _state.asStateFlow()
```

## Interview Notes

**What is a Property?**

High-level Kotlin construct representing object state.

**What is Getter?**

Function returning property value.

**What is Setter?**

Function modifying property value.

**What is field?**

Compiler-generated reference to backing field.

**What is Backing Field?**

Actual memory storage for property.

**What is Backing Property?**

Private mutable property exposed through public read-only property.

**Does Every Property Have Backing Field?**

No.
Computed properties may not.

**Why Use _state and state?**

Encapsulation.
Prevent external mutation.

**Senior Android Takeaway**

This chapter is the foundation for:

* `private val _uiState` -> `val uiState`
* `private val _events` -> `val events`
* `private var _token` -> `val token`

These patterns appear in:
* MVVM
* MVI
* Compose
* StateFlow
* SharedFlow
* KMP

If you truly understand getters, setters, backing fields, and backing properties, a huge portion of Android architecture suddenly starts making sense.

## Exercises

**1. Predict Output**

```kotlin
class User {

    val name: String
        get() = "Akshay"

}

fun main() {

    val user = User()

    println(user.name)

}
```

**2. Why does this crash?**

```kotlin
var age = 0
    set(value) {
        age = value
    }
```

**3. Does this property have a backing field?**

```kotlin
val fullName: String
    get() = "Akshay Kumar"
```

Explain why.