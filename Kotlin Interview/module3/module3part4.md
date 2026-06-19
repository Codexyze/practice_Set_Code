# Module 3 — Functional Kotlin

## Chapter 3.4 — map(), filter(), flatMap(), associate(), groupBy(), fold(), reduce()

---

## Why This Chapter Matters

If I open almost any modern Android project:

```kotlin
songs.map { }
users.filter { }
playlist.groupBy { }
results.associateBy { }
uiState.fold(...)
```

I'll find these operators everywhere.

They're heavily used in:

```text
Compose UI
Room
Retrofit
Flow
StateFlow
Paging
KMP
Backend Kotlin
```

Most developers memorize:

```text
map
filter
flatMap
```

but don't understand:

```text
What problem do they solve?
How are they implemented?
What is their complexity?
When should we avoid them?
```

Today we'll learn them properly.

---

## Part 1 — map()

---

## The Problem

Suppose API returns:

```kotlin
data class SongDto(
	val id: Int,
	val title: String
)
```

But UI needs:

```kotlin
data class SongUiModel(
	val displayName: String
)
```

We need transformation.

---

Without map()

```kotlin
fun main() {

	val songs = listOf(
		SongDto(1, "Believer"),
		SongDto(2, "Thunder")
	)

	val uiSongs =
		mutableListOf()

	for(song in songs) {

		uiSongs.add(
			SongUiModel(song.title)
		)
	}

	println(uiSongs)
}
```

Works.

But verbose.

---

## map() Solution

```kotlin
fun main() {

	val songs = listOf(
		SongDto(1, "Believer"),
		SongDto(2, "Thunder")
	)

	val uiSongs =

		songs.map { song ->

			SongUiModel(
				displayName = song.title
			)

		}

	println(uiSongs)
}
```

Output

```text
[
 SongUiModel(Believer),
 SongUiModel(Thunder)
]
```

---

## Theory

map means:

```text
Transform every element

Old Type → New Type
```

---

Visual

```text
1 2 3

 ↓
map

10 20 30
```

---

## How map Works Internally

Simplified implementation:

```kotlin
fun  myMap(

	list: List,

	transform: (T) -> R

): List {

	val result =
		mutableListOf()

	for(item in list) {

		result.add(
			transform(item)
		)
	}

	return result
}
```

---

## Complexity

```text
Time  : O(n)

Memory: O(n)
```

Because new list is created.

---

## Android Example

Room Entity:

```text
SongEntity
```

↓

UI Model:

```text
SongUiModel
```

using:

```kotlin
entities.map {
	it.toUiModel()
}
```

Extremely common.

---

## Part 2 — filter()

---

## Problem

Suppose:

```kotlin
val numbers =
	listOf(
		1,2,3,4,5,6
	)
```

Need:

```text
Only Even Numbers
```

---

Without filter

```kotlin
val result =
	mutableListOf()

for(number in numbers) {

	if(number % 2 == 0) {

		result.add(number)

	}
}
```

---

With filter

```kotlin
val result =

	numbers.filter {

		it % 2 == 0

	}
```

Output

```text
[2,4,6]
```

---

## Theory

filter means:

```text
Keep elements

that satisfy condition
```

---

Visual

```text
1 2 3 4 5 6

filter even

↓

2 4 6
```

---

## Internal Implementation

```kotlin
fun  myFilter(

	list: List,

	predicate: (T) -> Boolean

): List {

	val result =
		mutableListOf()

	for(item in list) {

		if(predicate(item)) {

			result.add(item)

		}
	}

	return result
}
```

---

## Android Example

Songs:

```kotlin
songs.filter {

	it.isFavorite

}
```

Only favorites remain.

---

## Complexity

```text
Time  : O(n)

Memory: O(n)
```

---

## Part 3 — map + filter Chain

Very common.

---

Example

```kotlin
val result =

	songs

		.filter {

			it.isFavorite

		}

		.map {

			it.title

		}
```

---

Visual

```text
Songs

 ↓ filter

Favorite Songs

 ↓ map

Titles
```

---

## Interview Question

Which runs first?

```kotlin
songs
	.filter { }
	.map { }
```

Answer:

```text
filter first

then map
```

---

## Part 4 — flatMap()

Interview favorite.

---

## Problem

Suppose:

```kotlin
val playlists = listOf(

	listOf("Believer","Thunder"),

	listOf("Numb","Faint")

)
```

Structure:

```text
[
 [A,B],
 [C,D]
]
```

Need:

```text
[A,B,C,D]
```

---

## Solution

```kotlin
val result =

	playlists.flatMap {

		it

	}
```

Output

```text
[
 Believer,
 Thunder,
 Numb,
 Faint
]
```

---

## Theory

flatMap:

```text
Map

then

Flatten
```

---

Visual

```text
[[1,2],[3,4]]

↓

flatMap

↓

[1,2,3,4]
```

---

## Internal Idea

```kotlin
for(list in lists) {

	for(item in list) {

		result.add(item)

	}
}
```

---

## Android Example

Suppose:

```text
Albums

 ↓

Songs
```

Need all songs.

```kotlin
albums.flatMap {
	it.songs
}
```

---

## Part 5 — groupBy()

