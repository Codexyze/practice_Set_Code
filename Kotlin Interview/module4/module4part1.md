# Module 4 — Collections Mastery

## Chapter 4.1 — Collection Internals (List, MutableList, Set, Map)

---

## Why This Chapter Matters

Almost every Android app you've built uses collections.

Examples from your projects:

```text
List<Song>
List<Playlist>
List<FoodItem>
List<Workout>
List<Message>
```

In Compose:

```kotlin
LazyColumn(
	items(songs) { song ->
		SongItem(song)
	}
)
```

In Room:

```kotlin
@Query("SELECT * FROM songs")
suspend fun getSongs(): List<SongEntity>
```

In Retrofit:

```kotlin
suspend fun getSongs(): List<SongDto>
```

We use collections daily.

But most developers don't know:

```text
What actually lives in memory?
Why is ArrayList fast?
Why is Set unique?
Why is HashMap O(1)?
Why can some operations become O(n)?
```

This chapter fixes that.

---

## What Is A Collection?

A collection is simply:

```text
A container that stores multiple values.
```

Example:

```kotlin
val songs = listOf(
	"Believer",
	"Thunder",
	"Numb"
)
```

Memory conceptually:

```text
songs  |  |  V
+-----------+
| Believer  |
| Thunder   |
| Numb      |
+-----------+
```

Without collections:

```kotlin
val song1 = "Believer"
val song2 = "Thunder"
val song3 = "Numb"
```

Impossible to scale.

Collections solve this.

---

## Kotlin Collection Hierarchy

```text
Iterable
  |
  |
Collection
  |--------------------------------|
			  |              |
			 List           Set           Map
```

Most Android work happens here.

---

## Part 1 — List

---

## What Is List?

Definition:

```text
Ordered collection.
Duplicates allowed.
Index based access.
```

---

Example

```kotlin
val songs = listOf(
	"Believer",
	"Thunder",
	"Believer"
)
```

Valid.

Duplicates allowed.

Access by index:

```kotlin
println(
	songs[0]
)
```

Output:

```text
Believer
```

---

## Important Characteristics

List guarantees:

```text
Order Preserved
Index Access
Duplicates Allowed
```

Example:

```kotlin
val numbers =
	listOf(
		10,
		20,
		30
	)
```

Memory:

```text
Index 0 -> 10
1 -> 20
2 -> 30
```

---

## Why Android Loves Lists

Suppose:

```kotlin
val songs: List<Song>
```

UI displays:

```text
Position 0
Position 1
Position 2
```

Exactly what List provides.

---

## Immutable List

Example:

```kotlin
val songs =
	listOf(
		"Believer",
		"Thunder"
	)
```

Question:

Can we add item?

```kotlin
songs.add("Numb")
```

No.

Compilation error.

Why?

Because:

```text
List
```

is read-only.

Not truly immutable.

Read-only interface.

Important interview point.

---

## Interview Trap

Many developers say:

```text
List is immutable
```

Not entirely accurate.

Example:

```kotlin
val mutable =
	mutableListOf(
		1, 2, 3
	)

val readOnly: List<Int> =
	mutable
```

Now:

```kotlin
mutable.add(4)
```

ReadOnly list sees update.

Meaning:

```text
List = Read Only View
Not True Immutable Collection
```

Interview favorite.

---

## Part 2 — MutableList

---

Definition

```text
Ordered
Duplicates Allowed
Can Modify
```

Example:

```kotlin
val songs =
	mutableListOf<String>()
```

Add elements:

```kotlin
songs.add("Believer")
songs.add("Thunder")
```

Remove:

```kotlin
songs.remove("Believer")
```

Replace:

```kotlin
songs[0] = "Numb"
```

---

## Memory View

```kotlin
val songs =
	mutableListOf(
		"Believer",
		"Thunder"
	)
```

Memory:

```text
Index 0 -> Believer
1 -> Thunder
```

After:

```kotlin
songs.add("Numb")
```

Memory:

```text
0 -> Believer
1 -> Thunder
2 -> Numb
```

---

## Part 3 — ArrayList Internals

Now things become interesting.

Question:

What actually powers:

```kotlin
mutableListOf()
```

?

Answer:

```text
ArrayList
```

Most Kotlin Lists on JVM use:

```text
java.util.ArrayList
```

under the hood.

---

## ArrayList Internals

Imagine:

```kotlin
val songs =
	mutableListOf<String>()
```

Internally:

```text
Array
```

gets created.

Visual:

```text
Index 0 -> Empty
1 -> Empty
2 -> Empty
3 -> Empty
4 -> Empty
```

Add:

```kotlin
songs.add("Believer")
```

Memory:

```text
0 -> Believer
1 -> Empty
2 -> Empty
3 -> Empty
4 -> Empty
```

Add:

```kotlin
songs.add("Thunder")
```

```text
0 -> Believer
1 -> Thunder
2 -> Empty
3 -> Empty
4 -> Empty
```

---

## Why Index Access Is O(1)

Example:

```kotlin
songs[1]
```

Array knows:

```text
Memory Address + Index
```

Formula conceptually:

```text
baseAddress + index
```

Direct jump.

No searching.

Complexity:

```text
O(1)
```

Constant time.

---

## Interview Question

Complexity of:

```kotlin
songs[5000]
```

Answer:

```text
O(1)
```

Whether:

```kotlin
songs[5]
```

