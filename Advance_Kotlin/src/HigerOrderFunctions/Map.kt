package HigerOrderFunctions

fun main() {
    // ------------------- THEORY -------------------
    // map is used to transform each element of a collection into another form.
    // It takes each element, applies a function, and returns a new list of transformed elements.
    //
    // Why use map?
    // Instead of writing loops manually, map makes it cleaner and functional.
    // Example: Converting a list of numbers into their squares.

    val numbers = listOf(1, 2, 3, 4, 5) // original list

    // Using map to square each number
    val squares = numbers.map { number ->
        number * number  // transformation function
    }

    println("Original numbers: $numbers")
    println("Squared numbers: $squares")
}
