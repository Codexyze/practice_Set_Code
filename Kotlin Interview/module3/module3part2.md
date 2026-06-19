# Module 3 — Functional Kotlin

## Chapter 3.2 — Lambdas Deep Dive

### Why Lambdas Exist

Before lambdas, if we wanted to pass behavior, we usually created classes.

Imagine old Java style:

```java
interface ClickListener {
    void onClick();
}

class LoginClickListener implements ClickListener {
    @Override
    public void onClick() {
        System.out.println("Login Clicked");
    }
}

// Usage:
button.setClickListener(
    new LoginClickListener()
);
```

Question:  
For one click event...

Do we really need:
* Interface
* Class
* Object
* Method

?

Too much boilerplate.

Kotlin's answer:

```kotlin
button.setClickListener {
    println("Login Clicked")
}
```

Same behavior.  
Much less code.

---

### What Is A Lambda?

**Definition:**  
A lambda is an **anonymous function**.

Anonymous means:  
Function without a name.

**Normal function:**

```kotlin
fun greet() {
    println("Hello")
}
```

Has name: `greet`

**Lambda:**

```kotlin
{
    println("Hello")
}
```

No name.

---

### First Lambda

**Working Example**

```kotlin
fun main() {
    // A lambda is an object that can be stored in a variable
    val greeting = {
        println("Hello")
    }

    // We execute it by calling invoke() implicitly
    greeting()
}
```

Output:
`Hello`

**Line By Line**

`val greeting =`  
Create variable.

`{`  
Start lambda.

`println("Hello")`  
Lambda body.

`}`  
End lambda.

Compiler infers type: `() -> Unit`

`greeting()`  
Execute lambda.

---

### Memory Model

```kotlin
val greeting = {
    println("Hello")
}
```

Conceptually:

```text
greeting
    |
    |
    V

Function Object
```

**Important:**  
A lambda is actually an object.  
Not magic.  
Not syntax only.

### Lambda Type

**Example:**

```kotlin
val greeting = {
    println("Hello")
}
```

Type: `() -> Unit`

Meaning:  
Input  -> Output  
Nothing -> Nothing

---

### Lambda Returning Value

**Example**

```kotlin
val square = {
    5 * 5
}

fun main() {
    println(square())
}
```

Output:
`25`

**Important Kotlin Rule**  
The last expression inside a lambda becomes its return value.

**Example**

```kotlin
val operation = {
    println("Calculating")
    100 // This is the last expression, so it's the return value
}

fun main() {
    val result = operation()
    println(result)
}
```

Output:
```text
Calculating
100
```

Compiler sees type: `() -> Int`

---

### Lambda With Parameters

Now things become useful.

**Example**

```kotlin
val square = { number: Int ->
    number * number
}

fun main() {
    println(
        square(5)
    )
}
```

Output:
`25`

**Line By Line**

`{`  
Start lambda.

`number: Int`  
Parameter.

`->`  
Separates parameters from the body.

`number * number`  
Return value.

Compiler infers type: `(Int) -> Int`

**Visual**

Input     | Output
--------- | ------
`Int`     | `Int`

---

### Explicit Function Type

Very important for readability and compiler safety.

**Example**

```kotlin
val square: (Int) -> Int = { number ->
    number * number
}
```

Meaning:  
The variable `square` can only hold a function that accepts an `Int` and returns an `Int`.

The compiler validates that the lambda on the right matches this contract.

---

### Multiple Parameters

**Working Example**

```kotlin
val add = { a: Int, b: Int ->
    a + b
}

fun main() {
    println(
        add(10, 20)
    )
}
```

Output:
`30`

Type: `(Int, Int) -> Int`

**Visual**

```text
(Int, Int)
      |
      |
      V

     Int
```

---

### Lambdas Are Objects

**Interview favorite.**

**Example**

```kotlin
val add = { a: Int, b: Int -> a + b }
```

Compiler roughly creates:

```java
// This is a conceptual representation
class GeneratedLambda implements Function2<Integer, Integer, Integer> {
    public Integer invoke(Integer a, Integer b) {
        return a + b;
    }
}
```

Not exact JVM code, but mentally close. This shows that a lambda is an object with an `invoke` method.

---

### Higher-Order Functions & Trailing Lambdas

Now lambdas become powerful.

**Definition:**  
A function that accepts a function or returns a function.

**Working Example**

```kotlin
fun calculate(
    a: Int,
    b: Int,
    // This parameter is a function
    operation: (Int, Int) -> Int
): Int {
    // We execute the function that was passed in
    return operation(a, b)
}
```

**Usage with Trailing Lambda Syntax**

One of Kotlin's most famous features. If a lambda is the *last* parameter of a function, you can move it outside the parentheses.

```kotlin
fun main() {
    val result = calculate(10, 20) { x, y ->
        x + y
    }
    println(result)
}
```

Output:
`30`

