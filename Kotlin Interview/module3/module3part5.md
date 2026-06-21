# Module 3 — Functional Kotlin

## Chapter 3.5 — Scope Functions Deep Dive

---

## Why This Chapter Matters

This chapter is one of the most misunderstood topics in Kotlin.

Most developers memorize:

```text
let
run
with
apply
also
```

using tables.

Then forget them.

I don't want you to memorize.

I want you to understand:

```text
Why they exist
What problem they solve
How compiler sees them
When Android developers use them
When NOT to use them
```

---

## The Problem Scope Functions Solve

Imagine:

```kotlin
data class User(
	var name: String,
	var age: Int
)
```

Without scope functions:

```kotlin
fun main() {

	val user = User(
		name = "Akshay",
		age = 22
	)

	user.name = "Akshay S"
	user.age = 23

	println(user.name)
	println(user.age)
}
```

Works.

But when object becomes larger:

```text
user.name
user.age
user.email
user.phone
user.address
```

repetition becomes annoying.

Kotlin says:

```text
Let's temporarily move into object's scope.
```

This is where scope functions come from.

---

## Before Learning All Five

You only need to remember two concepts.

---

## Concept 1 — Receiver

Example:

```kotlin
user.run {
	println(name)
	println(age)
}
```

Notice:

```text
name
```

instead of:

```text
user.name
```

Why?

Because object becomes:

```text
Receiver
```

inside block.

Compiler treats:

```text
name
```

as:

```text
this.name
```

---

## Concept 2 — Argument

Example:

```kotlin
user.let {
	println(it.name)
	println(it.age)
}
```

Object becomes:

```text
it
```

instead of receiver.

Visual

```text
Receiver
this.name
this.age
```

vs

```text
Argument
it.name
it.age
```

Everything about scope functions is basically:

```text
Receiver vs Argument
and
Return Value
```

---

## Part 1 — let

---

## Theory

let provides object as:

```text
it
```

and returns:

```text
Lambda Result
```

---

## Working Example

```kotlin
data class User(
	val name: String
)

fun main() {

	val user =
		User("Akshay")

	user.let {
		println(it.name)
	}
}
```

Output:

```text
Akshay
```

---

## Line By Line

```text
user.let
```

Call let on object.

```text
{
```

Lambda starts.

```text
it
```

Refers to user.

```text
it.name
```

Equivalent to:

```text
user.name
```

---

## Why Android Uses let

Null safety.

Example:

```kotlin
val user: User? = getUser()

user?.let {
	println(it.name)
}
```

Meaning:

```text
If user exists
execute block
```

This pattern appears everywhere.

---

## Interview Question

Why is let commonly used with nullable types?

Answer:

```text
Executes block only when value is non-null.
```

---

## Part 2 — run

---

## Theory

run provides object as:

```text
this
```

and returns:

```text
Lambda Result
```

---

## Working Example

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

	val text =
		user.run {
			"$name is $age"
		}

	println(text)
}
```

Output

```text
Akshay is 22
```

---

## Line By Line

```text
user.run
```

Object becomes receiver.

Inside block:

```text
name
```

actually means:

```text
this.name
```

```text
"$name is $age"
```

Last expression.

Returned automatically.

---

## Why run Exists

Object configuration.

Calculation.

Transformation.

Android Example

```kotlin
val title =
	song.run {
		"$artist - $title"
	}
```

---

## Part 3 — with

---

## Theory

with is almost same as run.

Difference:

```text
Called differently.
```

Example:

```kotlin
with(user) {
	println(name)
	println(age)
}
```

Notice:

```text
with(user)
```

instead of:

```text
user.run
```

---

## Working Example

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

	val result =
		with(user) {
			"$name $age"
		}

	println(result)
}
```

Output

```text
Akshay 22
```

---

## Why with Exists

Useful when object already exists.

Example:

```kotlin
with(binding) {
	tvTitle.text = "Hello"
	tvSubtitle.text = "World"
}
```

Very common in XML Android.

---

## Part 4 — apply

---

## Theory

apply provides:

```text
this
```

and returns:

```text
Original Object
```

This is critical.

---

## Working Example

```kotlin
data class User(
	var name: String = "",
	var age: Int = 0
)

fun main() {

	val user =
		User()
			.apply {
				name = "Akshay"
				age = 22
			}

	println(user)
}
```

