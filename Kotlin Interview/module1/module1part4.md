# Module 1 — Core Kotlin Language

## Chapter 1.4 — Null Safety Deep Dive

This chapter alone is responsible for a huge percentage of Kotlin's popularity.

Before Kotlin existed, developers lived in fear of:

`NullPointerException`

This became so common that it was called:

*The Billion Dollar Mistake*

by Tony Hoare, the inventor of null references.

### Part 1 — What Is Null?

Imagine:

```kotlin
val name = "Akshay"
```

Memory:

```text
name ----> "Akshay"
```

Reference points to object.

Now:

```kotlin
val name = null
```

Memory:

```text
name ----> nothing
```

No object exists.

Problem:

```kotlin
name.length
```

How can length be calculated if no object exists?

It can't.

Crash.

**Java Problem**

Java allows:

```java
String name = null;
System.out.println(name.length());
```

Runtime:

`NullPointerException`

Crash.

**Kotlin Solution**

Kotlin says:

*If something can be null, you must explicitly declare it.*

### Part 2 — Nullable vs Non-Nullable Types

**Non-Nullable**

```kotlin
fun main() {

    val name: String = "Akshay"

    println(name.length)
}
```

Output:

`6`

Compiler guarantee:

*name can never be null*

Try:

```kotlin
fun main() {

    val name: String = null
}
```

Compilation error.

Not runtime.

Compile time.

**Nullable Type**

Add:

`?`

Example:

```kotlin
fun main() {

    val name: String? = null
}
```

Now compiler allows it.

Mental model:

```text
String
   |
   +---- only String objects

String?
   |
   +---- String objects
   |
   +---- null
```

### Part 3 — Why Compiler Stops You

Example:

```kotlin
fun main() {

    val name: String? = null

    println(name.length)
}
```

Compilation error.

Compiler thinks:

*Maybe null*

*Maybe String*

*I cannot guarantee safety*

So code is rejected.

### Part 4 — Safe Call Operator

One of Kotlin's most famous operators.

`?.`

**Working Example**

```kotlin
fun main() {

    val name: String? = "Akshay"

    println(name?.length)
}
```

Output:

`6`

Now:

```kotlin
fun main() {

    val name: String? = null

    println(name?.length)
}
```

Output:

`null`

No crash.

Mental model:

```text
If object exists
    execute

Else
    return null
```

Equivalent Java:

```java
if(name != null){
    System.out.println(name.length());
}
```

**Chaining Safe Calls**

Example:

```kotlin
data class Address(
    val city: String
)

data class User(
    val address: Address?
)

fun main() {

    val user =
        User(
            Address("Udupi")
        )

    println(
        user.address?.city
    )
}
```

Output:

`Udupi`

If address becomes null:

`null`

No crash.

### Part 5 — Elvis Operator

One of the most used operators in Android.

Symbol:

`?:`

**Working Example**

```kotlin
fun main() {

    val name: String? = null

    val result =
        name ?: "Guest"

    println(result)
}
```

Output:

`Guest`

If value exists:

```kotlin
fun main() {

    val name: String? = "Akshay"

    val result =
        name ?: "Guest"

    println(result)
}
```

Output:

`Akshay`

Mental model:

*Use left side*

*If null*

*Use right side*

**Android Example Idea**

```kotlin
username ?: "Unknown User"
```

Very common.

### Part 6 — let

One of the most misunderstood functions.

**Working Example**

```kotlin
fun main() {

    val name: String? = "Akshay"

    name?.let {

        println(it)

        println(it.length)
    }
}
```

Output:

```text
Akshay
6
```

What is happening?

Compiler says:

*If name exists*

*Execute block*

Equivalent:

```kotlin
if(name != null){

    println(name)

    println(name.length)
}
```

**Why let Exists**

Avoid repeating:

```kotlin
if(user != null){

}
```

everywhere.

### Part 7 — Non-Null Assertion !!

Most dangerous operator.

Example:

```kotlin
fun main() {

    val name: String? = "Akshay"

    println(name!!)
}
```

Output:

`Akshay`

Now:

```kotlin
fun main() {

    val name: String? = null

    println(name!!)
}
```

Runtime:

`KotlinNullPointerException`

Crash.

Meaning:

*Trust me compiler*
*I know it isn't null*

**Interview Question**

*When should `!!` be used?*

Rarely.

Usually:

Never, unless you absolutely know the value cannot be null.

### Part 8 — requireNotNull

Used for function arguments.

**Working Example**

```kotlin
fun printName(
    name: String?
) {

    requireNotNull(name)

    println(name.length)
}

fun main() {

    printName("Akshay")
}
```

Output:

`6`

Now:

```kotlin
printName(null)
```

Runtime:

`IllegalArgumentException`

Meaning:

*Caller gave invalid argument*

### Part 9 — checkNotNull

Very similar.

**Working Example**

```kotlin
fun main() {

    val userName: String? = "Akshay"

    checkNotNull(userName)

    println(userName.length)
}
```

Output:

`6`

If null:

`IllegalStateException`

Difference:

*   `requireNotNull`: Argument problem
*   `checkNotNull`: State problem

### Part 10 — Smart Cast with Null

**Working Example**

```kotlin
fun main() {

    val name: String? = "Akshay"

    if(name != null){

        println(name.length)
    }
}
```

Compiler smart casts:

`String?`

into:

`String`

inside block.

No cast needed.

### Part 11 — Platform Types

Most interview candidates fail this.

Java code:

```java
String getName()
```

Can it return null?

Compiler doesn't know.

Kotlin sees:

`String!`

called:

**Platform Type**

Dangerous because:

*Maybe null*
*Maybe not null*

Compiler can't guarantee.

This often happens with:

* Java Libraries
* Old Android APIs
* Legacy Code

**Why NPE Still Exists In Kotlin**

Interview favorite.

People say:

*Kotlin eliminates NullPointerException*

Wrong.

Kotlin *reduces* them.

Still possible via:

1. `!!` (Non-Null Assertion)
2. Platform Types (`String!`)
3. Java Interop
4. Bad Initialization

## Interview Notes

**Difference Between String and String?**

*   `String`: Cannot be null.
*   `String?`: Can be null.

**What Does `?.` Do?**

Executes access only if object exists.
Otherwise returns null.

**What Does `?:` Do?**

Provides fallback value.

**What Does `let` Do?**

Runs block only if object is non-null.

**Difference Between `requireNotNull` and `checkNotNull`**

*   `requireNotNull`: Argument validation
*   `checkNotNull`: State validation

**Why Is `!!` Dangerous?**

It bypasses compiler safety.
Can crash at runtime.

**Does Kotlin Completely Remove NPE?**

No.
It significantly reduces them.

## Exercises

**1. Predict Output**

```kotlin
fun main() {

    val name: String? = null

    println(name?.length)
}
```

**2. Predict Output**

```kotlin
fun main() {

    val name: String? = null

    println(
        name ?: "Guest"
    )
}
```

**3. Predict Output**

```kotlin
fun main() {

    val name: String? = null

    println(name!!)
}
```

**4. Explain**

Why does this compile?

```kotlin
val name: String? = "Akshay"

if(name != null){

    println(name.length)
}
```