# Module 3 — Functional Kotlin

Before Lambdas.  
Before `map`.  
Before `filter`.  
Before `Flow`.

You need to understand one fundamental question:

**Why Functional Programming Exists**

Most developers skip this.  
Then later:

```kotlin
list.map { }
```

becomes magic syntax they memorize.  
I don't want that.

---

## Chapter 3.1 — Functions as First-Class Citizens

### What Does "First-Class Citizen" Mean?

Let's start with a normal value.

**Example**

```kotlin
fun main() {
    val age = 22
    println(age)
}
```

Output:
`22`

Question:  
Can `Int` be:
* Stored?
* Passed?
* Returned?

Yes.

**Stored**
```kotlin
val age = 22
```

**Passed**
```kotlin
fun printAge(age: Int) {
    println(age)
}
```

**Returned**
```kotlin
fun getAge(): Int {
    return 22
}
```

Because `Int` is a:  
**First-Class Citizen**

**Definition**

A thing is first-class if it can be:
1. Stored in variables
2. Passed to functions as arguments
3. Returned from functions

### Traditional Languages vs Kotlin

Many old languages (like early versions of Java) treated functions specially.

Example idea:  
*Variables are values.*  
*Functions are separate. They only execute logic.*

**Theory Insight:** In older Java, if you wanted to pass a function (like a click action), you couldn't just pass the function itself. You had to create an Interface (e.g., `OnClickListener`), create an object of that interface, and pass the object. The function was "trapped" inside the object.

Kotlin says:  
**Functions are values too.**

This changes everything.

---

### Normal Function

```kotlin
fun greet() {
    println("Hello")
}
```

Question:  
Can we store this function in a variable?

Answer:  
Yes.

---

### Function Reference

**Working Example**

```kotlin
fun greet() {
    println("Hello")
}

fun main() {
    // Storing the function reference, NOT executing it
    val greeting = ::greet

    // Executing the stored function
    greeting()
}
```

Output:
`Hello`

**Line By Line**

`fun greet()`  
Normal function declaration.

`val greeting`  
Variable.

`::greet`  
Function reference.  
Meaning: Store the function itself. Do not execute it.

**Important Distinction:**

This:  
`greet()`  
means: *Execute function.*

This:  
`::greet`  
means: *Reference function.*

Huge difference.

---

### Memory Model

Imagine:

```kotlin
val greeting = ::greet
```

Memory conceptually:

```text
greeting
    |
    |
    V
reference to greet()
```

Later:

```kotlin
greeting()
```

executes the referenced function.

---

### Why This Matters For Android

Because Compose does this constantly.

**Example**

```kotlin
Button(
    onClick = { }
)
```

Question:  
What is `onClick`?

Answer:  
A function.  
Not a `String`.  
Not an `Int`.  
A function.

This becomes possible entirely because:  
**Functions are values.**

---

### Function Type

Let's inspect the type.

**Example**

```kotlin
fun greet() {
    println("Hello")
}
```

Reference:

```kotlin
val greeting = ::greet
```

Compiler sees the type as:  
`() -> Unit`

**What Does This Mean?**

Let's break it down.

`()`  
Input parameters.  
None.

`Unit`  
Return type.

Meaning:  
Function  
takes nothing  
returns nothing

**Visual**

`() -> Unit`

Input  → Output  
Nothing → Nothing

---

### Another Example

```kotlin
fun square(
    number: Int
): Int {
    return number * number
}
```

Type becomes:  
`(Int) -> Int`

**Visual**

`(Int) -> Int`

Input → Output  
Int   → Int

**Working Example**

```kotlin
fun square(
    number: Int
): Int {
    return number * number
}

fun main() {
    // Store function reference
    val operation = ::square

    // Execute stored function by passing an argument
    println(
        operation(5)
    )
}
```

Output:
`25`

**Line By Line**

`val operation = ::square`  
Store function.

`operation(5)`  
Execute stored function.  
Equivalent to: `square(5)`

