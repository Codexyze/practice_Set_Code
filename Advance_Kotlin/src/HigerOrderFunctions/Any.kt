package HigerOrderFunctions

fun main() {
    // Create a list of numbers from 1 to 10
    val nums = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    // Use the 'any' function to check a condition on the list
    val is5Present = nums.any { it ->
        // 'it' represents each element of the list, one by one
        it == 5 // Check if the current element is equal to 5
        // 'any' returns true if **at least one element** satisfies this condition
        // If no element satisfies it, 'any' returns false
    }

    // Print the result
    println(is5Present) // prints true, because 5 exists in the list
}
