package HigerOrderFunctions

fun main() {
    // ------------------- THEORY -------------------
    // groupingBy groups elements of a collection based on a key.
    // It returns a map: key → list of items with that key.
    //
    // Why use groupingBy?
    // Very useful when we need to categorize data automatically.
    //
    // Example: Grouping words by their first letter.

    val words = listOf("apple", "ant", "banana", "bat", "ball", "cat")

    // Group words based on their starting character
    val grouped = words.groupingBy { word ->
        word.first() // key is the first letter
    }.eachCount() // we can also count how many words in each group

    println("Original words: $words")
    println("Grouped words by first letter: $grouped")
}