Output: `25`

---

### Functions Stored In Explicit Variables

Now things get interesting.

Instead of compiler inferring:

```kotlin
val operation = ::square
```

We can explicitly write the type:

```kotlin
val operation: (Int) -> Int = ::square
```

Let's break that down.

`(Int)`  
Function accepts Int.

`-> Int`  
Returns Int.

So this variable can **only** hold functions matching:  
`Int → Int`

**Valid Example**

```kotlin
fun cube(
    x: Int
): Int {
    return x * x * x
}
```

Can store:

```kotlin
val operation: (Int) -> Int = ::cube
```

Compiler is happy because `cube` accepts an `Int` and returns an `Int`.

**Invalid Example**

```kotlin
fun greet() {
}
```

Trying:

```kotlin
val operation: (Int) -> Int = ::greet
```

Compilation error.

Why?  
Expected: `(Int) -> Int`  
Got: `() -> Unit`

**Mental Model**

Think of `(Int) -> Int` as a **contract**.  
Any function matching:  
Input: `Int`  
Output: `Int`  
can be safely stored in this variable.

---

### First Look At Higher-Order Functions

**Definition:**  
A function that accepts another function as a parameter, or returns a function.

**Theory Insight:** This is how you pass *behavior* instead of just *data*. Instead of telling a function *what* data to work on, you can tell a function *how* to react when something happens.

**Example**

```kotlin
fun execute(
    action: () -> Unit // Parameter is a function
) {
    // Executing the passed function
    action()
}
```

**Line By Line**

`fun execute(`  
Normal function declaration.

`action: () -> Unit`  
Parameter is a function.  
Not `Int`. Not `String`. A function.

`action()`  
Execute incoming function.

**Working Example**

```kotlin
fun greet() {
    println("Hello")
}

fun execute(
    action: () -> Unit
) {
    action()
}

fun main() {
    // Passing the reference of greet to execute
    execute(::greet)
}
```

Output:
`Hello`

**Execution Flow**

`execute()`  
    ↓  
`greet()`  
    ↓  
`Hello`

---

### Why This Is Huge For Android

Because:

```kotlin
Button(
    onClick = {}
)
```

works exactly like this.

The Compose component receives a **Function**, stores it, and executes it later (when the user taps the screen).

**Android Real-World Example**

Imagine a dialog function:

```kotlin
fun showDialog(
    onConfirm: () -> Unit
) {
    println("Dialog Open")
    
    // Wait for user to click Yes, then:
    onConfirm()
}
```

**Usage:**

```kotlin
showDialog {
    println("Delete Item")
}
```

Output:
```text
Dialog Open
Delete Item
```

---

## Interview Notes

**What Does First-Class Citizen Mean?**  
It means an entity can be:
* Stored in variables
* Passed as arguments
* Returned from functions

**Are Functions First-Class Citizens In Kotlin?**  
Yes. This is the foundation of Functional Programming in Kotlin.

**What Is Function Reference?**  
`::functionName`  
It grabs a reference to the function without executing it.

**Difference Between `greet()` and `::greet`?**  
`greet()` -> Execute the function.  
`::greet` -> Reference the function as an object.

**What Does `(Int) -> Int` Mean?**  
A function type contract.  
The function accepts an `Int` and returns an `Int`.

**What Is A Higher-Order Function?**  
A function that:
* Accepts a function as a parameter
* OR returns a function.

---

## Exercises

**1. Predict Output**

```kotlin
fun greet() {
    println("Hello")
}

fun main() {
    val action = ::greet
    action()
}
```

**2. What Is The Function Type Here?**

```kotlin
fun square(
    x: Int
): Int {
    return x * x
}
```

**3. Why Does This Compile?**

```kotlin
fun execute(
    action: () -> Unit
) {
    action()
}
```

**4. Real Android Question**

What is the exact Kotlin type of:

```kotlin
onClick = {}
```

inside a Compose `Button`?