One of the most useful operators.

---

Example

```kotlin
data class Song(

	val title: String,

	val artist: String

)
```

---

Data

```kotlin
val songs = listOf(

	Song("Believer","Imagine Dragons"),

	Song("Thunder","Imagine Dragons"),

	Song("Numb","Linkin Park")
)
```

---

Group

```kotlin
val grouped =

	songs.groupBy {

		it.artist

	}
```

Output

```text
Imagine Dragons ->
  [Believer, Thunder]

Linkin Park ->
  [Numb]
```

---

## Theory

groupBy creates:

```text
Map>
```

---

Visual

```text
Songs

↓

Artist

↓

Grouped Songs
```

---

## Android Example

Music Player

```kotlin
songs.groupBy {
	it.albumName
}
```

For album sections.

---

## Part 6 — associate()

Less common but powerful.

---

Example

```kotlin
val users = listOf(

	User(1,"Akshay"),

	User(2,"Rahul")
)
```

Need:

```text
Map
```

---

Solution

```kotlin
val userMap =

	users.associateBy {

		it.id

	}
```

Output

```text
{
 1 -> User(...)
 2 -> User(...)
}
```

---

## Why Useful?

Instead of:

```kotlin
users.find {
	it.id == 2
}
```

every time.

Use:

```kotlin
userMap[2]
```

O(1) lookup.

---

## Android Example

Caching.

```kotlin
songs.associateBy {
	it.id
}
```

Very common.

---

## Part 7 — reduce()

This is where functional thinking gets serious.

---

Problem

Need total:

```text
1 + 2 + 3 + 4
```

---

Solution

```kotlin
val result =

	listOf(1,2,3,4)

		.reduce { acc, value ->

			acc + value

		}
```

Output

```text
10
```

---

## How reduce Works

Iteration 1

```text
acc = 1

value = 2

result = 3
```

---

Iteration 2

```text
acc = 3

value = 3

result = 6
```

---

Iteration 3

```text
acc = 6

value = 4

result = 10
```

---

## Visual

```text
1 2 3 4

↓

3

↓

6

↓

10
```

---

## Important

reduce uses:

```text
First element
```

as initial value.

---

Empty list?

```kotlin
emptyList()
	.reduce { a,b -> a+b }
```

Crash.

---

## Part 8 — fold()

Safer version.

---

Example

```kotlin
val result =

	listOf(1,2,3,4)

		.fold(0) { acc, value ->

			acc + value

		}
```

Output

```text
10
```

---

## Difference

reduce:

```text
Uses first element
```

---

fold:

```text
Uses supplied initial value
```

---

Example

```kotlin
.fold(100)
```

Result:

```text
110
```

---

## Why Fold Is Powerful

Can create anything.

---

Example

```kotlin
val text =

	listOf(
		"A",
		"B",
		"C"
	)

	.fold("") { acc, item ->

		acc + item

	}
```

Output

```text
ABC
```

---

## Android Example

Total Playlist Duration

```kotlin
val totalDuration =

	songs.fold(0L) {

		total, song ->

		total + song.duration

	}
```

---

## Reduce vs Fold

Interview favorite.

---

reduce

```text
Uses first element

Cannot handle empty list
```

---

fold

```text
Uses initial value

Can handle empty list
```

---

## Real Android Pipeline

Suppose:

```text
Room Entity
```

↓

```text
Filter Favorites
```

↓

```text
Convert To UI Model
```

↓

```text
Group By Album
```

---

Code

```kotlin
val result =

	entities

		.filter {
			it.isFavorite
		}

		.map {
			it.toUiModel()
		}

		.groupBy {
			it.album
		}
```

This is the kind of code you'll see daily in production Android.

---

## Common Mistakes

### Mistake 1

Using map for filtering.

Wrong:

```kotlin
list.map {
	if(it > 5) it
}
```

Use filter.

---

### Mistake 2

Using reduce on empty list.

Crash.

Use fold.

---

### Mistake 3

Long chains everywhere.

```kotlin
list
.map{}
.filter{}
.map{}
.filter{}
.groupBy{}
```

Can hurt readability.

Extract functions.

---

## Interview Notes

### map()

Transforms elements.

---

### filter()

Keeps matching elements.

---

### flatMap()

Maps then flattens nested collections.

---

### groupBy()

Groups items by key.

Returns:

```text
Map>
```

---

### associateBy()

Creates map.

Returns:

```text
Map
```

---

### reduce()

Accumulates using first element.

---

### fold()

Accumulates using provided initial value.

---

### Difference Between map and flatMap?

map:

```text
1 -> 1 result
```

---

flatMap:

```text
1 -> many results
```

and flatten.

---

## Exercises

### 1 Predict Output

```kotlin
val result =

	listOf(1,2,3)

		.map {

			it * 2

		}

println(result)
```

---

### 2 Predict Output

```kotlin
val result =

	listOf(1,2,3,4)

		.filter {

			it % 2 == 0

		}

println(result)
```

---

### 3 Difference?

```kotlin
reduce
```

vs

```kotlin
fold
```

Explain with empty list example.

---

### 4 Android Question

You fetched:

```text
List
```

Need:

```text
List
```

Which operator would you use and why?