or

```kotlin
songs[500000]
```

same complexity.

---

## Why Insert Can Be Expensive

Example:

```kotlin
songs.add(
	0,
	"New Song"
)
```

Current:

```text
0 -> A
1 -> B
2 -> C
```

Need:

```text
0 -> New
1 -> A
2 -> B
3 -> C
```

Everything shifts.

Visual:

```text
A -> move
B -> move
C -> move
```

Complexity:

```text
O(n)
```

Interview favorite.

---

## Android Example

Suppose:

```text
LazyColumn
```

with:

```text
10000 songs
```

Adding at beginning repeatedly:

```kotlin
songs.add(0, newSong)
```

can become expensive.

---

## Part 4 — Set

---

## Definition

```text
Unique Elements Only
```

Example:

```kotlin
val numbers =
	setOf(
		1,
		2,
		2,
		3
	)
```

Output:

```text
[1,2,3]
```

Duplicate removed.

---

## Why?

Set enforces uniqueness.

Visual:

```text
Add 1
Set: [1]

Add 2
Set: [1,2]

Add 2 again
Ignored
```

---

## Android Example

Suppose:

```text
Recently Played Artists
```

Need duplicates removed.

Set useful.

---

## Part 5 — HashSet Internals

Most Sets use:

```text
HashSet
```

internally.

HashSet uses:

```text
hashCode()
```

Example:

```kotlin
data class User(
	val id: Int
)
```

When adding:

```kotlin
set.add(
	User(1)
)
```

HashSet computes:

```kotlin
user.hashCode()
```

Then stores item in bucket.

Why Data Classes Matter

Remember:

```text
data class User(...)
```

generates:

```text
equals()
hashCode()
```

Automatically.

That's one reason data classes work beautifully inside Sets and Maps.

---

## Part 6 — Map

One of the most important collections.

---

## Definition

```text
Key → Value
```

relationship.

Example:

```kotlin
val songs = mapOf(
	1 to "Believer",
	2 to "Thunder"
)
```

Memory:

```text
1 -> Believer
2 -> Thunder
```

Access:

```kotlin
println(
	songs[1]
)
```

Output:

```text
Believer
```

---

## Why Not List?

Imagine:

```text
100000 songs
```

Need song with:

```text
ID = 58234
```

List approach:

```kotlin
songs.find {
	it.id == 58234
}
```

Complexity:

```text
O(n)
```

Must search.

Map:

```kotlin
songMap[58234]
```

Complexity:

```text
O(1)
```

Huge difference.

---

## Android Example

Caching.

```kotlin
val songMap =
	songs.associateBy {
		it.id
	}
```

Now:

```kotlin
songMap[id]
```

Fast lookup.

---

## HashMap Internals

Question:

How can Map be O(1)?

Magic?

No.

Hashing.

Suppose:

```kotlin
map[123]
```

HashMap computes:

```kotlin
123.hashCode()
```

Then finds bucket.

Visual:

```text
Bucket 0
Bucket 1
Bucket 2
Bucket 3
```

Hash determines bucket.

Then:

```text
Direct Access
```

instead of full search.

---

## Complexity Table

```text
Operation     ArrayList   Get By Index   O(1)
			  Add End     O(1) amortized
			  Add Start   O(n)
			  Remove Mid  O(n)
			  Search      O(n)

Operation     HashMap     Get   O(1)
						  Put   O(1)
						  Remove O(1)
```

Average case.

---

## Android Examples

### Music Player

```kotlin
val songs: List<Song>
```

Display UI.

### Food Tracker

```kotlin
val foods: List<Food>
```

Show meals.

### Fast Lookup

```kotlin
val foodMap =
	foods.associateBy {
		it.id
	}
```

### Unique Artists

```kotlin
val artists =
	songs.map {
		it.artist
	}.toSet()
```

---

## Common Mistakes

### Mistake 1

Using List for fast lookup.

Bad:

```kotlin
songs.find {
	it.id == id
}
```

Repeatedly.

Use:

```text
Map
```

### Mistake 2

Using MutableList everywhere.

Prefer:

```text
List
```

when mutation unnecessary.

### Mistake 3

Ignoring complexity.

```kotlin
add(0, item)
```

inside large loops.

Expensive.

---

## Interview Notes

### Difference Between List and Set?

List:

```text
Ordered
Duplicates Allowed
```

Set:

```text
Unique Values
```

### Difference Between List and MutableList?

List:

```text
Read Only
```

MutableList:

```text
Can Modify
```

### Why Is ArrayList Access O(1)?

Direct index calculation.

### Why Is Insertion At Start O(n)?

All elements shift.

### Why Is HashMap Fast?

Uses hashing.

Direct bucket lookup.

### Why Are Data Classes Good Map Keys?

Generated:

```text
equals()
hashCode()
```

---

## Exercises

### 1 Predict Output

```kotlin
val numbers =
	setOf(
		1,
		2,
		2,
		3
	)

println(numbers)
```

---

### 2 Complexity?

```kotlin
songs[50000]
```

in ArrayList.

---

### 3 Complexity?

```kotlin
songs.add(
	0,
	newSong
)
```

---

### 4 Android Design Question

You have:

```text
100000 songs
```

and repeatedly search by:

```text
songId
```

Would you choose:

```kotlin
List<Song>
```

or

```kotlin
Map<Int, Song>
```

Explain why.