This is the foundation of `map`, `filter`, `Flow`, Compose, Navigation, and Coroutines.

---

### The Implicit `it` Parameter

**Interview favorite.**

If a lambda has **only one parameter**, Kotlin provides an implicit name for it: `it`.

**Example**

```kotlin
// Explicitly naming the single parameter
val printName: (String) -> Unit = { name ->
    println(name)
}

// Using the implicit 'it'
val printNameWithIt: (String) -> Unit = {
    println(it)
}

fun main() {
    printNameWithIt("Akshay")
}
```

Output:
`Akshay`

**When NOT To Use `it`**

If the logic is complex or nested, `it` can become hard to read.

**Bad:**
```kotlin
list.map {
    it.name.uppercase().trim().reversed()
}
```

**Prefer:**
```kotlin
list.map { user ->
    user.name.uppercase().trim().reversed()
}
```

---

### Lambdas Capture Variables (Closures)

This is huge.

**Example**

```kotlin
fun main() {
    var count = 0

    val increment = {
        count++ // The lambda can access and modify 'count'
    }

    increment()
    increment()

    println(count)
}
```

Output:
`2`

**What Is A Closure?**  
A lambda "closes over" or remembers the variables from its surrounding scope, even if that scope has finished executing.

**Android Example**

```kotlin
var clicks = 0

Button(
    onClick = {
        clicks++ // This lambda captures 'clicks'
    }
)
```

---

### Lambda vs. Function Reference

Both can often be used interchangeably when the function signature matches.

```kotlin
// Using a lambda
list.forEach { item ->
    println(item)
}

// Using the implicit 'it'
list.forEach {
    println(it)
}

// Using a function reference (cleanest for this case)
list.forEach(::println)
```

---

## Interview Notes

**What Is A Lambda?**  
An anonymous function that can be treated like a value (stored, passed, returned).

**What Is A Function Type?**  
The signature of a function, e.g., `(Int) -> String`, which means it accepts an `Int` and returns a `String`.

**What Is A Higher-Order Function?**  
A function that accepts a function as a parameter or returns a function.

**What Is Trailing Lambda Syntax?**  
A syntactic sugar that allows moving the last lambda argument of a function call outside the parentheses for better readability.

**What Is `it`?**  
The implicit, default name for the single parameter in a lambda.

**What Is A Closure?**  
The ability of a lambda to access and manipulate variables from its containing (outer) scope.

**Are Lambdas Objects?**  
Yes. Under the hood, the compiler generates a class that implements a `FunctionN` interface, with an `invoke` method containing the lambda's body.

---

## Exercises

**1. Predict Output**

```kotlin
val greeting = {
    println("Hello")
}

fun main() {
    // What happens here?
}
```

**2. What Is The Type?**

```kotlin
val square = { x: Int ->
    x * x
}
```

**3. Why Does This Work?**

```kotlin
var count = 0

val increment = {
    count++
}
```
Explain using the concept of closures.

**4. Compose Question**

What is the function type of the `onClick` parameter in a Compose `Button`?

```kotlin
Button(
    onClick = {
        println("Clicked")
    }
)
```

---

I've also updated your main `README.md` to include this new module in the navigation guide.

```diff
--- a/d:\KotlinApp\AdsMobTest\Kotlin Interview\README.md
+++ b/d:\KotlinApp\AdsMobTest\Kotlin Interview\README.md
@@ -30,6 +30,10 @@
 ### **Module 2: Classes, Objects, Properties & Constructors**
 This module transitions into Object-Oriented Kotlin. It is the foundation for modeling real-world data and architectural components (ViewModels, Repositories, UiState, etc.). You will learn exactly what the compiler generates for properties, how data classes differ from normal classes, how inheritance and sealed classes shape state modeling, and how singletons and companion objects replace Java's `static`.
 
+### **Module 3: Functional Kotlin**
+This module covers the core philosophy of functional programming. You will learn why functions are "first-class citizens," how lambdas replace boilerplate, and how higher-order functions like `map` and `filter` are built. This is the foundation for understanding modern libraries like Flow and Compose.
+
 ---
 
 ## 🗺️ Topic Navigation Guide
@@ -90,3 +94,9 @@
   * `companion object` (Kotlin's alternative to Java's `static`)
   * Factory patterns & `const val`
   * Anonymous Objects / Object Expressions (`object : Listener {}`)
   * `data object` (Clean toString() for sealed hierarchies)
+
+### [Module 3 — Functional Kotlin]
+* **Chapter 3.1 — Functions as First-Class Citizens**
+  * What "First-Class" means (Store, Pass, Return)
+  * Function References (`::greet`) and Function Types (`() -> Unit`)
+  * Higher-Order Functions (Passing functions as parameters)
+* **Chapter 3.2 — Lambdas Deep Dive**
+  * What a Lambda is (Anonymous Functions)
+  * Trailing Lambda Syntax & the implicit `it` parameter
+  * Closures (How lambdas capture variables)