Output

```text
User(name=Akshay, age=22)
```

---

## Line By Line

```text
User()
```

Create object.

```text
.apply
```

Enter receiver scope.

```text
name = "Akshay"
```

actually:

```text
this.name = "Akshay"
```

Most important:

After apply finishes:

```text
returns User object
```

---

## Why Android Loves apply

Builder style.

Example:

```kotlin
Intent(
	context,
	PlayerActivity::class.java
).apply {
	putExtra("songId", 1)
}
```

Without apply:

```kotlin
val intent = Intent(...)
intent.putExtra(...)
```

Cleaner.

---

## Part 5 — also

---

## Theory

also provides:

```text
it
```

and returns:

```text
Original Object
```

---

## Working Example

```kotlin
val user =
	User(
		"Akshay",
		22
	)
		.also {
			println(it)
		}
```

Output

```text
User(name=Akshay, age=22)
```

---

## Why also Exists

Side effects.

Logging.

Debugging.

Analytics.

Example:

```kotlin
repository
	.getSongs()
	.also {
		println(
			"Loaded ${it.size}"
		)
	}
```

Main operation unchanged.

Just observe result.

---

## The Golden Rule

Most confusion disappears if you remember:

```text
Function    Object Access    Returns
let         it               Lambda Result
run         this             Lambda Result
with        this             Lambda Result
apply       this             Original Object
also        it               Original Object
```

---

## Memory Trick

### let

Think:

```text
let me use it
```

Uses:

```text
it
```

### run

Think:

```text
run calculation
```

Returns lambda result.

### apply

Think:

```text
apply configuration
```

Returns configured object.

### also

Think:

```text
also do this
```

Logging.

Analytics.

Side effects.

Returns original object.

---

## Android Examples

### let

Nullable data.

```kotlin
user?.let {
	showUser(it)
}
```

### run

Build result.

```kotlin
val text =
	user.run {
		"$name $age"
	}
```

### apply

Object setup.

```kotlin
val paint =
	Paint().apply {
		textSize = 18f
	}
```

### also

Logging.

```kotlin
songs
	.also {
		Log.d(
			"Songs",
			"${it.size}"
		)
	}
```

---

## Interview Favorite

Question:

Difference between:

```text
apply
```

and

```text
also
```

Answer:

Both return:

```text
Original Object
```

Difference:

apply

```text
this
```

also

```text
it
```

Question:

Difference between:

```text
let
```

and

```text
run
```

Answer:

Both return:

```text
Lambda Result
```

Difference:

let

```text
it
```

run

```text
this
```

---

## Common Mistakes

### Mistake 1

Nested scope functions.

Bad:

```kotlin
user.run {
	address?.let {
		city.run {
			...
		}
	}
}
```

Unreadable.

### Mistake 2

Using apply for calculations.

Bad:

```kotlin
val result =
	user.apply {
		age + 5
	}
```

Returns:

```text
user
```

not result.

### Mistake 3

Using let everywhere.

Some developers write:

```kotlin
user.let {}
```

for everything.

Don't.

Use it when it improves readability.

---

## JVM View

This:

```kotlin
user.let {
	println(it.name)
}
```

roughly becomes:

```kotlin
val temp = user
println(temp.name)
```

Scope functions are mostly inline utility functions.

No magic.

---

## Senior Android Takeaway

When reviewing production Android code:

```text
apply
```

usually means:

```text
Object Configuration
```

```text
also
```

usually means:

```text
Side Effect
Logging
Tracking
```

```text
let
```

usually means:

```text
Nullable Handling
Small Transformation
```

```text
run
```

usually means:

```text
Compute Result
```

Understanding these patterns makes modern Android code much easier to read.

---

## Exercises

### Predict Output

```kotlin
val result =
	"Akshay".let {
		it.length
	}

println(result)
```

---

### Predict Output

```kotlin
val result =
	"Akshay".run {
		length
	}

println(result)
```

---

### What Does This Return?

```kotlin
val user =
	User()
		.apply {
			name = "Akshay"
		}
```

---

### Why Use also Here?

```kotlin
repository
	.getSongs()
	.also {
		println(it.size)
	}
```
